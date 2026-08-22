package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Immutable history record. Aggregated progress is derived from these attempts. */
@Entity(
    tableName = "quiz_attempts",
    indices = [
        Index("categoryId"),
        Index(value = ["categoryId", "levelNumber"]),
        Index("completedAtEpochMs"),
    ],
)
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: String,
    val levelNumber: Int,
    val score: Int,
    val stars: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val durationMs: Long,
    val completedAtEpochMs: Long,
)
