package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_responses",
    indices = [
        Index("attemptId"),
        Index("questionId"),
    ],
    foreignKeys = [
        ForeignKey(
            entity = QuizAttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class QuestionResponseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val attemptId: Long,
    val questionId: String,
    val selectedAnswerIndex: Int?,
    val correctAnswerIndex: Int,
    val isCorrect: Boolean,
    val responseTimeMs: Long,
    val position: Int,
)
