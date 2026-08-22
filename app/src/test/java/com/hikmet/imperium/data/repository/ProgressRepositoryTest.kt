package com.hikmet.imperium.data.repository

import com.hikmet.imperium.data.dao.QuizAttemptDao
import com.hikmet.imperium.data.entities.QuizAttemptEntity
import com.hikmet.imperium.domain.model.CategoryId
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressRepositoryTest {
    private val repository = ProgressRepository(FakeQuizAttemptDao(), LocalHistoryContentRepository())

    @Test
    fun `replays do not inflate stars or completed level count`() {
        val today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val overview = repository.calculateOverview(
            listOf(
                attempt("ancient", level = 1, score = 50, stars = 1, completedAt = today - 86_400_000),
                attempt("ancient", level = 1, score = 100, stars = 3, completedAt = today),
                attempt("medieval", level = 1, score = 75, stars = 2, completedAt = today),
            ),
        )

        assertEquals(3, overview.totalQuizzes)
        assertEquals(5, overview.totalStars)
        assertEquals(75, overview.averageScore)
        assertEquals(100, overview.bestScore)
        assertEquals(2, overview.timeline.size)
        val ancient = overview.categories.single { it.categoryId == CategoryId.ANCIENT }
        assertEquals(1, ancient.completedLevels)
        assertEquals(3, ancient.stars)
        assertEquals(75, ancient.averageScore)
    }

    @Test
    fun `zero baseline improvement is safe`() {
        val attempts = (1..5).map { attempt("ancient", it, 0, 0, it.toLong()) } +
            (6..10).map { attempt("ancient", it, 100, 3, it.toLong()) }

        assertEquals(0, repository.calculateOverview(attempts).improvementPercent)
    }

    private fun attempt(
        category: String,
        level: Int,
        score: Int,
        stars: Int,
        completedAt: Long,
    ) = QuizAttemptEntity(
        id = completedAt,
        categoryId = category,
        levelNumber = level,
        score = score,
        stars = stars,
        correctAnswers = score / 25,
        totalQuestions = 4,
        durationMs = 10_000,
        completedAtEpochMs = completedAt,
    )

    private class FakeQuizAttemptDao : QuizAttemptDao {
        override suspend fun insert(attempt: QuizAttemptEntity): Long = attempt.id
        override fun observeById(attemptId: Long): Flow<QuizAttemptEntity?> = flowOf(null)
        override fun observeAll(): Flow<List<QuizAttemptEntity>> = flowOf(emptyList())
        override fun observeForCategory(categoryId: String): Flow<List<QuizAttemptEntity>> = flowOf(emptyList())
        override suspend fun latestForLevel(categoryId: String, levelNumber: Int): QuizAttemptEntity? = null
    }
}
