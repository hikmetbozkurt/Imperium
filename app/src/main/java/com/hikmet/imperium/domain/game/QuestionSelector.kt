package com.hikmet.imperium.domain.game

import com.hikmet.imperium.domain.model.QuizQuestion
import javax.inject.Inject
import kotlin.math.floor
import kotlin.random.Random

data class QuestionSelectionPolicy(
    val questionCount: Int = 4,
    val maxRecentOverlapRatio: Double = 0.4,
) {
    init {
        require(questionCount > 0) { "Question count must be positive" }
        require(maxRecentOverlapRatio in 0.0..1.0) { "Recent overlap ratio must be between zero and one" }
    }
}

/**
 * Creates a reproducible quiz from a larger level pool. Recent questions are used only when the
 * unseen pool cannot fill the session, and answer positions are shuffled with the same seed.
 */
class QuestionSelector @Inject constructor() {
    fun select(
        pool: List<QuizQuestion>,
        recentQuestionIds: Set<String>,
        seed: Long,
        policy: QuestionSelectionPolicy = QuestionSelectionPolicy(),
    ): List<QuizQuestion> {
        if (pool.isEmpty()) return emptyList()

        val random = Random(seed)
        val targetCount = minOf(policy.questionCount, pool.size)
        val fresh = pool.filterNot { it.id in recentQuestionIds }.shuffled(random)
        val recent = pool.filter { it.id in recentQuestionIds }.shuffled(random)
        val allowedRecent = floor(targetCount * policy.maxRecentOverlapRatio).toInt()

        val selected = buildList {
            addAll(fresh.take(targetCount))
            if (size < targetCount) {
                val preferredRecentCount = minOf(targetCount - size, allowedRecent)
                addAll(recent.take(preferredRecentCount))
            }
            if (size < targetCount) {
                addAll(recent.drop(allowedRecent).take(targetCount - size))
            }
        }

        return selected
            .shuffled(random)
            .map { it.shuffleOptions(random) }
    }

    private fun QuizQuestion.shuffleOptions(random: Random): QuizQuestion {
        val shuffled = options
            .mapIndexed { index, option -> index to option }
            .shuffled(random)
        return copy(
            options = shuffled.map { it.second },
            correctAnswerIndex = shuffled.indexOfFirst { (originalIndex, _) ->
                originalIndex == correctAnswerIndex
            },
        )
    }
}
