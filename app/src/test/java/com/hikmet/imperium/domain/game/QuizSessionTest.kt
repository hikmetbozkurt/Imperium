package com.hikmet.imperium.domain.game

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.QuizQuestion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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

    @Test
    fun `mora adds fifteen seconds once and caps the question at forty five seconds`() {
        val extended = session(durationMs = 40_000).addTime()

        assertEquals(45_000L, extended.remainingTimeMs)
        assertTrue(extended.moraUsed)
        assertEquals(extended, extended.addTime())
    }

    @Test
    fun `mora cannot be consumed after the answer is revealed`() {
        val answered = session().selectAnswer(1)

        assertEquals(answered, answered.addTime())
        assertFalse(answered.moraUsed)
    }

    @Test
    fun `fifty fifty deterministically hides two wrong answers only once`() {
        val original = fourOptionSession(seed = 73L)
        val reduced = original.useFiftyFifty()

        assertTrue(reduced.fiftyFiftyUsed)
        assertEquals(2, reduced.hiddenOptionIndices.size)
        assertFalse(reduced.hiddenOptionIndices.contains(original.currentQuestion.correctAnswerIndex))
        assertEquals(reduced.hiddenOptionIndices, fourOptionSession(seed = 73L).useFiftyFifty().hiddenOptionIndices)
        assertEquals(reduced, reduced.useFiftyFifty())
    }

    @Test
    fun `fifty fifty choices clear on next question but the lifeline stays consumed`() {
        val reduced = fourOptionSession(seed = 91L).useFiftyFifty()
        val next = reduced
            .selectAnswer(reduced.currentQuestion.correctAnswerIndex)
            .nextQuestion()

        assertTrue(next.fiftyFiftyUsed)
        assertTrue(next.hiddenOptionIndices.isEmpty())
        assertEquals(next, next.useFiftyFifty())
    }

    private fun session(durationMs: Long = 40_000) = QuizSession(
        categoryId = CategoryId.ANCIENT,
        levelNumber = 1,
        questions = questions,
        remainingTimeMs = durationMs,
    )

    private fun fourOptionSession(seed: Long) = QuizSession(
        categoryId = CategoryId.ANCIENT,
        levelNumber = 1,
        questions = listOf(
            QuizQuestion("four-1", "Question?", listOf("A", "B", "C", "D"), correctAnswerIndex = 2),
            QuizQuestion("four-2", "Next?", listOf("A", "B", "C", "D"), correctAnswerIndex = 0),
        ),
        sessionSeed = seed,
    )
}
