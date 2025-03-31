package com.hikmet.imperium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.data.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for category and level management
 */
class CategoryViewModel(private val repository: QuizRepository) : ViewModel() {
    
    // State for categories
    private val _categoriesState = MutableStateFlow<DataState<List<CategoryEntity>>>(DataState.Loading)
    val categoriesState: StateFlow<DataState<List<CategoryEntity>>> = _categoriesState.asStateFlow()
    
    // Selected category
    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> = _selectedCategory.asStateFlow()
    
    // State for levels in the selected category
    private val _levelsState = MutableStateFlow<DataState<List<LevelWithProgress>>>(DataState.Loading)
    val levelsState: StateFlow<DataState<List<LevelWithProgress>>> = _levelsState.asStateFlow()
    
    // Total stars earned
    private val _totalStars = MutableStateFlow(0)
    val totalStars: StateFlow<Int> = _totalStars.asStateFlow()
    
    init {
        // Load categories on initialization
        loadCategories()
        
        // Load total stars
        viewModelScope.launch {
            _totalStars.value = repository.getTotalStars()
        }
    }
    
    /**
     * Load all categories
     */
    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = DataState.Loading
            try {
                repository.allCategories.collect { categories ->
                    _categoriesState.value = if (categories.isNotEmpty()) {
                        DataState.Success(categories)
                    } else {
                        DataState.Empty
                    }
                }
            } catch (e: Exception) {
                _categoriesState.value = DataState.Error("Failed to load categories: ${e.message}")
            }
        }
    }
    
    /**
     * Select a category and load its levels
     */
    fun selectCategory(categoryId: String) {
        viewModelScope.launch {
            _levelsState.value = DataState.Loading
            
            try {
                // Get the category
                val category = repository.getCategoryById(categoryId)
                if (category == null) {
                    _levelsState.value = DataState.Error("Category not found")
                    return@launch
                }
                
                _selectedCategory.value = category
                
                // Combine levels with progress data
                val levelsFlow = repository.getLevelsForCategory(categoryId)
                val progressFlow = repository.getLevelProgressForCategory(categoryId)
                val userProgressFlow = repository.getUserProgressForCategory(categoryId)
                
                combine(
                    levelsFlow,
                    progressFlow,
                    userProgressFlow
                ) { levels, progress, userProgress ->
                    // Map levels to LevelWithProgress objects
                    levels.map { level ->
                        // Find progress for this level
                        val levelProgress = progress.find { 
                            it.categoryId == level.categoryId && it.levelNumber == level.levelNumber 
                        }
                        
                        // Check if level is unlocked
                        val isUnlocked = userProgress?.unlockedLevels != null && 
                                         level.levelNumber <= userProgress.unlockedLevels
                        
                        LevelWithProgress(
                            level = level,
                            progress = levelProgress,
                            isUnlocked = isUnlocked,
                            starsRequired = level.requiredStarsToUnlock,
                            isCompleted = levelProgress?.completed ?: false,
                            highestScore = levelProgress?.highestScore ?: 0,
                            starsEarned = levelProgress?.starsEarned ?: 0,
                            bestTimeMs = levelProgress?.bestTimeMs
                        )
                    }
                }.collect { levelsWithProgress ->
                    _levelsState.value = if (levelsWithProgress.isNotEmpty()) {
                        DataState.Success(levelsWithProgress)
                    } else {
                        DataState.Empty
                    }
                }
                
            } catch (e: Exception) {
                _levelsState.value = DataState.Error("Failed to load levels: ${e.message}")
            }
        }
    }
    
    /**
     * Get the user's progress for a category
     */
    fun getUserProgressForCategory(categoryId: String): Flow<UserProgressEntity?> {
        return repository.getUserProgressForCategory(categoryId)
    }
    
    /**
     * Get completion percentage for a category
     */
    suspend fun getCategoryCompletionPercentage(categoryId: String): Float {
        val category = repository.getCategoryById(categoryId) ?: return 0f
        val completedLevels = repository.getCompletedLevelCount(categoryId)
        return completedLevels.toFloat() / category.totalLevels.toFloat()
    }
    
    /**
     * Factory for creating ViewModel with dependencies
     */
    class Factory(private val repository: QuizRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/**
 * Represents a level with its progress information
 */
data class LevelWithProgress(
    val level: LevelEntity,
    val progress: LevelProgressEntity?,
    val isUnlocked: Boolean,
    val starsRequired: Int,
    val isCompleted: Boolean,
    val highestScore: Int,
    val starsEarned: Int,
    val bestTimeMs: Long?
)

/**
 * Represents the state of data loading
 */
sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    object Empty : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
} 