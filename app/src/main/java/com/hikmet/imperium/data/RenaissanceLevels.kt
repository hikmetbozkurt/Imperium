package com.hikmet.imperium.data

import com.hikmet.imperium.R

// Data class for Renaissance levels

data class RenaissanceLevel(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val requiredStars: Int = 0,
    val isUnlocked: Boolean = true
)

object RenaissanceLevels {
    val levels = listOf(
        RenaissanceLevel(
            id = "1",
            title = "Dawn of the Renaissance",
            description = "Explore the origins of the Renaissance in Italy and the revival of classical learning.",
            imageResId = R.drawable.ic_renaissance,
            isUnlocked = true
        ),
        RenaissanceLevel(
            id = "2",
            title = "Artistic Revolution",
            description = "Discover the groundbreaking works of Leonardo da Vinci, Michelangelo, and Raphael.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 2
        ),
        RenaissanceLevel(
            id = "3",
            title = "Scientific Awakening",
            description = "Learn about the scientific advances of Copernicus, Galileo, and Vesalius.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 4
        ),
        RenaissanceLevel(
            id = "4",
            title = "Humanism and Philosophy",
            description = "Study the rise of humanism and the works of Petrarch, Erasmus, and Machiavelli.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 6
        ),
        RenaissanceLevel(
            id = "5",
            title = "Printing and Knowledge",
            description = "Examine the impact of Gutenberg's printing press and the spread of ideas.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 8
        ),
        RenaissanceLevel(
            id = "6",
            title = "Exploration and Discovery",
            description = "Follow the journeys of Columbus, da Gama, and Magellan during the Age of Exploration.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 10
        ),
        RenaissanceLevel(
            id = "7",
            title = "Northern Renaissance",
            description = "Discover the unique developments in art and culture in Northern Europe.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 12
        ),
        RenaissanceLevel(
            id = "8",
            title = "Renaissance Literature",
            description = "Explore the works of Shakespeare, Cervantes, and other literary giants.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 14
        ),
        RenaissanceLevel(
            id = "9",
            title = "Religious Change",
            description = "Understand the Reformation and its impact on European society.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 16
        ),
        RenaissanceLevel(
            id = "10",
            title = "Legacy of the Renaissance",
            description = "Examine the lasting influence of the Renaissance on modern science, art, and thought.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 18
        ),
        RenaissanceLevel(
            id = "11",
            title = "Renaissance Science",
            description = "Explore the scientific advancements and discoveries of the Renaissance.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 20
        ),
        RenaissanceLevel(
            id = "12",
            title = "Renaissance Literature",
            description = "Discover the influential writers and poets of the Renaissance.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 22
        ),
        RenaissanceLevel(
            id = "13",
            title = "Renaissance Architecture",
            description = "Examine the architectural innovations and masterpieces of the era.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 24
        ),
        RenaissanceLevel(
            id = "14",
            title = "Renaissance Exploration",
            description = "Learn about the explorers and new worlds discovered during the Renaissance.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 26
        ),
        RenaissanceLevel(
            id = "15",
            title = "Renaissance Politics",
            description = "Understand the political changes and influential leaders of the period.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 28
        ),
        RenaissanceLevel(
            id = "16",
            title = "Renaissance Philosophy",
            description = "Explore the philosophical ideas and thinkers of the Renaissance.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 30
        ),
        RenaissanceLevel(
            id = "17",
            title = "Renaissance Music",
            description = "Discover the music and composers that defined the Renaissance sound.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 32
        ),
        RenaissanceLevel(
            id = "18",
            title = "Renaissance Daily Life",
            description = "Learn about the daily life, customs, and culture of Renaissance people.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 34
        ),
        RenaissanceLevel(
            id = "19",
            title = "Renaissance Legacy",
            description = "Analyze the lasting impact of the Renaissance on modern society.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 36
        ),
        RenaissanceLevel(
            id = "20",
            title = "Renaissance Review",
            description = "Test your knowledge of all things Renaissance in this final challenge.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 38
        )
    )
} 