package com.hikmet.imperium.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.QuizAttempt
import com.hikmet.imperium.domain.model.QuestionResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomGameProgressRepositoryTest {
    private lateinit var database: ImperiumDatabase
    private lateinit var repository: RoomGameProgressRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            ImperiumDatabase::class.java,
        ).build()
        repository = RoomGameProgressRepository(database, LocalHistoryContentRepository())
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun replayUpdatesBestAtomicallyWithoutInflatingStars() = runBlocking {
        val first = repository.recordAttempt(attempt(correct = 3, completedAt = 1))
        val improved = repository.recordAttempt(attempt(correct = 4, completedAt = 2))
        val replay = repository.recordAttempt(attempt(correct = 2, completedAt = 3))

        assertEquals(2, first.addedStars)
        assertEquals(1, improved.addedStars)
        assertEquals(0, replay.addedStars)
        val category = repository.observeCategory(CategoryId.ANCIENT).first()
        assertEquals(3, category.totalStars)
        assertEquals(2, category.unlockedLevels)
        assertEquals(3, database.quizAttemptDao().observeAll().first().size)
        assertEquals(setOf("q-3", "q-2", "q-1"), repository.recentQuestionIds(CategoryId.ANCIENT, 1))
        val recorded = repository.observeRecordedAttempt(improved.id).first()
        assertEquals(true, recorded?.isNewBest)
        assertEquals(1, recorded?.addedStars)
        val level = database.levelProgressDao().getLevelProgress("ancient", 1)
        assertEquals(3, level?.starsEarned)
        assertEquals(100, level?.highestScore)
        assertEquals(3, level?.attemptCount)
    }

    @Test
    fun failedAttemptDoesNotCompleteLevelOrCreateBestTime() = runBlocking {
        repository.recordAttempt(attempt(correct = 1, completedAt = 1))

        val level = database.levelProgressDao().getLevelProgress("ancient", 1)
        assertEquals(false, level?.completed)
        assertEquals(null, level?.bestTimeMs)
        assertEquals(1, repository.observeCategory(CategoryId.ANCIENT).first().unlockedLevels)
    }

    @Test
    fun oneStarRecordsTheBestButDoesNotPassOrUnlock() = runBlocking {
        repository.recordAttempt(attempt(correct = 2, completedAt = 1))

        val level = database.levelProgressDao().getLevelProgress("ancient", 1)
        assertEquals(1, level?.starsEarned)
        assertEquals(false, level?.completed)
        assertEquals(null, level?.bestTimeMs)
        assertEquals(1, repository.observeCategory(CategoryId.ANCIENT).first().unlockedLevels)
    }

    @Test
    fun perfectAttemptsAdvanceOnlyOneContiguousLevel() = runBlocking {
        val first = repository.recordAttempt(attempt(level = 1, correct = 4, completedAt = 1))
        val second = repository.recordAttempt(attempt(level = 2, correct = 4, completedAt = 2))

        assertEquals(2, first.unlockedLevels)
        assertEquals(3, second.unlockedLevels)
        assertEquals(3, repository.observeCategory(CategoryId.ANCIENT).first().unlockedLevels)

        val lockedAttempt = runCatching {
            repository.recordAttempt(attempt(level = 4, correct = 4, completedAt = 3))
        }
        assertTrue(lockedAttempt.exceptionOrNull() is IllegalArgumentException)
    }

    private fun attempt(level: Int = 1, correct: Int, completedAt: Long) = QuizAttempt(
        categoryId = CategoryId.ANCIENT,
        levelNumber = level,
        correctAnswers = correct,
        totalQuestions = 4,
        durationMs = 10_000,
        completedAtEpochMs = completedAt,
        sessionSeed = completedAt,
        responses = listOf(
            QuestionResponse(
                questionId = "q-$completedAt",
                selectedAnswerIndex = 0,
                correctAnswerIndex = if (correct > 0) 0 else 1,
                isCorrect = correct > 0,
                responseTimeMs = 2_000,
                position = 0,
            ),
        ),
    )
}
