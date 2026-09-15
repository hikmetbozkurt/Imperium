package com.hikmet.imperium.domain.repository

import com.hikmet.imperium.domain.model.CategoryId
import kotlinx.coroutines.flow.Flow

data class ProgressPoint(
    val epochDay: Long,
    val averageScore: Int,
    val quizCount: Int,
)

data class CategoryAnalytics(
    val categoryId: CategoryId,
    val title: String,
    val completedLevels: Int,
    val totalLevels: Int,
    val stars: Int,
    val averageScore: Int,
)

data class ProgressOverview(
    val totalQuizzes: Int,
    val averageScore: Int,
    val bestScore: Int,
    val totalStars: Int,
    val currentStreakDays: Int,
    val improvementPercent: Int,
    val timeline: List<ProgressPoint>,
    val categories: List<CategoryAnalytics>,
)

interface ProgressAnalyticsRepository {
    fun observeProgress(): Flow<ProgressOverview>
}
