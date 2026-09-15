package com.hikmet.imperium.domain.game

import com.hikmet.imperium.domain.model.QuizQuestion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionSelectorTest {
    private val selector = QuestionSelector()
    private val pool = (1..20).map { index ->
        QuizQuestion(
            id = "q$index",
            text = "Question $index",
            options = listOf("wrong-$index-a", "correct-$index", "wrong-$index-b", "wrong-$index-c"),
            correctAnswerIndex = 1,
        )
    }

    @Test
    fun `same seed creates the same question and option order`() {
        val first = selector.select(pool, emptySet(), seed = 42)
        val second = selector.select(pool, emptySet(), seed = 42)

        assertEquals(first, second)
        assertEquals(4, first.size)
    }

    @Test
    fun `recent overlap is avoided when enough fresh questions exist`() {
        val recent = pool.take(10).map(QuizQuestion::id).toSet()
        val selected = selector.select(pool, recent, seed = 7)

        assertTrue(selected.none { it.id in recent })
    }

    @Test
    fun `replaying a level uses the unseen half of an eight question bank`() {
        val levelPool = pool.take(8)
        val firstAttempt = selector.select(levelPool, emptySet(), seed = 1)
        val secondAttempt = selector.select(
            pool = levelPool,
            recentQuestionIds = firstAttempt.map(QuizQuestion::id).toSet(),
            seed = 2,
        )

        assertEquals(4, firstAttempt.size)
        assertEquals(4, secondAttempt.size)
        assertTrue(firstAttempt.map(QuizQuestion::id).toSet().intersect(secondAttempt.map(QuizQuestion::id).toSet()).isEmpty())
    }

    @Test
    fun `option shuffle keeps the correct answer attached to its question`() {
        val selected = selector.select(pool, emptySet(), seed = 99)

        selected.forEach { question ->
            assertTrue(question.options[question.correctAnswerIndex].startsWith("correct-"))
        }
    }
}
