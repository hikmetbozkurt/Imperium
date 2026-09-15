package com.hikmet.imperium.domain.game

import org.junit.Assert.assertEquals
import org.junit.Test

class GameRulesTest {
    @Test
    fun `score handles boundaries and invalid totals`() {
        assertEquals(0, GameRules.score(correctAnswers = 1, totalQuestions = 0))
        assertEquals(0, GameRules.score(correctAnswers = -1, totalQuestions = 4))
        assertEquals(50, GameRules.score(correctAnswers = 2, totalQuestions = 4))
        assertEquals(100, GameRules.score(correctAnswers = 9, totalQuestions = 4))
    }

    @Test
    fun `stars match the product thresholds`() {
        assertEquals(0, GameRules.stars(39))
        assertEquals(1, GameRules.stars(40))
        assertEquals(2, GameRules.stars(60))
        assertEquals(3, GameRules.stars(80))
    }

    @Test
    fun `replay contributes only improved stars`() {
        assertEquals(2, GameRules.starDelta(previousBest = 0, earned = 2))
        assertEquals(0, GameRules.starDelta(previousBest = 2, earned = 2))
        assertEquals(0, GameRules.starDelta(previousBest = 3, earned = 1))
        assertEquals(1, GameRules.starDelta(previousBest = 2, earned = 3))
    }

    @Test
    fun `only a passing attempt at the progression frontier unlocks one level`() {
        val requirements = mapOf(1 to 0, 2 to 2, 3 to 4, 4 to 6)

        assertEquals(2, GameRules.unlockedLevelAfterAttempt(1, 1, 2, 2, requirements))
        assertEquals(2, GameRules.unlockedLevelAfterAttempt(2, 1, 3, 5, requirements))
        assertEquals(2, GameRules.unlockedLevelAfterAttempt(2, 2, 1, 5, requirements))
        assertEquals(2, GameRules.unlockedLevelAfterAttempt(2, 2, 3, 3, requirements))
        assertEquals(3, GameRules.unlockedLevelAfterAttempt(2, 2, 3, 6, requirements))
        assertEquals(4, GameRules.unlockedLevelAfterAttempt(4, 4, 3, 12, requirements))
    }

    @Test
    fun `quiz duration scales without shortening existing sessions`() {
        assertEquals(120_000L, GameRules.quizDurationMs(4))
        assertEquals(240_000L, GameRules.quizDurationMs(8))
    }

    @Test
    fun `two stars is the minimum passing result`() {
        assertEquals(false, GameRules.isPassingStars(0))
        assertEquals(false, GameRules.isPassingStars(1))
        assertEquals(true, GameRules.isPassingStars(2))
        assertEquals(true, GameRules.isPassingStars(3))
    }
}
