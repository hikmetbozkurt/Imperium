package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a quiz question
 * Each level contains multiple questions
 */
@Entity(
    tableName = "questions",
    indices = [
        Index("categoryId"),
        Index("levelNumber")
    ]
)
data class QuestionEntity(
    @PrimaryKey
    val id: String, // Unique identifier for the question
    val categoryId: String, // Category this question belongs to
    val levelNumber: Int, // Level this question belongs to
    val text: String, // The question text
    val difficulty: Int, // 1-5 scale
    val imageResourceName: String? = null, // Optional image resource name
    val explanation: String? = null // Optional explanation of the answer
) 