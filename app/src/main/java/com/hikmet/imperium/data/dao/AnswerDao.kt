package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hikmet.imperium.data.entities.AnswerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Answer operations
 */
@Dao
interface AnswerDao {
    /**
     * Get all answers for a question as a Flow
     */
    @Query("SELECT * FROM answers WHERE questionId = :questionId ORDER BY sortOrder")
    fun getAnswersForQuestionAsFlow(questionId: String): Flow<List<AnswerEntity>>
    
    /**
     * Get all answers for a question
     */
    @Query("SELECT * FROM answers WHERE questionId = :questionId ORDER BY sortOrder")
    suspend fun getAnswersForQuestion(questionId: String): List<AnswerEntity>
    
    /**
     * Get the correct answer for a question
     */
    @Query("SELECT * FROM answers WHERE questionId = :questionId AND isCorrect = 1 LIMIT 1")
    suspend fun getCorrectAnswerForQuestion(questionId: String): AnswerEntity?
    
    /**
     * Insert answers
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<AnswerEntity>)
    
    /**
     * Update an answer
     */
    @Update
    suspend fun updateAnswer(answer: AnswerEntity)
    
    /**
     * Delete all answers for a question
     */
    @Query("DELETE FROM answers WHERE questionId = :questionId")
    suspend fun deleteAnswersForQuestion(questionId: String)
    
    /**
     * Get answer count for a question
     */
    @Query("SELECT COUNT(*) FROM answers WHERE questionId = :questionId")
    suspend fun getAnswerCountForQuestion(questionId: String): Int
} 