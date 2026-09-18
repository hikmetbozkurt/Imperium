package com.hikmet.imperium.ui.profile

internal enum class ProfileRank {
    NoviceChronicler,
    Archivist,
    Historian,
    ImperialScholar,
}

internal fun profileRank(totalStars: Int): ProfileRank = when (totalStars.coerceAtLeast(0)) {
    in 0..19 -> ProfileRank.NoviceChronicler
    in 20..59 -> ProfileRank.Archivist
    in 60..119 -> ProfileRank.Historian
    else -> ProfileRank.ImperialScholar
}

internal fun masteryProgress(completedLevels: Int, totalLevels: Int): Float {
    if (totalLevels <= 0) return 0f
    return completedLevels.toFloat().div(totalLevels).coerceIn(0f, 1f)
}
