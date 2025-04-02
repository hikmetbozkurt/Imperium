package com.hikmet.imperium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for Level operations and tracking progress
 */
class LevelViewModel(val repository: QuizRepository) : ViewModel() {
    
    // State for total stars
    private val _totalStars = MutableStateFlow(0)
    val totalStars: StateFlow<Int> = _totalStars.asStateFlow()
    
    init {
        // Load total stars 
        viewModelScope.launch {
            _totalStars.value = repository.getTotalStars()
        }
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
        // We need to get all level progress entries and sum up their stars
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
        // First level is always unlocked
        if (levelNumber == 1) return true
        
        // For other levels, check if enough stars have been earned
        val totalStars = getCategoryStars(categoryId)
        return totalStars >= requiredStars
    }
    
    /**
     * Factory for creating ViewModel with dependencies
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