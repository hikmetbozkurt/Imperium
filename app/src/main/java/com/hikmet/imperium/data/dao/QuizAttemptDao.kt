package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hikmet.imperium.data.entities.QuizAttemptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizAttemptDao {
    @Insert
    suspend fun insert(attempt: QuizAttemptEntity): Long

    @Query("SELECT * FROM quiz_attempts WHERE id = :attemptId")
    fun observeById(attemptId: Long): Flow<QuizAttemptEntity?>

    @Query("SELECT * FROM quiz_attempts ORDER BY completedAtEpochMs DESC")
    fun observeAll(): Flow<List<QuizAttemptEntity>>

    @Query(
        "SELECT * FROM quiz_attempts " +
            "WHERE categoryId = :categoryId ORDER BY completedAtEpochMs DESC",
    )
    fun observeForCategory(categoryId: String): Flow<List<QuizAttemptEntity>>

    @Query(
        "SELECT * FROM quiz_attempts WHERE categoryId = :categoryId AND levelNumber = :levelNumber " +
            "ORDER BY completedAtEpochMs DESC LIMIT 1",
    )
    suspend fun latestForLevel(categoryId: String, levelNumber: Int): QuizAttemptEntity?
}
