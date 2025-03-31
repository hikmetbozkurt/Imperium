package com.hikmet.imperium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.data.entities.AnswerEntity
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.QuestionEntity
import com.hikmet.imperium.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for the quiz functionality
 */
class QuizViewModel(private val repository: QuizRepository) : ViewModel() {
    
    // Quiz state
    private val _quizState = MutableStateFlow<QuizState>(QuizState.Loading)
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()
    
    // Current quiz data
    private var currentCategory: CategoryEntity? = null
    private var currentLevelNumber: Int = 1
    private var quizQuestions: List<QuestionEntity> = emptyList()
    private var quizAnswers: Map<String, List<AnswerEntity>> = emptyMap()
    
    // Quiz progress
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()
    
    private val _selectedAnswerId = MutableStateFlow<Int?>(null)
    val selectedAnswerId: StateFlow<Int?> = _selectedAnswerId.asStateFlow()
    
    private val _quizProgress = MutableStateFlow(QuizProgress())
    val quizProgress: StateFlow<QuizProgress> = _quizProgress.asStateFlow()
    
    private val _quizResults = MutableStateFlow<QuizResults?>(null)
    val quizResults: StateFlow<QuizResults?> = _quizResults.asStateFlow()
    
    // Timer state
    private val _timerState = MutableStateFlow(TimerState(isRunning = false, remainingTimeMs = DEFAULT_QUIZ_DURATION_MS))
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()
    
    private var quizStartTimeMs: Long = 0
    
    /**
     * Load a quiz for a specific category and level
     */
    fun loadQuiz(categoryId: String, levelNumber: Int) {
        viewModelScope.launch {
            try {
                _quizState.value = QuizState.Loading
                
                // Get the category
                currentCategory = repository.getCategoryById(categoryId)
                currentLevelNumber = levelNumber
                
                // Get quiz questions
                quizQuestions = repository.getRandomQuestionsForLevel(categoryId, levelNumber, QUESTIONS_PER_QUIZ)
                
                if (quizQuestions.isEmpty()) {
                    _quizState.value = QuizState.Error("No questions found for this level")
                    return@launch
                }
                
                // Create a mutable map to store question answers
                val answerMap = mutableMapOf<String, List<AnswerEntity>>()
                
                // Get answers for each question and randomize their order
                quizQuestions.forEach { question ->
                    val answers = repository.getAnswersForQuestion(question.id)
                    
                    // Randomize the order of answers
                    val shuffledAnswers = answers.shuffled()
                    
                    // Store in the map
                    answerMap[question.id] = shuffledAnswers
                }
                
                // Assign to the class property
                quizAnswers = answerMap
                
                // Reset quiz progress
                _currentQuestionIndex.value = 0
                _selectedAnswerId.value = null
                _quizProgress.value = QuizProgress()
                
                // Set quiz ready state
                _quizState.value = QuizState.Ready
                
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to load quiz: ${e.message}")
            }
        }
    }
    
    /**
     * Start the quiz
     */
    fun startQuiz() {
        quizStartTimeMs = System.currentTimeMillis()
        _timerState.value = TimerState(isRunning = true, remainingTimeMs = DEFAULT_QUIZ_DURATION_MS)
        _quizState.value = QuizState.Active
    }
    
    /**
     * Get the current question
     */
    fun getCurrentQuestion(): QuestionEntity? {
        return quizQuestions.getOrNull(_currentQuestionIndex.value)
    }
    
    /**
     * Get answers for the current question
     */
    fun getCurrentAnswers(): List<AnswerEntity> {
        val currentQuestion = getCurrentQuestion() ?: return emptyList()
        return quizAnswers[currentQuestion.id] ?: emptyList()
    }
    
    /**
     * Select an answer
     */
    fun selectAnswer(answerId: Int) {
        if (_quizState.value != QuizState.Active) return
        
        _selectedAnswerId.value = answerId
        
        // Check if answer is correct
        val currentQuestion = getCurrentQuestion() ?: return
        val selectedAnswer = getCurrentAnswers().find { it.id == answerId } ?: return
        
        // Update progress
        _quizProgress.value = _quizProgress.value.copy(
            questionsAnswered = _quizProgress.value.questionsAnswered + 1,
            correctAnswers = _quizProgress.value.correctAnswers + (if (selectedAnswer.isCorrect) 1 else 0)
        )
    }
    
    /**
     * Move to the next question
     */
    fun nextQuestion() {
        _selectedAnswerId.value = null
        
        if (_currentQuestionIndex.value < quizQuestions.size - 1) {
            _currentQuestionIndex.value += 1
        } else {
            // Quiz completed
            completeQuiz()
        }
    }
    
    /**
     * Complete the quiz and calculate results
     */
    private fun completeQuiz() {
        val elapsedTimeMs = System.currentTimeMillis() - quizStartTimeMs
        val score = (_quizProgress.value.correctAnswers * 100) / QUESTIONS_PER_QUIZ
        val stars = repository.calculateStars(score / 100f)
        
        _quizResults.value = QuizResults(
            score = score,
            stars = stars,
            correctAnswers = _quizProgress.value.correctAnswers,
            totalQuestions = QUESTIONS_PER_QUIZ,
            timeMs = elapsedTimeMs
        )
        
        _timerState.value = _timerState.value.copy(isRunning = false)
        _quizState.value = QuizState.Completed
        
        // Save results to database
        saveQuizResults()
    }
    
    /**
     * Save quiz results to database
     */
    private fun saveQuizResults() {
        val results = _quizResults.value ?: return
        val category = currentCategory ?: return
        
        viewModelScope.launch {
            try {
                // Update level progress
                repository.updateLevelProgress(
                    categoryId = category.id,
                    levelNumber = currentLevelNumber,
                    score = results.score,
                    stars = results.stars,
                    timeMs = results.timeMs
                )
                
                // Unlock next level if enough stars were earned
                if (results.stars > 0 && currentLevelNumber < category.totalLevels) {
                    repository.unlockLevel(category.id, currentLevelNumber + 1)
                }
            } catch (e: Exception) {
                // Handle error, but don't disrupt user experience
            }
        }
    }
    
    /**
     * Update timer
     */
    fun updateTimer(remainingTimeMs: Long) {
        _timerState.value = _timerState.value.copy(remainingTimeMs = remainingTimeMs)
        
        // Note: Complete quiz handling is now moved to the UI layer
        // to show the time expiration alert
    }
    
    // Factory for creating ViewModel with dependencies
    class Factory(private val repository: QuizRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QuizViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    
    companion object {
        const val QUESTIONS_PER_QUIZ = 4
        const val DEFAULT_QUIZ_DURATION_MS = 40000L // 40 seconds total per level
    }
}

/**
 * Represents the state of the quiz
 */
sealed class QuizState {
    object Loading : QuizState()
    object Ready : QuizState()
    object Active : QuizState()
    object Completed : QuizState()
    data class Error(val message: String) : QuizState()
}

/**
 * Represents the progress of the current quiz
 */
data class QuizProgress(
    val questionsAnswered: Int = 0,
    val correctAnswers: Int = 0
)

/**
 * Represents the results of a completed quiz
 */
data class QuizResults(
    val score: Int,
    val stars: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeMs: Long
)

/**
 * Represents the state of the quiz timer
 */
data class TimerState(
    val isRunning: Boolean,
    val remainingTimeMs: Long
) 