package com.hikmet.imperium.data

import com.hikmet.imperium.R // Assuming R is your resource class

data class ModernHistoryLevel(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int, // You'll need to add an ic_modern.xml or similar drawable
    val requiredStars: Int = 0,
    var isUnlocked: Boolean = false // isUnlocked will be determined by user progress
)

object ModernHistoryLevels {
    val levels = listOf(
        ModernHistoryLevel(
            id = "1",
            title = "Industrial Revolution",
            description = "The dawn of machines and factories.",
            imageResId = R.drawable.ic_modern, // Placeholder, ensure ic_modern exists
            isUnlocked = true // Level 1 is typically unlocked by default
        ),
        ModernHistoryLevel(
            id = "2",
            title = "Steam Power Era",
            description = "Railways and steamships connect the world.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 2 // Example: 2 stars from Level 1 to unlock
        ),
        ModernHistoryLevel(
            id = "3",
            title = "World Wars",
            description = "Global conflicts that reshaped nations.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 4 
        ),
        ModernHistoryLevel(
            id = "4",
            title = "Cold War Begins",
            description = "Superpowers in a tense standoff.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 6
        ),
        ModernHistoryLevel(
            id = "5",
            title = "Space Race",
            description = "Humanity reaches for the stars.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 8
        ),
        ModernHistoryLevel(
            id = "6",
            title = "Digital Dawn",
            description = "The first computers and microprocessors.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 10
        ),
        ModernHistoryLevel(
            id = "7",
            title = "Globalization",
            description = "An interconnected world economy emerges.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 12
        ),
        ModernHistoryLevel(
            id = "8",
            title = "Internet Revolution",
            description = "The World Wide Web changes everything.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 15 // Difficulty jump
        ),
        ModernHistoryLevel(
            id = "9",
            title = "Climate Awareness",
            description = "Understanding our planet's challenges.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 18
        ),
        ModernHistoryLevel(
            id = "10",
            title = "War on Terror",
            description = "A new era of global security concerns.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 21
        ),
        ModernHistoryLevel(
            id = "11",
            title = "Smartphone Era",
            description = "Computing power in your pocket.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 24
        ),
        ModernHistoryLevel(
            id = "12",
            title = "Social Media Age",
            description = "Connecting billions, new challenges arise.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 27
        ),
        ModernHistoryLevel(
            id = "13",
            title = "Pandemic World",
            description = "Global health crisis and its aftermath.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 30
        ),
        ModernHistoryLevel(
            id = "14",
            title = "Green Energy Shift",
            description = "Transitioning to sustainable power.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 33
        ),
        ModernHistoryLevel(
            id = "15",
            title = "AI Revolution",
            description = "Intelligent machines transform society.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 40 // Difficulty jump
        ),
        ModernHistoryLevel(
            id = "16",
            title = "Quantum Computing",
            description = "The next frontier of computation.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 45
        ),
        ModernHistoryLevel(
            id = "17",
            title = "Mars Exploration",
            description = "Humanity's steps to other planets.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 50
        ),
        ModernHistoryLevel(
            id = "18",
            title = "Post-Capitalism Concepts",
            description = "Exploring new economic models.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 55
        ),
        ModernHistoryLevel(
            id = "19",
            title = "Transhumanism & Biohacking",
            description = "Enhancing human capabilities.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 60
        ),
        ModernHistoryLevel(
            id = "20",
            title = "Future Scenarios & Risks",
            description = "Navigating humanity's long-term path.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 65
        )
    )
} 