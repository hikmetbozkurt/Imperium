package com.hikmet.imperium.ui.util

/**
 * Calculate stars based on score percentage
 */
fun calculateStars(score: Int): Int {
    return when {
        score >= 80 -> 3 // 3 stars for 80+ points
        score >= 60 -> 2 // 2 stars for 60-79 points
        score >= 40 -> 1 // 1 star for 40-59 points
        else -> 0 // 0 stars for less than 40 points
    }
} 