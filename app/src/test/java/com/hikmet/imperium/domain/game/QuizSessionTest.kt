package com.hikmet.imperium.domain.game

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.QuizQuestion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuizSessionTest {
    private val questions = listOf(
        QuizQuestion("q1", "Question one?", listOf("A", "B"), correctAnswerIndex = 1),
        QuizQuestion("q2", "Question two?", listOf("A", "B"), correctAnswerIndex = 0),
    )

    @Test
    fun `answer can be scored only once`() {
        val answered = session().selectAnswer(1).selectAnswer(1)

        assertEquals(1, answered.correctAnswers)
        assertEquals(1, answered.selectedAnswerIndex)
        assertEquals(1, answered.responses.size)
        assertEquals(true, answered.responses.single().isCorrect)
    }

    @Test
    fun `next requires an answer and resets selection`() {
        assertEquals(0, session().nextQuestion().currentQuestionIndex)

        val next = session().selectAnswer(1).nextQuestion()
        assertEquals(1, next.currentQuestionIndex)
        assertNull(next.selectedAnswerIndex)
    }

    @Test
    fun `last answer completes quiz and creates deterministic result`() {
        val completed = session()
            .selectAnswer(1)
            .nextQuestion()
            .selectAnswer(1)
            .nextQuestion()

        val result = completed.result()!!
        assertEquals(QuizSessionStatus.COMPLETED, completed.status)
        assertEquals(50, result.score)
        assertEquals(1, result.stars)
        assertEquals(1, result.correctAnswers)
    }

    @Test
    fun `timeout reveals current answer and pauses until user continues`() {
        val timedOut = session(durationMs = 1_000).tick(1_500)

        assertEquals(QuizSessionStatus.ACTIVE, timedOut.status)
        assertEquals(true, timedOut.isCurrentQuestionTimedOut)
        assertEquals(1_000, timedOut.elapsedTimeMs)
        assertNull(timedOut.result())
        assertNull(timedOut.responses.single().selectedAnswerIndex)

        val next = timedOut.nextQuestion()
        assertEquals(1, next.currentQuestionIndex)
        assertEquals(GameRules.QUESTION_DURATION_MS, next.remainingTimeMs)
        assertEquals(false, next.isCurrentQuestionTimedOut)
    }

    @Test
    fun `timer stops after an answer is selected`() {
        val answered = session(durationMs = 5_000).tick(1_000).selectAnswer(1)

        assertEquals(answered, answered.tick(2_000))
    }

    @Test
    fun `each question receives a fresh timer`() {
        val next = session()
            .tick(12_000)
            .selectAnswer(1)
            .nextQuestion()

        assertEquals(GameRules.QUESTION_DURATION_MS, next.remainingTimeMs)
        assertEquals(0L, next.questionElapsedTimeMs)
        assertEquals(12_000L, next.elapsedTimeMs)
    }

    @Test
    fun `timing out the last question can complete the challenge`() {
        val completed = session(durationMs = 1_000)
            .tick(1_000)
            .nextQuestion()
            .tick(GameRules.QUESTION_DURATION_MS)
            .nextQuestion()

        assertEquals(QuizSessionStatus.COMPLETED, completed.status)
        assertEquals(2, completed.responses.size)
        assertEquals(0, completed.result()?.correctAnswers)
    }

    private fun session(durationMs: Long = 40_000) = QuizSession(
        categoryId = CategoryId.ANCIENT,
        levelNumber = 1,
        questions = questions,
        remainingTimeMs = durationMs,
    )
}
