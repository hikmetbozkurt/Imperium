package com.hikmet.imperium.domain.game

object GameRules {
    const val QUESTION_DURATION_MS = 30_000L
    const val TIMER_WARNING_THRESHOLD_MS = 20_000L
    const val TIMER_CRITICAL_THRESHOLD_MS = 10_000L
    const val MINIMUM_PASSING_STARS = 2

    /** Kept for callers that need an estimated upper bound for a complete challenge. */
    fun quizDurationMs(questionCount: Int): Long =
        questionCount.coerceAtLeast(1) * QUESTION_DURATION_MS

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

    fun isPassingStars(stars: Int): Boolean = stars >= MINIMUM_PASSING_STARS

    /**
     * Advances a category by one contiguous level only.
     *
     * A high score on an earlier level can improve the category star total, but it cannot skip
     * the current progression frontier. Existing unlocks are treated as a high-water mark so an
     * app update never takes previously unlocked content away from the player.
     */
    fun unlockedLevelAfterAttempt(
        currentlyUnlocked: Int,
        completedLevel: Int,
        bestStarsForLevel: Int,
        totalStars: Int,
        requiredStarsByLevel: Map<Int, Int>,
    ): Int {
        val firstLevel = requiredStarsByLevel.keys.minOrNull() ?: 1
        val lastLevel = requiredStarsByLevel.keys.maxOrNull() ?: firstLevel
        val safeCurrent = currentlyUnlocked.coerceIn(firstLevel, lastLevel)
        if (completedLevel != safeCurrent || !isPassingStars(bestStarsForLevel)) return safeCurrent

        val nextLevel = requiredStarsByLevel.keys
            .filter { it > safeCurrent }
            .minOrNull()
            ?: return safeCurrent
        val requiredStars = requiredStarsByLevel.getValue(nextLevel)
        return if (totalStars.coerceAtLeast(0) >= requiredStars) nextLevel else safeCurrent
    }
}
