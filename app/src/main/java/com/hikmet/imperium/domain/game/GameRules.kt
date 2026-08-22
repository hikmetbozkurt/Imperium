package com.hikmet.imperium.domain.game

object GameRules {
    const val DEFAULT_QUIZ_DURATION_MS = 40_000L

    fun score(correctAnswers: Int, totalQuestions: Int): Int {
        if (totalQuestions <= 0) return 0
        return ((correctAnswers.coerceIn(0, totalQuestions) * 100) / totalQuestions)
            .coerceIn(0, 100)
    }

    fun stars(score: Int): Int = when (score.coerceIn(0, 100)) {
        in 80..100 -> 3
        in 60..79 -> 2
        in 40..59 -> 1
        else -> 0
    }

    /** Only an improvement over the previous best contributes to the category total. */
    fun starDelta(previousBest: Int, earned: Int): Int =
        (earned.coerceIn(0, 3) - previousBest.coerceIn(0, 3)).coerceAtLeast(0)

    fun unlockedLevelAfterAttempt(
        currentlyUnlocked: Int,
        completedLevel: Int,
        earnedStars: Int,
        totalLevels: Int,
    ): Int {
        val safeCurrent = currentlyUnlocked.coerceIn(1, totalLevels.coerceAtLeast(1))
        if (earnedStars <= 0 || completedLevel >= totalLevels) return safeCurrent
        return maxOf(safeCurrent, completedLevel + 1).coerceAtMost(totalLevels)
    }
}
