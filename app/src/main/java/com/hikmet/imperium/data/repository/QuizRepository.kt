package com.hikmet.imperium.data.repository

import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.data.entities.AnswerEntity
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.QuestionEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for accessing quiz data
 * Provides a clean API for the rest of the app
 */
class QuizRepository(private val database: ImperiumDatabase) {
    
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
        database.userProgressDao().unlockLevel(categoryId, levelNumber)
    }
    
    suspend fun getTotalStars(): Int {
        return database.userProgressDao().getTotalStarsEarned() ?: 0
    }
    
    // Levels
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
    
    suspend fun updateLevelProgress(categoryId: String, levelNumber: Int, score: Int, stars: Int, timeMs: Long?): Boolean {
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
    
    /**
     * Get a complete quiz with questions and answers for a specific level
     */
    suspend fun getQuizForLevel(categoryId: String, levelNumber: Int, questionCount: Int = 4): Map<QuestionEntity, List<AnswerEntity>> {
        val questions = getRandomQuestionsForLevel(categoryId, levelNumber, questionCount)
        val quizMap = mutableMapOf<QuestionEntity, List<AnswerEntity>>()
        
        for (question in questions) {
            val answers = getAnswersForQuestion(question.id)
            quizMap[question] = answers
        }
        
        return quizMap
    }
    
    /**
     * Calculate stars based on score percentage
     */
    fun calculateStars(scorePercentage: Float): Int {
        return when {
            scorePercentage >= 0.9f -> 3 // 90% or higher = 3 stars
            scorePercentage >= 0.7f -> 2 // 70% or higher = 2 stars
            scorePercentage >= 0.5f -> 1 // 50% or higher = 1 star
            else -> 0 // Less than 50% = 0 stars
        }
    }
    
    /**
     * Check if a level is unlocked for a user
     */
    suspend fun isLevelUnlocked(categoryId: String, levelNumber: Int): Boolean {
        val progress = database.userProgressDao().getProgressForCategory(categoryId)
        return progress != null && levelNumber <= progress.unlockedLevels
    }
    
    /**
     * Get completed level count for a category
     */
    suspend fun getCompletedLevelCount(categoryId: String): Int {
        return database.levelProgressDao().getCompletedLevelCount(categoryId)
    }
} 