package com.hikmet.imperium.ui.home

/**
 * Chooses the most advanced real category for the dashboard hero while keeping
 * repository order stable when progress is tied.
 */
internal fun selectCurrentExpedition(categories: List<HomeCategory>): HomeCategory? =
    categories.withIndex()
        .maxWithOrNull(
            compareBy<IndexedValue<HomeCategory>> { it.value.progress.unlockedLevels }
                .thenByDescending { it.index },
        )
        ?.value

internal data class ExpeditionSummary(
    val categoryCount: Int,
    val levelCount: Int,
    val totalStars: Int,
)

internal fun summarizeExpeditions(categories: List<HomeCategory>): ExpeditionSummary =
    ExpeditionSummary(
        categoryCount = categories.size,
        levelCount = categories.sumOf { it.content.levels.size },
        totalStars = categories.sumOf { it.progress.totalStars },
    )
