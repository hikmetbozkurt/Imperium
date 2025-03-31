package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hikmet.imperium.data.entities.QuestionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Question operations
 */
@Dao
interface QuestionDao {
    /**
     * Get all questions as a Flow
     */
    @Query("SELECT * FROM questions")
    fun getAllQuestionsAsFlow(): Flow<List<QuestionEntity>>
    
    /**
     * Get a specific question by ID
     */
    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: String): QuestionEntity?
    
    /**
     * Get questions for a specific category
     */
    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    suspend fun getQuestionsForCategory(categoryId: String): List<QuestionEntity>
    
    /**
     * Get questions for a specific level
     */
    @Query("SELECT * FROM questions WHERE categoryId = :categoryId AND levelNumber = :levelNumber")
    suspend fun getQuestionsForLevel(categoryId: String, levelNumber: Int): List<QuestionEntity>
    
    /**
     * Get a random set of questions for a specific level
     */
    @Query("SELECT * FROM questions WHERE categoryId = :categoryId AND levelNumber = :levelNumber ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomQuestionsForLevel(categoryId: String, levelNumber: Int, count: Int): List<QuestionEntity>
    
    /**
     * Get count of questions for a level
     */
    @Query("SELECT COUNT(*) FROM questions WHERE categoryId = :categoryId AND levelNumber = :levelNumber")
    suspend fun getQuestionCountForLevel(categoryId: String, levelNumber: Int): Int
    
    /**
     * Insert questions
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)
    
    /**
     * Update a question
     */
    @Update
    suspend fun updateQuestion(question: QuestionEntity)
} 