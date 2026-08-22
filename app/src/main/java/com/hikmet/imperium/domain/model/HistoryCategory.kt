package com.hikmet.imperium.domain.model

/** Stable identifier shared by content, progress and navigation. */
enum class CategoryId(val value: String) {
    ANCIENT("ancient"),
    MEDIEVAL("medieval"),
    RENAISSANCE("renaissance"),
    MODERN("modern"),
    WORLD_WARS("world_wars");

    companion object {
        fun from(value: String): CategoryId? = entries.firstOrNull { it.value == value }
    }
}

data class HistoryCategory(
    val id: CategoryId,
    val title: String,
    val description: String,
    val longDescription: String,
    val iconResourceName: String,
    val gradientStartColor: Long,
    val gradientEndColor: Long,
    val levels: List<HistoryLevel>,
)
data class HistoryLevel(
    val number: Int,
    val title: String,
    val description: String,
    val iconResId: Int,
    val requiredStars: Int,
)
