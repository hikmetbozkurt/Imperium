package com.hikmet.imperium.data

import com.hikmet.imperium.R

data class ModernLevel(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val requiredStars: Int = 0,
    val isUnlocked: Boolean = true
)

object ModernLevels {
    val levels = listOf(
        ModernLevel(
            id = "1",
            title = "The Industrial Revolution",
            description = "Discover how machinery, factories, and new technologies transformed society.",
            imageResId = R.drawable.ic_modern,
            isUnlocked = true
        ),
        ModernLevel(
            id = "2",
            title = "American Revolution",
            description = "Learn about the struggle for independence and the birth of the United States.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 2
        ),
        ModernLevel(
            id = "3",
            title = "French Revolution",
            description = "Explore the political upheaval that transformed France and inspired revolutionary movements.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 4
        ),
        ModernLevel(
            id = "4",
            title = "Napoleon Bonaparte",
            description = "Study the life and legacy of the military genius who reshaped Europe.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 6
        ),
        ModernLevel(
            id = "5",
            title = "Victorian Era",
            description = "Explore the cultural, social, and technological changes during Queen Victoria's reign.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 8
        ),
        ModernLevel(
            id = "6",
            title = "Colonialism & Imperialism",
            description = "Learn about European powers' global expansion and its consequences.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 10
        ),
        ModernLevel(
            id = "7",
            title = "Scientific Breakthroughs",
            description = "Discover major scientific theories and inventions that revolutionized our understanding.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 12
        ),
        ModernLevel(
            id = "8",
            title = "The Gilded Age",
            description = "Study the era of rapid growth, industrialization, and wealth inequality in America.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 14
        ),
        ModernLevel(
            id = "9",
            title = "Suffrage Movements",
            description = "Learn about the struggle for voting rights for women and minorities.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 16
        ),
        ModernLevel(
            id = "10",
            title = "The Great Depression",
            description = "Explore the global economic crisis and its impact on society and politics.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 18
        ),
        ModernLevel(
            id = "11",
            title = "Cold War Era",
            description = "Study the tense period of geopolitical conflict between superpowers.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 20
        ),
        ModernLevel(
            id = "12",
            title = "Civil Rights Movement",
            description = "Learn about the struggle for equality and justice for all people.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 22
        ),
        ModernLevel(
            id = "13",
            title = "Space Exploration",
            description = "Discover the achievements in human spaceflight and planetary exploration.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 24
        ),
        ModernLevel(
            id = "14",
            title = "Digital Revolution",
            description = "Explore the development of computers, the internet, and the information age.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 26
        ),
        ModernLevel(
            id = "15",
            title = "Globalization",
            description = "Study the increasing interconnectedness of economies, cultures, and societies.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 28
        )
    )
} 