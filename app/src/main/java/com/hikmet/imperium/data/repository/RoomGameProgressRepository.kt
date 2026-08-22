package com.hikmet.imperium.data.repository

import androidx.room.withTransaction
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.QuizAttemptEntity
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

            val score = GameRules.score(attempt.correctAnswers, attempt.totalQuestions)
            val stars = GameRules.stars(score)
            val categoryId = attempt.categoryId.value
            val previousLevel = database.levelProgressDao()
                .getLevelProgress(categoryId, attempt.levelNumber)
            val previousBestStars = previousLevel?.starsEarned ?: 0
            val starDelta = GameRules.starDelta(previousBestStars, stars)

            database.levelProgressDao().insertOrUpdateLevelProgress(
                LevelProgressEntity(
                    categoryId = categoryId,
                    levelNumber = attempt.levelNumber,
                    starsEarned = maxOf(previousBestStars, stars),
                    highestScore = maxOf(previousLevel?.highestScore ?: 0, score),
                    completed = true,
                    bestTimeMs = bestTime(previousLevel?.bestTimeMs, attempt.durationMs),
                    attemptCount = (previousLevel?.attemptCount ?: 0) + 1,
                    lastPlayedTimestamp = attempt.completedAtEpochMs,
                ),
            )

            val previousCategory = database.userProgressDao().getProgressForCategory(categoryId)
                ?: UserProgressEntity(categoryId = categoryId)
            val updatedLevelStars = database.levelProgressDao()
                .getLevelProgressForCategory(categoryId)
                .associate { it.levelNumber to it.starsEarned }
            val unlockedLevels = GameRules.unlockedLevelAfterAttempt(
                currentlyUnlocked = previousCategory.unlockedLevels,
                completedLevel = attempt.levelNumber,
                earnedStars = stars,
                totalLevels = category.levels.size,
            )
            database.userProgressDao().insertOrUpdateProgress(
                previousCategory.copy(
                    unlockedLevels = unlockedLevels,
                    totalStarsEarned = updatedLevelStars.values.sum(),
                    highestLevelCompleted = maxOf(
                        previousCategory.highestLevelCompleted,
                        attempt.levelNumber,
                    ),
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
            val attemptId = database.quizAttemptDao().insert(result.toEntity(attempt.completedAtEpochMs))
            RecordedAttempt(
                id = attemptId,
                result = result,
                isNewBest = previousLevel == null ||
                    stars > previousBestStars ||
                    score > previousLevel.highestScore,
                addedStars = starDelta,
                unlockedLevels = unlockedLevels,
            )
        }

    override fun observeResult(attemptId: Long): Flow<QuizResult?> =
        database.quizAttemptDao().observeById(attemptId).map { it?.toDomain() }

    override fun observeCategory(categoryId: CategoryId): Flow<CategoryProgress> =
        database.userProgressDao().getProgressForCategoryAsFlow(categoryId.value).map { progress ->
            CategoryProgress(
                categoryId = categoryId,
                unlockedLevels = progress?.unlockedLevels ?: 1,
                totalStars = progress?.levelStars?.values?.sum() ?: 0,
                levelStars = progress?.levelStars.orEmpty(),
            )
        }

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

    private fun QuizResult.toEntity(completedAtEpochMs: Long) = QuizAttemptEntity(
        categoryId = categoryId.value,
        levelNumber = levelNumber,
        score = score,
        stars = stars,
        correctAnswers = correctAnswers,
        totalQuestions = totalQuestions,
        durationMs = durationMs,
        completedAtEpochMs = completedAtEpochMs,
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
}
