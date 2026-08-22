package com.hikmet.imperium.domain.model

data class QuizQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
) {
    init {
        require(text.isNotBlank()) { "Question text cannot be blank" }
        require(options.size >= 2) { "A question needs at least two options" }
        require(correctAnswerIndex in options.indices) { "Correct answer index is outside the options" }
    }
}

data class QuizResult(
    val categoryId: CategoryId,
    val levelNumber: Int,
    val score: Int,
    val stars: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val durationMs: Long,
)

data class QuizAttempt(
    val categoryId: CategoryId,
    val levelNumber: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val durationMs: Long,
    val completedAtEpochMs: Long,
) {
    init {
        require(levelNumber > 0) { "Level number must be positive" }
        require(totalQuestions > 0) { "An attempt needs at least one question" }
        require(correctAnswers in 0..totalQuestions) { "Correct answers must fit the question count" }
        require(durationMs >= 0) { "Duration cannot be negative" }
        require(completedAtEpochMs >= 0) { "Completion time cannot be negative" }
    }
}
