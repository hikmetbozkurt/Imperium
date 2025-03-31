package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing an answer option for a question
 * Each question has multiple answer options
 */
@Entity(
    tableName = "answers",
    indices = [Index("questionId")],
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questionId: String, // The question this answer belongs to
    val text: String, // The answer text
    val isCorrect: Boolean, // Whether this is the correct answer
    val sortOrder: Int // The order in which to display this answer (0-based)
) 