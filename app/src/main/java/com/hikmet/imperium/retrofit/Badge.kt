package com.hikmet.imperium.retrofit

import com.google.gson.annotations.SerializedName

/**
 * Response wrapper for the JSON from the API
 */
data class BadgeResponse(
    @SerializedName("badgeSystem")
    val badgeSystem: List<Badge>
)

/**
 * Data class representing a badge that can be earned by users
 * Based on the JSON structure from https://www.jsonkeeper.com/b/PPQ1
 */
data class Badge(
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("minStars")
    val minStars: Int,
    
    @SerializedName("maxStars")
    val maxStars: Int
) {
    /**
     * Check if user has earned this badge based on their star count
     */
    fun isEarned(userStars: Int): Boolean {
        return userStars >= minStars
    }
    
    /**
     * Get progress towards earning this badge (0.0 to 1.0)
     */
    fun getProgress(userStars: Int): Float {
        if (userStars >= minStars) return 1.0f
        if (minStars == 0) return 1.0f
        return (userStars.toFloat() / minStars.toFloat()).coerceIn(0.0f, 1.0f)
    }
} 