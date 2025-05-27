package com.hikmet.imperium.ui.progress

import java.time.LocalDate

/**
 * Data class for progress chart entries
 */
data class ProgressEntry(
    val date: LocalDate,
    val score: Float
)

/**
 * Time view options for the progress chart
 */
enum class TimeView(val displayName: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    ALL_TIME("All Time")
}

 