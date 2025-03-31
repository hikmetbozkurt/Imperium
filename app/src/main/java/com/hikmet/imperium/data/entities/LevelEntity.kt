package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a level within a category
 * Each category has multiple levels (e.g., Ancient Civilizations Level 1, 2, etc.)
 */
@Entity(
    tableName = "levels",
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
data class LevelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: String,
    val levelNumber: Int,
    val title: String,
    val description: String,
    val difficulty: Int, // 1-5 scale of difficulty
    val requiredStarsToUnlock: Int = 0 // Stars required to unlock this level
) 