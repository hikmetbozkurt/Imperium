package com.hikmet.imperium.data.repository

import android.util.Log
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.AnswerEntity
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.QuestionEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for accessing quiz data
 * Provides a clean API for the rest of the app
 */
@Singleton
class QuizRepository @Inject constructor(
    private val database: ImperiumDatabase
) {
    
    // Categories
    val allCategories: Flow<List<CategoryEntity>> = database.categoryDao().getAllCategories()
    
    suspend fun getCategoryById(categoryId: String): CategoryEntity? {
        return database.categoryDao().getCategoryById(categoryId)
    }
    
    suspend fun insertCategories(categories: List<CategoryEntity>) {
        database.categoryDao().insertCategories(categories)
    }
    
    // User Progress
    fun getUserProgressForCategory(categoryId: String): Flow<UserProgressEntity?> {
        return database.userProgressDao().getProgressForCategoryAsFlow(categoryId)
    }
    
    suspend fun updateUserProgress(progress: UserProgressEntity) {
        database.userProgressDao().insertOrUpdateProgress(progress)
    }
    
    suspend fun unlockLevel(categoryId: String, levelNumber: Int) {
        Log.d("UNLOCK_DEBUG", "Starting unlockLevel for $categoryId level $levelNumber")
        try {
            val currentProgress = database.userProgressDao().getProgressForCategory(categoryId)
            Log.d("UNLOCK_DEBUG", "Current progress: $currentProgress")
            if (currentProgress == null) {
                Log.d("UNLOCK_DEBUG", "No progress found, creating new entry with level $levelNumber unlocked")
                val newProgress = UserProgressEntity(
                    categoryId = categoryId,
                    unlockedLevels = levelNumber,
                    totalStarsEarned = 0,
                    highestLevelCompleted = 0,
                    lastPlayedTimestamp = System.currentTimeMillis(),
                    levelStars = emptyMap()
                )
                database.userProgressDao().insertOrUpdateProgress(newProgress)
                Log.d("UNLOCK_DEBUG", "Created new progress: $newProgress")
            } else if (levelNumber > currentProgress.unlockedLevels) {
                Log.d("UNLOCK_DEBUG", "Updating existing progress to unlock level $levelNumber")
                val updatedProgress = currentProgress.copy(
                    unlockedLevels = levelNumber,
                    lastPlayedTimestamp = System.currentTimeMillis()
                )
                database.userProgressDao().insertOrUpdateProgress(updatedProgress)
                Log.d("UNLOCK_DEBUG", "Updated progress: $updatedProgress")
            } else {
                Log.d("UNLOCK_DEBUG", "Level $levelNumber is already unlocked (current: ${currentProgress.unlockedLevels})")
            }
            val finalProgress = database.userProgressDao().getProgressForCategory(categoryId)
            Log.d("UNLOCK_DEBUG", "Final progress after unlock: $finalProgress")
        } catch (e: Exception) {
            Log.e("UNLOCK_DEBUG", "Error unlocking level: ${e.message}", e)
        }
    }
    
    suspend fun getTotalStars(): Int {
        return database.userProgressDao().getTotalStarsEarned() ?: 0
    }
    
    // Levels
    suspend fun getLevelsByCategory(categoryId: String): List<LevelEntity> {
        return database.levelDao().getLevelsForCategory(categoryId)
    }

    fun getLevelsForCategory(categoryId: String): Flow<List<LevelEntity>> {
        return database.levelDao().getLevelsForCategoryAsFlow(categoryId)
    }
    
    suspend fun insertLevels(levels: List<LevelEntity>) {
        database.levelDao().insertLevels(levels)
    }
    
    // Level Progress
    fun getLevelProgressForCategory(categoryId: String): Flow<List<LevelProgressEntity>> {
        return database.levelProgressDao().getLevelProgressForCategoryAsFlow(categoryId)
    }
    
    suspend fun updateLevelProgress(categoryId: String, levelNumber: Int, score: Int, stars: Int, timeMs: Long? = null): Boolean {
        if (levelNumber == 1 && stars > 0) {
            unlockLevel(categoryId, 2)
        }
        val userProgress = database.userProgressDao().getProgressForCategory(categoryId)
        userProgress?.let {
            val updatedStarsMap = it.levelStars.toMutableMap()
            updatedStarsMap[levelNumber] = stars
            updateUserProgress(it.copy(levelStars = updatedStarsMap, totalStarsEarned = it.totalStarsEarned + stars))
        }

        return database.levelProgressDao().updateStarsForLevel(categoryId, levelNumber, stars, score, timeMs)
    }
    
    // Questions
    suspend fun getQuestionsForLevel(categoryId: String, levelNumber: Int): List<QuestionEntity> {
        return database.questionDao().getQuestionsForLevel(categoryId, levelNumber)
    }
    
    suspend fun getRandomQuestionsForLevel(categoryId: String, levelNumber: Int, count: Int = 4): List<QuestionEntity> {
        return database.questionDao().getRandomQuestionsForLevel(categoryId, levelNumber, count)
    }
    
    suspend fun insertQuestions(questions: List<QuestionEntity>) {
        database.questionDao().insertQuestions(questions)
    }
    
    // Answers
    suspend fun getAnswersForQuestion(questionId: String): List<AnswerEntity> {
        return database.answerDao().getAnswersForQuestion(questionId)
    }
    
    suspend fun insertAnswers(answers: List<AnswerEntity>) {
        database.answerDao().insertAnswers(answers)
    }
    
    suspend fun getQuizForLevel(categoryId: String, levelNumber: Int, questionCount: Int = 4): Map<QuestionEntity, List<AnswerEntity>> {
        val questions = getRandomQuestionsForLevel(categoryId, levelNumber, questionCount)
        val quizMap = mutableMapOf<QuestionEntity, List<AnswerEntity>>()
        for (question in questions) {
            val answers = getAnswersForQuestion(question.id)
            quizMap[question] = answers
        }
        return quizMap
    }
    
    fun calculateStars(scorePercentage: Float): Int {
        return when {
            scorePercentage >= 0.9f -> 3
            scorePercentage >= 0.7f -> 2
            scorePercentage >= 0.5f -> 1
            else -> 0
        }
    }
    
    suspend fun isLevelUnlocked(categoryId: String, levelNumber: Int): Boolean {
        val progress = database.userProgressDao().getProgressForCategory(categoryId)
        return progress != null && levelNumber <= progress.unlockedLevels
    }
    
    suspend fun getCompletedLevelCount(categoryId: String): Int {
        return database.levelProgressDao().getCompletedLevelCount(categoryId)
    }
} 