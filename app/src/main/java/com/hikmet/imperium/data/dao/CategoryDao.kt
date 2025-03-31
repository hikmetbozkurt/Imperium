package com.hikmet.imperium.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hikmet.imperium.data.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Category operations
 */
@Dao
interface CategoryDao {
    /**
     * Get all categories as a Flow
     */
    @Query("SELECT * FROM categories ORDER BY id ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    
    /**
     * Get a specific category by ID
     */
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?
    
    /**
     * Get category count
     */
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoryCount(): Int
    
    /**
     * Insert one or more categories
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)
    
    /**
     * Update a category
     */
    @Update
    suspend fun updateCategory(category: CategoryEntity)
} 