package com.hikmet.imperium.domain.repository

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.QuizAttempt
import com.hikmet.imperium.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

data class RecordedAttempt(
    val id: Long,
    val result: QuizResult,
    val isNewBest: Boolean,
    val addedStars: Int,
    val unlockedLevels: Int,
    val newlyUnlockedLevel: Int? = null,
)

data class CategoryProgress(
    val categoryId: CategoryId,
    val unlockedLevels: Int,
    val totalStars: Int,
    val levelStars: Map<Int, Int>,
)

interface GameProgressRepository {
    suspend fun recordAttempt(attempt: QuizAttempt): RecordedAttempt
    fun observeResult(attemptId: Long): Flow<QuizResult?>
    fun observeRecordedAttempt(attemptId: Long): Flow<RecordedAttempt?>
    fun observeCategory(categoryId: CategoryId): Flow<CategoryProgress>
    suspend fun recentQuestionIds(
        categoryId: CategoryId,
        levelNumber: Int,
        limit: Int = 24,
    ): Set<String>
}
