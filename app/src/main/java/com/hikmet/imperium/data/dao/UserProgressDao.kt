package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hikmet.imperium.data.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User Progress operations
 */
@Dao
interface UserProgressDao {
    /**
     * Get the user's progress for all categories as a Flow
     */
    @Query("SELECT * FROM user_progress")
    fun getAllProgressAsFlow(): Flow<List<UserProgressEntity>>
    
    /**
     * Get the user's progress for all categories
     */
    @Query("SELECT * FROM user_progress")
    suspend fun getAllProgress(): List<UserProgressEntity>
    
    /**
     * Get the user's progress for a specific category as a Flow
     */
    @Query("SELECT * FROM user_progress WHERE categoryId = :categoryId")
    fun getProgressForCategoryAsFlow(categoryId: String): Flow<UserProgressEntity?>
    
    /**
     * Get the user's progress for a specific category
     */
    @Query("SELECT * FROM user_progress WHERE categoryId = :categoryId")
    suspend fun getProgressForCategory(categoryId: String): UserProgressEntity?
    
    /**
     * Insert or update user progress
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserProgressEntity)
    
    /**
     * Update user progress
     */
    @Update
    suspend fun updateProgress(progress: UserProgressEntity)
    
    /**
     * Get total stars earned across all categories
     */
    @Query("SELECT SUM(totalStarsEarned) FROM user_progress")
    suspend fun getTotalStarsEarned(): Int?
    
    /**
     * Unlock a level for a category
     */
    @Transaction
    suspend fun unlockLevel(categoryId: String, newLevel: Int) {
        val progress = getProgressForCategory(categoryId)
        if (progress != null && newLevel > progress.unlockedLevels) {
            updateProgress(progress.copy(unlockedLevels = newLevel))
        } else if (progress == null) {
            // Create new progress entry with the specified level unlocked
            insertOrUpdateProgress(
                UserProgressEntity(
                    categoryId = categoryId,
                    unlockedLevels = newLevel,
                    totalStarsEarned = 0,
                    highestLevelCompleted = 0,
                    lastPlayedTimestamp = System.currentTimeMillis()
                )
            )
        }
    }
} 