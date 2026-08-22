package com.hikmet.imperium.domain.game

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.QuizQuestion
import com.hikmet.imperium.domain.model.QuizResult

enum class QuizSessionStatus {
    ACTIVE,
    COMPLETED,
}
data class QuizSession(
    val categoryId: CategoryId,
    val levelNumber: Int,
    val questions: List<QuizQuestion>,
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val correctAnswers: Int = 0,
    val remainingTimeMs: Long = GameRules.DEFAULT_QUIZ_DURATION_MS,
    val elapsedTimeMs: Long = 0L,
    val status: QuizSessionStatus = QuizSessionStatus.ACTIVE,
) {
    init {
        require(levelNumber > 0) { "Level number must be positive" }
        require(questions.isNotEmpty()) { "A quiz session needs at least one question" }
        require(currentQuestionIndex in questions.indices) { "Current question index is invalid" }
        require(remainingTimeMs >= 0) { "Remaining time cannot be negative" }
        require(elapsedTimeMs >= 0) { "Elapsed time cannot be negative" }
    }

    val currentQuestion: QuizQuestion
        get() = questions[currentQuestionIndex]

    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questions.lastIndex

    fun selectAnswer(answerIndex: Int): QuizSession {
        if (status != QuizSessionStatus.ACTIVE || selectedAnswerIndex != null) return this
        if (answerIndex !in currentQuestion.options.indices) return this

        return copy(
            selectedAnswerIndex = answerIndex,
            correctAnswers = correctAnswers + if (answerIndex == currentQuestion.correctAnswerIndex) 1 else 0,
        )
    }

    fun nextQuestion(): QuizSession {
        if (status != QuizSessionStatus.ACTIVE || selectedAnswerIndex == null) return this
        if (isLastQuestion) return copy(status = QuizSessionStatus.COMPLETED)

        return copy(
            currentQuestionIndex = currentQuestionIndex + 1,
            selectedAnswerIndex = null,
        )
    }

    fun tick(deltaMs: Long): QuizSession {
        if (status != QuizSessionStatus.ACTIVE || deltaMs <= 0) return this

        val consumed = minOf(deltaMs, remainingTimeMs)
        val remaining = remainingTimeMs - consumed
        return copy(
            remainingTimeMs = remaining,
            elapsedTimeMs = elapsedTimeMs + consumed,
            status = if (remaining == 0L) QuizSessionStatus.COMPLETED else status,
        )
    }

    fun result(): QuizResult? {
        if (status != QuizSessionStatus.COMPLETED) return null
        val score = GameRules.score(correctAnswers, questions.size)
        return QuizResult(
            categoryId = categoryId,
            levelNumber = levelNumber,
            score = score,
            stars = GameRules.stars(score),
            correctAnswers = correctAnswers,
            totalQuestions = questions.size,
            durationMs = elapsedTimeMs,
        )
    }
}
