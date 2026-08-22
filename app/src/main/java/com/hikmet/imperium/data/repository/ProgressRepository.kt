package com.hikmet.imperium.data.repository

import com.hikmet.imperium.data.dao.QuizAttemptDao
import com.hikmet.imperium.data.entities.QuizAttemptEntity
import com.hikmet.imperium.domain.repository.CategoryAnalytics
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import com.hikmet.imperium.domain.repository.ProgressAnalyticsRepository
import com.hikmet.imperium.domain.repository.ProgressOverview
import com.hikmet.imperium.domain.repository.ProgressPoint
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

/** Calculates analytics exclusively from persisted, immutable attempt history. */
@Singleton
class ProgressRepository @Inject constructor(
    private val quizAttemptDao: QuizAttemptDao,
    private val contentRepository: HistoryContentRepository,
) : ProgressAnalyticsRepository {
    private val zoneId = ZoneId.systemDefault()

    override fun observeProgress(): Flow<ProgressOverview> =
        quizAttemptDao.observeAll().map(::calculateOverview)

    internal fun calculateOverview(attempts: List<QuizAttemptEntity>): ProgressOverview {
        val chronological = attempts.sortedBy(QuizAttemptEntity::completedAtEpochMs)
        val bestStarsByLevel = attempts
            .groupBy { it.categoryId to it.levelNumber }
            .mapValues { (_, values) -> values.maxOf(QuizAttemptEntity::stars) }

        return ProgressOverview(
            totalQuizzes = attempts.size,
            averageScore = attempts.map(QuizAttemptEntity::score).averageOrZero(),
            bestScore = attempts.maxOfOrNull(QuizAttemptEntity::score) ?: 0,
            totalStars = bestStarsByLevel.values.sum(),
            currentStreakDays = calculateStreak(attempts),
            improvementPercent = calculateImprovement(chronological),
            timeline = attempts
                .groupBy { it.completedAtEpochMs.toLocalDate().toEpochDay() }
                .map { (epochDay, dailyAttempts) ->
                    ProgressPoint(
                        epochDay = epochDay,
                        averageScore = dailyAttempts.map(QuizAttemptEntity::score).averageOrZero(),
                        quizCount = dailyAttempts.size,
                    )
                }
                .sortedBy(ProgressPoint::epochDay),
            categories = contentRepository.categories().map { category ->
                val categoryAttempts = attempts.filter { it.categoryId == category.id.value }
                CategoryAnalytics(
                    categoryId = category.id,
                    title = category.title,
                    completedLevels = categoryAttempts.map(QuizAttemptEntity::levelNumber).distinct().size,
                    totalLevels = category.levels.size,
                    stars = bestStarsByLevel
                        .filterKeys { (categoryId, _) -> categoryId == category.id.value }
                        .values
                        .sum(),
                    averageScore = categoryAttempts.map(QuizAttemptEntity::score).averageOrZero(),
                )
            },
        )
    }

    private fun calculateStreak(attempts: List<QuizAttemptEntity>): Int {
        val activeDays = attempts.map { it.completedAtEpochMs.toLocalDate() }.toSet()
        if (activeDays.isEmpty()) return 0
        val today = LocalDate.now(zoneId)
        var cursor = if (today in activeDays) today else today.minusDays(1)
        if (cursor !in activeDays) return 0
        var streak = 0
        while (cursor in activeDays) {
            streak++
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    private fun calculateImprovement(attempts: List<QuizAttemptEntity>): Int {
        if (attempts.size < 2) return 0
        val recent = attempts.takeLast(5).map(QuizAttemptEntity::score)
        val previous = attempts.dropLast(recent.size).takeLast(5).map(QuizAttemptEntity::score)
        if (previous.isEmpty()) return 0
        val previousAverage = previous.average()
        if (previousAverage == 0.0) return 0
        return (((recent.average() - previousAverage) / previousAverage) * 100)
            .roundToInt()
            .coerceIn(-100, 100)
    }

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()

    private fun List<Int>.averageOrZero(): Int =
        if (isEmpty()) 0 else average().roundToInt()
}
