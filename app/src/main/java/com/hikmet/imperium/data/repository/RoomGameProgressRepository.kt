package com.hikmet.imperium.data.repository

import androidx.room.withTransaction
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.QuizAttemptEntity
import com.hikmet.imperium.data.entities.QuestionResponseEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.model.QuizAttempt
import com.hikmet.imperium.domain.model.QuizResult
import com.hikmet.imperium.domain.repository.CategoryProgress
import com.hikmet.imperium.domain.repository.GameProgressRepository
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import com.hikmet.imperium.domain.repository.RecordedAttempt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomGameProgressRepository @Inject constructor(
    private val database: ImperiumDatabase,
    private val contentRepository: HistoryContentRepository,
) : GameProgressRepository {
    override suspend fun recordAttempt(attempt: QuizAttempt): RecordedAttempt =
        database.withTransaction {
            val category = requireNotNull(contentRepository.category(attempt.categoryId)) {
                "Unknown category: ${attempt.categoryId.value}"
            }
            require(category.levels.any { it.number == attempt.levelNumber }) {
                "Unknown level ${attempt.levelNumber} for ${attempt.categoryId.value}"
            }
            require(attempt.totalQuestions > 0) { "An attempt needs at least one question" }
            require(attempt.correctAnswers in 0..attempt.totalQuestions) { "Invalid correct answer count" }

            ensureCategoryExists(category)

            val categoryId = attempt.categoryId.value
            val previousCategory = database.userProgressDao().getProgressForCategory(categoryId)
                ?: UserProgressEntity(categoryId = categoryId)
            require(attempt.levelNumber <= previousCategory.unlockedLevels) {
                "Level ${attempt.levelNumber} is locked for $categoryId"
            }

            val score = GameRules.score(attempt.correctAnswers, attempt.totalQuestions)
            val stars = GameRules.stars(score)
            val previousLevel = database.levelProgressDao()
                .getLevelProgress(categoryId, attempt.levelNumber)
            val previousBestStars = previousLevel?.starsEarned ?: 0
            val bestStarsForLevel = maxOf(previousBestStars, stars)
            val starDelta = GameRules.starDelta(previousBestStars, stars)
            val isSuccessful = GameRules.isPassingStars(stars)

            database.levelProgressDao().insertOrUpdateLevelProgress(
                LevelProgressEntity(
                    categoryId = categoryId,
                    levelNumber = attempt.levelNumber,
                    starsEarned = bestStarsForLevel,
                    highestScore = maxOf(previousLevel?.highestScore ?: 0, score),
                    completed = previousLevel?.completed == true || isSuccessful,
                    bestTimeMs = if (isSuccessful) {
                        bestTime(previousLevel?.bestTimeMs, attempt.durationMs)
                    } else {
                        previousLevel?.bestTimeMs
                    },
                    attemptCount = (previousLevel?.attemptCount ?: 0) + 1,
                    lastPlayedTimestamp = attempt.completedAtEpochMs,
                ),
            )

            val updatedLevelStars = database.levelProgressDao()
                .getLevelProgressForCategory(categoryId)
                .associate { it.levelNumber to it.starsEarned }
            val totalStars = updatedLevelStars.values.sum()
            val unlockedLevels = GameRules.unlockedLevelAfterAttempt(
                currentlyUnlocked = previousCategory.unlockedLevels,
                completedLevel = attempt.levelNumber,
                bestStarsForLevel = bestStarsForLevel,
                totalStars = totalStars,
                requiredStarsByLevel = category.levels.associate { it.number to it.requiredStars },
            )
            val newlyUnlockedLevel = unlockedLevels
                .takeIf { it > previousCategory.unlockedLevels }
            database.userProgressDao().insertOrUpdateProgress(
                previousCategory.copy(
                    unlockedLevels = unlockedLevels,
                    totalStarsEarned = totalStars,
                    highestLevelCompleted = if (isSuccessful) {
                        maxOf(previousCategory.highestLevelCompleted, attempt.levelNumber)
                    } else {
                        previousCategory.highestLevelCompleted
                    },
                    lastPlayedTimestamp = attempt.completedAtEpochMs,
                    levelStars = updatedLevelStars,
                ),
            )

            val result = QuizResult(
                categoryId = attempt.categoryId,
                levelNumber = attempt.levelNumber,
                score = score,
                stars = stars,
                correctAnswers = attempt.correctAnswers,
                totalQuestions = attempt.totalQuestions,
                durationMs = attempt.durationMs,
            )
            val isNewBest = (previousLevel == null && score > 0) ||
                stars > previousBestStars ||
                score > (previousLevel?.highestScore ?: 0)
            val recorded = RecordedAttempt(
                id = 0,
                result = result,
                isNewBest = isNewBest,
                addedStars = starDelta,
                unlockedLevels = unlockedLevels,
                newlyUnlockedLevel = newlyUnlockedLevel,
            )
            val attemptId = database.quizAttemptDao().insert(
                result.toEntity(
                    completedAtEpochMs = attempt.completedAtEpochMs,
                    sessionSeed = attempt.sessionSeed,
                    recorded = recorded,
                ),
            )
            if (attempt.responses.isNotEmpty()) {
                database.questionResponseDao().insertAll(
                    attempt.responses.map { response ->
                        QuestionResponseEntity(
                            attemptId = attemptId,
                            questionId = response.questionId,
                            selectedAnswerIndex = response.selectedAnswerIndex,
                            correctAnswerIndex = response.correctAnswerIndex,
                            isCorrect = response.isCorrect,
                            responseTimeMs = response.responseTimeMs,
                            position = response.position,
                        )
                    },
                )
            }
            recorded.copy(
                id = attemptId,
            )
        }

    override fun observeResult(attemptId: Long): Flow<QuizResult?> =
        database.quizAttemptDao().observeById(attemptId).map { it?.toDomain() }

    override fun observeRecordedAttempt(attemptId: Long): Flow<RecordedAttempt?> =
        database.quizAttemptDao().observeById(attemptId).map { entity ->
            entity?.toRecordedAttempt()
        }

    override fun observeCategory(categoryId: CategoryId): Flow<CategoryProgress> =
        database.userProgressDao().getProgressForCategoryAsFlow(categoryId.value).map { progress ->
            CategoryProgress(
                categoryId = categoryId,
                unlockedLevels = progress?.unlockedLevels ?: 1,
                totalStars = progress?.levelStars?.values?.sum() ?: 0,
                levelStars = progress?.levelStars.orEmpty(),
            )
        }

    override suspend fun recentQuestionIds(
        categoryId: CategoryId,
        levelNumber: Int,
        limit: Int,
    ): Set<String> = database.questionResponseDao()
        .recentForLevel(categoryId.value, levelNumber, limit.coerceAtLeast(1))
        .map(QuestionResponseEntity::questionId)
        .toSet()

    private suspend fun ensureCategoryExists(category: HistoryCategory) {
        if (database.categoryDao().getCategoryById(category.id.value) != null) return
        database.categoryDao().insertCategories(
            listOf(
                CategoryEntity(
                    id = category.id.value,
                    title = category.title,
                    description = category.description,
                    longDescription = category.longDescription,
                    totalLevels = category.levels.size,
                    gradientStartColor = category.gradientStartColor.toInt(),
                    gradientEndColor = category.gradientEndColor.toInt(),
                    iconResourceName = category.iconResourceName,
                ),
            ),
        )
    }

    private fun bestTime(previous: Long?, candidate: Long): Long =
        previous?.let { minOf(it, candidate) } ?: candidate

    private fun QuizResult.toEntity(
        completedAtEpochMs: Long,
        sessionSeed: Long,
        recorded: RecordedAttempt,
    ) = QuizAttemptEntity(
        categoryId = categoryId.value,
        levelNumber = levelNumber,
        score = score,
        stars = stars,
        correctAnswers = correctAnswers,
        totalQuestions = totalQuestions,
        durationMs = durationMs,
        completedAtEpochMs = completedAtEpochMs,
        sessionSeed = sessionSeed,
        isNewBest = recorded.isNewBest,
        addedStars = recorded.addedStars,
        unlockedLevels = recorded.unlockedLevels,
        newlyUnlockedLevel = recorded.newlyUnlockedLevel,
    )

    private fun QuizAttemptEntity.toDomain(): QuizResult? {
        val category = CategoryId.from(categoryId) ?: return null
        return QuizResult(
            categoryId = category,
            levelNumber = levelNumber,
            score = score,
            stars = stars,
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            durationMs = durationMs,
        )
    }

    private fun QuizAttemptEntity.toRecordedAttempt(): RecordedAttempt? {
        val result = toDomain() ?: return null
        return RecordedAttempt(
            id = id,
            result = result,
            isNewBest = isNewBest,
            addedStars = addedStars,
            unlockedLevels = unlockedLevels,
            newlyUnlockedLevel = newlyUnlockedLevel,
        )
    }
}
