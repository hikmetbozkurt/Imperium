package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a user's progress in a category
 * Tracks unlocked levels, stars earned, etc.
 */
@Entity(
    tableName = "user_progress",
    indices = [Index("categoryId")],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserProgressEntity(
    @PrimaryKey
    val categoryId: String,
    val unlockedLevels: Int = 1,  // Default is 1 (first level is unlocked)
    val totalStarsEarned: Int = 0,
    val highestLevelCompleted: Int = 0,
    val lastPlayedTimestamp: Long = System.currentTimeMillis(),
    val levelStars: Map<Int, Int> = emptyMap() // Stores stars earned for each level (levelNumber -> stars)
) 