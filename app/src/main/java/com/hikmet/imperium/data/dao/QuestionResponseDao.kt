package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hikmet.imperium.data.entities.QuestionResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionResponseDao {
    @Insert
    suspend fun insertAll(responses: List<QuestionResponseEntity>)

    @Query(
        "SELECT question_responses.* FROM question_responses " +
            "INNER JOIN quiz_attempts ON quiz_attempts.id = question_responses.attemptId " +
            "WHERE quiz_attempts.categoryId = :categoryId " +
            "AND quiz_attempts.levelNumber = :levelNumber " +
            "ORDER BY quiz_attempts.completedAtEpochMs DESC, question_responses.position ASC " +
            "LIMIT :limit",
    )
    suspend fun recentForLevel(
        categoryId: String,
        levelNumber: Int,
        limit: Int,
    ): List<QuestionResponseEntity>

    @Query("SELECT * FROM question_responses WHERE attemptId = :attemptId ORDER BY position")
    fun observeForAttempt(attemptId: Long): Flow<List<QuestionResponseEntity>>
}
