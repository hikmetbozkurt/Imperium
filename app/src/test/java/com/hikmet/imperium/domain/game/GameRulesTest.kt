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
    fun `successful attempt unlocks at most the next valid level`() {
        assertEquals(2, GameRules.unlockedLevelAfterAttempt(1, 1, 1, 20))
        assertEquals(7, GameRules.unlockedLevelAfterAttempt(7, 3, 3, 20))
        assertEquals(20, GameRules.unlockedLevelAfterAttempt(20, 20, 3, 20))
        assertEquals(4, GameRules.unlockedLevelAfterAttempt(4, 4, 0, 20))
    }
}
