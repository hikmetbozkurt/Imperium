package com.hikmet.imperium.domain.model

data class QuizQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val conceptId: String = id,
    val difficulty: Int = 2,
    val explanation: String? = null,
) {
    init {
        require(text.isNotBlank()) { "Question text cannot be blank" }
        require(options.size >= 2) { "A question needs at least two options" }
        require(correctAnswerIndex in options.indices) { "Correct answer index is outside the options" }
        require(difficulty in 1..5) { "Question difficulty must be between 1 and 5" }
    }
}

data class QuestionResponse(
    val questionId: String,
    val selectedAnswerIndex: Int?,
    val correctAnswerIndex: Int,
    val isCorrect: Boolean,
    val responseTimeMs: Long,
    val position: Int,
) {
    init {
        require(questionId.isNotBlank()) { "Question id cannot be blank" }
        require(selectedAnswerIndex == null || selectedAnswerIndex >= 0) { "Selected answer index is invalid" }
        require(correctAnswerIndex >= 0) { "Correct answer index is invalid" }
        require(responseTimeMs >= 0) { "Response time cannot be negative" }
        require(position >= 0) { "Question position cannot be negative" }
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
    val sessionSeed: Long = 0L,
    val responses: List<QuestionResponse> = emptyList(),
) {
    init {
        require(levelNumber > 0) { "Level number must be positive" }
        require(totalQuestions > 0) { "An attempt needs at least one question" }
        require(correctAnswers in 0..totalQuestions) { "Correct answers must fit the question count" }
        require(durationMs >= 0) { "Duration cannot be negative" }
        require(completedAtEpochMs >= 0) { "Completion time cannot be negative" }
        require(responses.map(QuestionResponse::position).distinct().size == responses.size) {
            "An attempt cannot contain duplicate response positions"
        }
    }
}
