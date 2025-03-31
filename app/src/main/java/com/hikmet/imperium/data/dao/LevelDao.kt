package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hikmet.imperium.data.entities.LevelEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Level operations
 */
@Dao
interface LevelDao {
    /**
     * Get all levels as a Flow
     */
    @Query("SELECT * FROM levels ORDER BY categoryId, levelNumber")
    fun getAllLevels(): Flow<List<LevelEntity>>
    
    /**
     * Get all levels for a category as a Flow
     */
    @Query("SELECT * FROM levels WHERE categoryId = :categoryId ORDER BY levelNumber")
    fun getLevelsForCategoryAsFlow(categoryId: String): Flow<List<LevelEntity>>
    
    /**
     * Get all levels for a category
     */
    @Query("SELECT * FROM levels WHERE categoryId = :categoryId ORDER BY levelNumber")
    suspend fun getLevelsForCategory(categoryId: String): List<LevelEntity>
    
    /**
     * Get a specific level by category and level number
     */
    @Query("SELECT * FROM levels WHERE categoryId = :categoryId AND levelNumber = :levelNumber")
    suspend fun getLevel(categoryId: String, levelNumber: Int): LevelEntity?
    
    /**
     * Count levels in a category
     */
    @Query("SELECT COUNT(*) FROM levels WHERE categoryId = :categoryId")
    suspend fun countLevelsInCategory(categoryId: String): Int
    
    /**
     * Insert one or more levels
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<LevelEntity>)
    
    /**
     * Update a level
     */
    @Update
    suspend fun updateLevel(level: LevelEntity)
} 