package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a user's progress in a specific level
 * Tracks stars earned, completion status, high score, etc.
 */
@Entity(
    tableName = "level_progress",
    indices = [
        Index("categoryId"), 
        Index("levelNumber")
    ],
    primaryKeys = ["categoryId", "levelNumber"]
)
data class LevelProgressEntity(
    val categoryId: String,
    val levelNumber: Int,
    val starsEarned: Int = 0,
    val highestScore: Int = 0,
    val completed: Boolean = false,
    val bestTimeMs: Long? = null, // Best completion time in milliseconds (null if not completed)
    val attemptCount: Int = 0,
    val lastPlayedTimestamp: Long = 0
) 