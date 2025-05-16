package com.hikmet.imperium.data

import com.hikmet.imperium.R

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
            title = "Early Renaissance",
            description = "Explore the beginnings of the Renaissance in Florence and the revival of classical learning.",
            imageResId = R.drawable.ic_renaissance,
            isUnlocked = true
        ),
        RenaissanceLevel(
            id = "2",
            title = "Renaissance Art",
            description = "Study the works of great artists like Leonardo da Vinci, Michelangelo, and Raphael.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 2
        ),
        RenaissanceLevel(
            id = "3",
            title = "Humanism",
            description = "Learn about the intellectual movement that emphasized human potential and achievements.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 4
        ),
        RenaissanceLevel(
            id = "4",
            title = "The Medici Family",
            description = "Discover the powerful banking family who became patrons of Renaissance art and culture.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 6
        ),
        RenaissanceLevel(
            id = "5",
            title = "Renaissance Architecture",
            description = "Explore the revival of classical architecture and the development of new techniques.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 8
        ),
        RenaissanceLevel(
            id = "6",
            title = "Science & Invention",
            description = "Study the scientific advancements and inventions of Renaissance polymaths.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 10
        ),
        RenaissanceLevel(
            id = "7",
            title = "The Printing Press",
            description = "Learn about Gutenberg's invention and its revolutionary impact on knowledge sharing.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 12
        ),
        RenaissanceLevel(
            id = "8",
            title = "Renaissance Literature",
            description = "Explore the works of Shakespeare, Cervantes, and other literary giants of the era.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 14
        ),
        RenaissanceLevel(
            id = "9",
            title = "Renaissance Music",
            description = "Discover the evolution of musical styles, instruments, and compositions in this period.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 16
        ),
        RenaissanceLevel(
            id = "10",
            title = "The Catholic Church",
            description = "Examine the Church's role in Renaissance culture and the beginnings of reform movements.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 18
        ),
        RenaissanceLevel(
            id = "11",
            title = "Northern Renaissance",
            description = "Study how Renaissance ideas spread to Northern Europe and their unique manifestations.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 20
        ),
        RenaissanceLevel(
            id = "12",
            title = "Women in the Renaissance",
            description = "Learn about notable women who made contributions to Renaissance art, literature, and society.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 22
        ),
        RenaissanceLevel(
            id = "13",
            title = "The Age of Exploration",
            description = "Explore how Renaissance curiosity and technology led to global exploration and discovery.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 24
        ),
        RenaissanceLevel(
            id = "14",
            title = "High Renaissance",
            description = "Study the peak period of artistic achievement in the early 16th century.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 26
        ),
        RenaissanceLevel(
            id = "15",
            title = "Politics & Diplomacy",
            description = "Discover how modern politics and diplomatic practices emerged during the Renaissance.",
            imageResId = R.drawable.ic_renaissance,
            requiredStars = 28
        )
    )
} 