package com.hikmet.imperium.feature.quiz

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.domain.game.QuizSession
import com.hikmet.imperium.domain.game.QuizSessionStatus
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.model.QuizAttempt
import com.hikmet.imperium.domain.repository.GameProgressRepository
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import com.hikmet.imperium.ui.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizScreenState(
    val category: HistoryCategory? = null,
    val session: QuizSession? = null,
    val isSaving: Boolean = false,
    val error: String? = null,
)

sealed interface QuizEvent {
    data class OpenResult(val attemptId: Long) : QuizEvent
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val progressRepository: GameProgressRepository,
    contentRepository: HistoryContentRepository,
    private val soundManager: SoundManager,
) : ViewModel() {
    private val _state = MutableStateFlow(QuizScreenState())
    val state: StateFlow<QuizScreenState> = _state.asStateFlow()

    private val eventChannel = Channel<QuizEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private var isCompletionStarted = false

    init {
        val categoryId = savedStateHandle.get<String>("categoryId")?.let(CategoryId::from)
        val levelNumber = savedStateHandle.get<Int>("levelNumber")
        val category = categoryId?.let(contentRepository::category)
        val questions = if (categoryId != null && levelNumber != null) {
            contentRepository.questions(categoryId, levelNumber).shuffled()
        } else {
            emptyList()
        }

        if (categoryId == null || levelNumber == null || category == null || questions.isEmpty()) {
            _state.value = QuizScreenState(error = "Quiz content could not be loaded")
        } else {
            _state.value = QuizScreenState(
                category = category,
                session = QuizSession(
                    categoryId = categoryId,
                    levelNumber = levelNumber,
                    questions = questions,
                ),
            )
            startTimer()
        }
    }

    fun selectAnswer(index: Int) {
        val session = _state.value.session ?: return
        val updated = session.selectAnswer(index)
        if (updated === session) return
        _state.value = _state.value.copy(session = updated)
        if (index == session.currentQuestion.correctAnswerIndex) {
            soundManager.playCorrectAnswer()
        } else {
            soundManager.playWrongAnswer()
        }
    }

    fun nextQuestion() {
        val session = _state.value.session ?: return
        val updated = session.nextQuestion()
        _state.value = _state.value.copy(session = updated)
        if (updated.status == QuizSessionStatus.COMPLETED) completeQuiz(updated)
    }

    private fun startTimer() {
        viewModelScope.launch {
            var previousTick = SystemClock.elapsedRealtime()
            while (_state.value.session?.status == QuizSessionStatus.ACTIVE) {
                delay(TIMER_RESOLUTION_MS)
                val now = SystemClock.elapsedRealtime()
                val session = _state.value.session ?: break
                val updated = session.tick(now - previousTick)
                previousTick = now
                _state.value = _state.value.copy(session = updated)
                if (updated.status == QuizSessionStatus.COMPLETED) completeQuiz(updated)
            }
        }
    }

    private fun completeQuiz(session: QuizSession) {
        if (isCompletionStarted) return
        isCompletionStarted = true
        _state.value = _state.value.copy(isSaving = true, session = session)
        viewModelScope.launch {
            runCatching {
                progressRepository.recordAttempt(
                    QuizAttempt(
                        categoryId = session.categoryId,
                        levelNumber = session.levelNumber,
                        correctAnswers = session.correctAnswers,
                        totalQuestions = session.questions.size,
                        durationMs = session.elapsedTimeMs,
                        completedAtEpochMs = System.currentTimeMillis(),
                    ),
                )
            }.onSuccess { recorded ->
                soundManager.playLevelComplete()
                eventChannel.send(QuizEvent.OpenResult(recorded.id))
            }.onFailure { throwable ->
                isCompletionStarted = false
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = throwable.message ?: "Progress could not be saved",
                )
            }
        }
    }

    private companion object {
        const val TIMER_RESOLUTION_MS = 250L
    }
}
