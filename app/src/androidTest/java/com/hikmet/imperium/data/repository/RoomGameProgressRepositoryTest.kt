package com.hikmet.imperium.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.QuizAttempt
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
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
        val level = database.levelProgressDao().getLevelProgress("ancient", 1)
        assertEquals(3, level?.starsEarned)
        assertEquals(100, level?.highestScore)
        assertEquals(3, level?.attemptCount)
    }

    private fun attempt(correct: Int, completedAt: Long) = QuizAttempt(
        categoryId = CategoryId.ANCIENT,
        levelNumber = 1,
        correctAnswers = correct,
        totalQuestions = 4,
        durationMs = 10_000,
        completedAtEpochMs = completedAt,
    )
}
