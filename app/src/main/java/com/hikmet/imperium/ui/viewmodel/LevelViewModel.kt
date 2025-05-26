package com.hikmet.imperium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel // Import Hilt annotation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject // Import Inject annotation

// Define a UI state data class for LevelScreen
data class LevelsUiState(
    val levels: List<LevelEntity> = emptyList(),
    val userProgress: UserProgressEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel for Level operations and tracking progress, now Hilt-injected.
 */
@HiltViewModel // Add Hilt annotation
class LevelViewModel @Inject constructor( // Add Inject annotation to constructor
    val repository: QuizRepository
) : ViewModel() {

    private val _levelsUiState = MutableStateFlow(LevelsUiState(isLoading = true))
    val levelsUiState: StateFlow<LevelsUiState> = _levelsUiState.asStateFlow()

    // State for total stars (can be part of LevelsUiState or separate)
    private val _totalStars = MutableStateFlow(0)
    val totalStars: StateFlow<Int> = _totalStars.asStateFlow()

    init {
        viewModelScope.launch {
            _totalStars.value = repository.getTotalStars()
            // Initial load or specific category load can be triggered here or by the UI
        }
    }

    fun loadLevelsForCategory(categoryId: String) {
        viewModelScope.launch {
            _levelsUiState.value = LevelsUiState(isLoading = true)
            try {
                // Assuming repository has a method to get LevelEntity list by categoryId
                val levels = repository.getLevelsByCategory(categoryId)
                // Use firstOrNull from kotlinx.coroutines.flow
                val userProgress = repository.getUserProgressForCategory(categoryId).firstOrNull()
                val actualUserProgress = userProgress ?: UserProgressEntity(
                    categoryId = categoryId,
                    unlockedLevels = 1, // Default: level 1 is unlocked
                    totalStarsEarned = 0,
                    highestLevelCompleted = 0,
                    lastPlayedTimestamp = System.currentTimeMillis(), // Add timestamp
                    levelStars = emptyMap()
                )

                // If userProgress was null, and we created a default, persist it.
                if (userProgress == null) {
                    repository.updateUserProgress(actualUserProgress)
                }


                _levelsUiState.value = LevelsUiState(
                    levels = levels,
                    userProgress = actualUserProgress,
                    isLoading = false
                )
            } catch (e: Exception) {
                _levelsUiState.value = LevelsUiState(isLoading = false, error = "Failed to load levels: ${e.message}")
            }
        }
    }

    fun getStarsForLevel(userProgress: UserProgressEntity, categoryId: String, levelNumber: Int): Int {
        // This logic might be better inside UserProgressEntity or a utility function
        // For now, assuming userProgress might contain a map or list of level stars.
        // This is a placeholder, actual implementation depends on UserProgressEntity structure
        return userProgress.levelStars[levelNumber] ?: 0 // Assuming levelStars is Map<Int, Int>
    }

    /**
     * Get level progress for a category
     */
    fun getLevelProgressForCategory(categoryId: String): Flow<List<LevelProgressEntity>> {
        return repository.getLevelProgressForCategory(categoryId)
    }
    
    /**
     * Get total stars earned across all categories
     */
    suspend fun getTotalStars(): Int {
        return repository.getTotalStars()
    }
    
    /**
     * Get category-specific stars
     */
    suspend fun getCategoryStars(categoryId: String): Int {
        val progressList = repository.getLevelProgressForCategory(categoryId).first()
        return progressList.sumOf { it.starsEarned }
    }
    
    /**
     * Update level progress with new stars earned
     */
    suspend fun updateLevelProgress(
        categoryId: String,
        levelNumber: Int,
        score: Int,
        stars: Int,
        timeMs: Long? = null
    ): Boolean {
        return repository.updateLevelProgress(categoryId, levelNumber, score, stars, timeMs)
    }
    
    /**
     * Get level progress for a specific level
     */
    suspend fun getLevelProgress(categoryId: String, levelNumber: Int): LevelProgressEntity? {
        val progressList = repository.getLevelProgressForCategory(categoryId).first()
        return progressList.find { it.categoryId == categoryId && it.levelNumber == levelNumber }
    }
    
    /**
     * Check if a level is unlocked based on stars earned
     */
    suspend fun isLevelUnlocked(categoryId: String, levelNumber: Int, requiredStars: Int): Boolean {
        if (levelNumber == 1) return true
        val totalStars = getCategoryStars(categoryId)
        return totalStars >= requiredStars
    }
    
    /**
     * Factory for creating LevelViewModel instances
     * This provides backwards compatibility for screens not yet using Hilt
     */
    class Factory(private val repository: QuizRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LevelViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LevelViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 