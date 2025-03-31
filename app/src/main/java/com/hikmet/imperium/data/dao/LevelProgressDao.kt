package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hikmet.imperium.data.entities.LevelProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Level Progress operations
 */
@Dao
interface LevelProgressDao {
    /**
     * Get level progress for all levels in a category as a Flow
     */
    @Query("SELECT * FROM level_progress WHERE categoryId = :categoryId ORDER BY levelNumber")
    fun getLevelProgressForCategoryAsFlow(categoryId: String): Flow<List<LevelProgressEntity>>
    
    /**
     * Get level progress for all levels in a category
     */
    @Query("SELECT * FROM level_progress WHERE categoryId = :categoryId ORDER BY levelNumber")
    suspend fun getLevelProgressForCategory(categoryId: String): List<LevelProgressEntity>
    
    /**
     * Get level progress for a specific level
     */
    @Query("SELECT * FROM level_progress WHERE categoryId = :categoryId AND levelNumber = :levelNumber")
    suspend fun getLevelProgress(categoryId: String, levelNumber: Int): LevelProgressEntity?
    
    /**
     * Get level progress for a specific level as a Flow
     */
    @Query("SELECT * FROM level_progress WHERE categoryId = :categoryId AND levelNumber = :levelNumber")
    fun getLevelProgressAsFlow(categoryId: String, levelNumber: Int): Flow<LevelProgressEntity?>
    
    /**
     * Insert or update level progress
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLevelProgress(levelProgress: LevelProgressEntity)
    
    /**
     * Insert multiple level progress entries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevelProgress(levelProgress: List<LevelProgressEntity>)
    
    /**
     * Update level progress
     */
    @Update
    suspend fun updateLevelProgress(levelProgress: LevelProgressEntity)
    
    /**
     * Get total stars earned in a category
     */
    @Query("SELECT SUM(starsEarned) FROM level_progress WHERE categoryId = :categoryId")
    suspend fun getTotalStarsForCategory(categoryId: String): Int?
    
    /**
     * Get the count of completed levels in a category
     */
    @Query("SELECT COUNT(*) FROM level_progress WHERE categoryId = :categoryId AND completed = 1")
    suspend fun getCompletedLevelCount(categoryId: String): Int
    
    /**
     * Get the highest completed level number in a category
     */
    @Query("SELECT MAX(levelNumber) FROM level_progress WHERE categoryId = :categoryId AND completed = 1")
    suspend fun getHighestCompletedLevel(categoryId: String): Int?
    
    /**
     * Update the stars earned for a level and update the user's progress
     * Returns true if this is a new record (more stars than before)
     */
    @Transaction
    suspend fun updateStarsForLevel(
        categoryId: String, 
        levelNumber: Int, 
        starsEarned: Int, 
        score: Int, 
        timeMs: Long?
    ): Boolean {
        val existingProgress = getLevelProgress(categoryId, levelNumber)
        
        val isNewRecord = existingProgress == null || starsEarned > existingProgress.starsEarned || 
                          (starsEarned == existingProgress.starsEarned && score > existingProgress.highestScore)
        
        val updatedProgress = existingProgress?.copy(
            starsEarned = maxOf(existingProgress.starsEarned, starsEarned),
            highestScore = maxOf(existingProgress.highestScore, score),
            completed = true,
            bestTimeMs = if (timeMs != null && (existingProgress.bestTimeMs == null || timeMs < existingProgress.bestTimeMs)) 
                            timeMs else existingProgress.bestTimeMs,
            attemptCount = existingProgress.attemptCount + 1,
            lastPlayedTimestamp = System.currentTimeMillis()
        ) ?: LevelProgressEntity(
            categoryId = categoryId,
            levelNumber = levelNumber,
            starsEarned = starsEarned,
            highestScore = score,
            completed = true,
            bestTimeMs = timeMs,
            attemptCount = 1,
            lastPlayedTimestamp = System.currentTimeMillis()
        )
        
        insertOrUpdateLevelProgress(updatedProgress)
        return isNewRecord
    }
} 