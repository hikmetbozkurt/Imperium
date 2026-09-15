package com.hikmet.imperium.domain.game

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.QuestionResponse
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
    val remainingTimeMs: Long = GameRules.QUESTION_DURATION_MS,
    val elapsedTimeMs: Long = 0L,
    val questionElapsedTimeMs: Long = 0L,
    val isCurrentQuestionTimedOut: Boolean = false,
    val sessionSeed: Long = 0L,
    val responses: List<QuestionResponse> = emptyList(),
    val status: QuizSessionStatus = QuizSessionStatus.ACTIVE,
) {
    init {
        require(levelNumber > 0) { "Level number must be positive" }
        require(questions.isNotEmpty()) { "A quiz session needs at least one question" }
        require(currentQuestionIndex in questions.indices) { "Current question index is invalid" }
        require(remainingTimeMs >= 0) { "Remaining time cannot be negative" }
        require(elapsedTimeMs >= 0) { "Elapsed time cannot be negative" }
        require(questionElapsedTimeMs >= 0) { "Question time cannot be negative" }
        require(!isCurrentQuestionTimedOut || remainingTimeMs == 0L) {
            "A timed out question cannot have remaining time"
        }
        require(responses.map(QuestionResponse::position).distinct().size == responses.size) {
            "A session cannot contain duplicate response positions"
        }
    }

    val currentQuestion: QuizQuestion
        get() = questions[currentQuestionIndex]

    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questions.lastIndex

    val isAnswerRevealed: Boolean
        get() = selectedAnswerIndex != null || isCurrentQuestionTimedOut

    fun selectAnswer(answerIndex: Int): QuizSession {
        if (status != QuizSessionStatus.ACTIVE || isAnswerRevealed) return this
        if (answerIndex !in currentQuestion.options.indices) return this

        return copy(
            selectedAnswerIndex = answerIndex,
            correctAnswers = correctAnswers + if (answerIndex == currentQuestion.correctAnswerIndex) 1 else 0,
            responses = responses + QuestionResponse(
                questionId = currentQuestion.id,
                selectedAnswerIndex = answerIndex,
                correctAnswerIndex = currentQuestion.correctAnswerIndex,
                isCorrect = answerIndex == currentQuestion.correctAnswerIndex,
                responseTimeMs = questionElapsedTimeMs,
                position = currentQuestionIndex,
            ),
        )
    }

    fun nextQuestion(): QuizSession {
        if (status != QuizSessionStatus.ACTIVE || !isAnswerRevealed) return this
        if (isLastQuestion) return copy(status = QuizSessionStatus.COMPLETED)

        return copy(
            currentQuestionIndex = currentQuestionIndex + 1,
            selectedAnswerIndex = null,
            remainingTimeMs = GameRules.QUESTION_DURATION_MS,
            questionElapsedTimeMs = 0L,
            isCurrentQuestionTimedOut = false,
        )
    }

    fun tick(deltaMs: Long): QuizSession {
        if (status != QuizSessionStatus.ACTIVE || isAnswerRevealed || deltaMs <= 0) return this

        val consumed = minOf(deltaMs, remainingTimeMs)
        val remaining = remainingTimeMs - consumed
        val didTimeOut = remaining == 0L
        val timedOutResponses = if (didTimeOut) {
            responses + QuestionResponse(
                questionId = currentQuestion.id,
                selectedAnswerIndex = null,
                correctAnswerIndex = currentQuestion.correctAnswerIndex,
                isCorrect = false,
                responseTimeMs = questionElapsedTimeMs + consumed,
                position = currentQuestionIndex,
            )
        } else {
            responses
        }
        return copy(
            remainingTimeMs = remaining,
            elapsedTimeMs = elapsedTimeMs + consumed,
            questionElapsedTimeMs = questionElapsedTimeMs + consumed,
            isCurrentQuestionTimedOut = didTimeOut,
            responses = timedOutResponses,
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
