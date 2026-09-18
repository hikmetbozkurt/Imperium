package com.hikmet.imperium.feature.quiz

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.domain.game.QuizSession
import com.hikmet.imperium.domain.game.QuizSessionStatus
import com.hikmet.imperium.domain.game.QuestionSelector
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.model.QuestionResponse
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
import kotlinx.coroutines.flow.first
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
    private val savedStateHandle: SavedStateHandle,
    private val progressRepository: GameProgressRepository,
    contentRepository: HistoryContentRepository,
    private val questionSelector: QuestionSelector,
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
        val pool = if (categoryId != null && levelNumber != null) {
            contentRepository.questions(categoryId, levelNumber)
        } else emptyList()

        if (categoryId == null || levelNumber == null || category == null || pool.isEmpty()) {
            _state.value = QuizScreenState(error = "Quiz content could not be loaded")
        } else {
            val sessionSeed = savedStateHandle.get<Long>(KEY_SESSION_SEED)
                ?: newSessionSeed().also { savedStateHandle[KEY_SESSION_SEED] = it }
            viewModelScope.launch {
                val progress = runCatching {
                    progressRepository.observeCategory(categoryId).first()
                }.getOrElse { throwable ->
                    _state.value = QuizScreenState(
                        category = category,
                        error = throwable.message ?: "Progress could not be loaded",
                    )
                    return@launch
                }
                if (levelNumber > progress.unlockedLevels) {
                    _state.value = QuizScreenState(category = category, error = "This level is locked")
                    return@launch
                }
                val recentQuestionIds = runCatching {
                    progressRepository.recentQuestionIds(categoryId, levelNumber)
                }.getOrDefault(emptySet())
                val questions = questionSelector.select(
                    pool = pool,
                    recentQuestionIds = recentQuestionIds,
                    seed = sessionSeed,
                )
                val session = restoreSession(
                    categoryId = categoryId,
                    levelNumber = levelNumber,
                    questions = questions,
                    sessionSeed = sessionSeed,
                ) ?: QuizSession(
                    categoryId = categoryId,
                    levelNumber = levelNumber,
                    questions = questions,
                    sessionSeed = sessionSeed,
                )
                _state.value = QuizScreenState(category = category, session = session)
                persistSession(session)
                startTimer()
            }
        }
    }

    fun selectAnswer(index: Int) {
        val session = _state.value.session ?: return
        val updated = session.selectAnswer(index)
        if (updated === session) return
        _state.value = _state.value.copy(session = updated)
        persistSession(updated)
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
        persistSession(updated)
        if (updated.status == QuizSessionStatus.COMPLETED) completeQuiz(updated)
    }

    fun useMora() = updateActiveSession(QuizSession::addTime)

    fun useFiftyFifty() = updateActiveSession(QuizSession::useFiftyFifty)

    private fun updateActiveSession(transform: (QuizSession) -> QuizSession) {
        val session = _state.value.session ?: return
        val updated = transform(session)
        if (updated === session || updated == session) return
        _state.value = _state.value.copy(session = updated)
        persistSession(updated)
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
                if (updated !== session) {
                    _state.value = _state.value.copy(session = updated)
                }
                if (!session.isCurrentQuestionTimedOut && updated.isCurrentQuestionTimedOut) {
                    soundManager.playWrongAnswer()
                }
                if (updated.remainingTimeMs / 1_000 != session.remainingTimeMs / 1_000 ||
                    updated.status == QuizSessionStatus.COMPLETED
                ) {
                    persistSession(updated)
                }
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
                        sessionSeed = session.sessionSeed,
                        responses = session.responses,
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

    private fun persistSession(session: QuizSession) {
        savedStateHandle[KEY_CURRENT_INDEX] = session.currentQuestionIndex
        savedStateHandle[KEY_SELECTED_INDEX] = session.selectedAnswerIndex
        savedStateHandle[KEY_CORRECT_ANSWERS] = session.correctAnswers
        savedStateHandle[KEY_REMAINING_TIME] = session.remainingTimeMs
        savedStateHandle[KEY_ELAPSED_TIME] = session.elapsedTimeMs
        savedStateHandle[KEY_QUESTION_TIME] = session.questionElapsedTimeMs
        savedStateHandle[KEY_TIMED_OUT] = session.isCurrentQuestionTimedOut
        savedStateHandle[KEY_MORA_USED] = session.moraUsed
        savedStateHandle[KEY_FIFTY_FIFTY_USED] = session.fiftyFiftyUsed
        savedStateHandle[KEY_HIDDEN_OPTIONS] = ArrayList(session.hiddenOptionIndices)
        savedStateHandle[KEY_RESPONSES] = ArrayList(session.responses.map(::encodeResponse))
    }

    private fun restoreSession(
        categoryId: CategoryId,
        levelNumber: Int,
        questions: List<com.hikmet.imperium.domain.model.QuizQuestion>,
        sessionSeed: Long,
    ): QuizSession? {
        val currentIndex = savedStateHandle.get<Int>(KEY_CURRENT_INDEX) ?: return null
        if (currentIndex !in questions.indices) return null
        val selectedIndex = savedStateHandle.get<Int>(KEY_SELECTED_INDEX)
        if (selectedIndex != null && selectedIndex !in questions[currentIndex].options.indices) return null
        val correctAnswers = savedStateHandle.get<Int>(KEY_CORRECT_ANSWERS) ?: 0
        val remainingTime = savedStateHandle.get<Long>(KEY_REMAINING_TIME) ?: return null
        val elapsedTime = savedStateHandle.get<Long>(KEY_ELAPSED_TIME) ?: 0L
        val questionTime = savedStateHandle.get<Long>(KEY_QUESTION_TIME) ?: 0L
        val isTimedOut = savedStateHandle.get<Boolean>(KEY_TIMED_OUT) ?: false
        val moraUsed = savedStateHandle.get<Boolean>(KEY_MORA_USED) ?: false
        val fiftyFiftyUsed = savedStateHandle.get<Boolean>(KEY_FIFTY_FIFTY_USED) ?: false
        val hiddenOptions = savedStateHandle.get<ArrayList<Int>>(KEY_HIDDEN_OPTIONS).orEmpty().toSet()
        val responses = savedStateHandle.get<ArrayList<String>>(KEY_RESPONSES)
            .orEmpty()
            .mapNotNull(::decodeResponse)
        return runCatching {
            QuizSession(
                categoryId = categoryId,
                levelNumber = levelNumber,
                questions = questions,
                currentQuestionIndex = currentIndex,
                selectedAnswerIndex = selectedIndex,
                correctAnswers = correctAnswers,
                remainingTimeMs = remainingTime.coerceIn(0L, GameRules.MORA_MAX_TIME_MS),
                elapsedTimeMs = elapsedTime,
                questionElapsedTimeMs = questionTime,
                isCurrentQuestionTimedOut = isTimedOut,
                moraUsed = moraUsed,
                fiftyFiftyUsed = fiftyFiftyUsed,
                hiddenOptionIndices = hiddenOptions,
                sessionSeed = sessionSeed,
                responses = responses,
            )
        }.getOrNull()
    }

    private fun encodeResponse(response: QuestionResponse): String = listOf(
        response.questionId,
        response.selectedAnswerIndex ?: NO_SELECTED_ANSWER,
        response.correctAnswerIndex,
        if (response.isCorrect) 1 else 0,
        response.responseTimeMs,
        response.position,
    ).joinToString(RESPONSE_SEPARATOR)

    private fun decodeResponse(value: String): QuestionResponse? {
        val fields = value.split(RESPONSE_SEPARATOR)
        if (fields.size != RESPONSE_FIELD_COUNT) return null
        return runCatching {
            val selectedIndex = fields[1].toInt().takeUnless { it == NO_SELECTED_ANSWER }
            QuestionResponse(
                questionId = fields[0],
                selectedAnswerIndex = selectedIndex,
                correctAnswerIndex = fields[2].toInt(),
                isCorrect = fields[3] == "1",
                responseTimeMs = fields[4].toLong(),
                position = fields[5].toInt(),
            )
        }.getOrNull()
    }

    private fun newSessionSeed(): Long = System.currentTimeMillis() xor System.nanoTime()

    private companion object {
        const val TIMER_RESOLUTION_MS = 250L
        const val KEY_SESSION_SEED = "quiz_session_seed"
        const val KEY_CURRENT_INDEX = "quiz_current_index"
        const val KEY_SELECTED_INDEX = "quiz_selected_index"
        const val KEY_CORRECT_ANSWERS = "quiz_correct_answers"
        const val KEY_REMAINING_TIME = "quiz_remaining_time"
        const val KEY_ELAPSED_TIME = "quiz_elapsed_time"
        const val KEY_QUESTION_TIME = "quiz_question_time"
        const val KEY_TIMED_OUT = "quiz_timed_out"
        const val KEY_MORA_USED = "quiz_mora_used"
        const val KEY_FIFTY_FIFTY_USED = "quiz_fifty_fifty_used"
        const val KEY_HIDDEN_OPTIONS = "quiz_hidden_options"
        const val KEY_RESPONSES = "quiz_responses"
        const val RESPONSE_SEPARATOR = ";"
        const val RESPONSE_FIELD_COUNT = 6
        const val NO_SELECTED_ANSWER = -1
    }
}
