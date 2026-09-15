package com.hikmet.imperium.feature.quiz

import org.junit.Assert.assertEquals
import org.junit.Test

class QuizPresentationTest {

    @Test
    fun `timer seconds are rendered as roman numerals`() {
        assertEquals("0", romanNumeral(0))
        assertEquals("XVI", romanNumeral(16))
        assertEquals("XXX", romanNumeral(30))
    }

    @Test
    fun `wrong selection reveals both selected and correct options`() {
        assertEquals(
            QuizAnswerVisualState.Incorrect,
            answerVisualState(index = 2, selectedIndex = 2, correctIndex = 0, isRevealed = true),
        )
        assertEquals(
            QuizAnswerVisualState.Correct,
            answerVisualState(index = 0, selectedIndex = 2, correctIndex = 0, isRevealed = true),
        )
        assertEquals(
            QuizAnswerVisualState.Dimmed,
            answerVisualState(index = 1, selectedIndex = 2, correctIndex = 0, isRevealed = true),
        )
    }

    @Test
    fun `unanswered options remain available`() {
        assertEquals(
            QuizAnswerVisualState.Default,
            answerVisualState(index = 1, selectedIndex = null, correctIndex = 0, isRevealed = false),
        )
    }

    @Test
    fun `correct selection is rendered as correct without an incorrect state`() {
        assertEquals(
            QuizAnswerVisualState.Correct,
            answerVisualState(index = 1, selectedIndex = 1, correctIndex = 1, isRevealed = true),
        )
        assertEquals(
            QuizAnswerVisualState.Dimmed,
            answerVisualState(index = 0, selectedIndex = 1, correctIndex = 1, isRevealed = true),
        )
    }

    @Test
    fun `timeout reveals the correct option and dims every distractor`() {
        assertEquals(
            QuizAnswerVisualState.Correct,
            answerVisualState(index = 3, selectedIndex = null, correctIndex = 3, isRevealed = true),
        )
        assertEquals(
            QuizAnswerVisualState.Dimmed,
            answerVisualState(index = 0, selectedIndex = null, correctIndex = 3, isRevealed = true),
        )
    }
}
