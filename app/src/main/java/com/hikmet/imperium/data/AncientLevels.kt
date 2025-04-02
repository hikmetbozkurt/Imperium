package com.hikmet.imperium.data

import com.hikmet.imperium.R

data class AncientLevel(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val requiredStars: Int = 0,
    val isUnlocked: Boolean = true
)

object AncientLevels {
    val levels = listOf(
        AncientLevel(
            id = "1",
            title = "Ancient Egypt: Beginnings",
            description = "Explore the rise of ancient Egyptian civilization along the Nile River and its early dynasties.",
            imageResId = R.drawable.ic_ancient,
            isUnlocked = true
        ),
        AncientLevel(
            id = "2",
            title = "Egyptian Society",
            description = "Learn about the social structure, daily life, and governance of ancient Egypt.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 2
        ),
        AncientLevel(
            id = "3",
            title = "Egyptian Culture",
            description = "Discover the rich cultural traditions, religion, and mythology of ancient Egypt.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 4
        ),
        AncientLevel(
            id = "4",
            title = "Egyptian Legacy",
            description = "Study the lasting achievements and influence of ancient Egyptian civilization.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 6
        ),
        AncientLevel(
            id = "5",
            title = "Ancient Greece: Origins",
            description = "Explore the foundation of ancient Greek civilization and early city-states.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 8
        ),
        AncientLevel(
            id = "6",
            title = "Classical Greece",
            description = "Learn about the Golden Age of Athens, Greek democracy, and the Peloponnesian War.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 10
        ),
        AncientLevel(
            id = "7",
            title = "Greek Culture & Philosophy",
            description = "Discover the groundbreaking contributions of Greek thinkers, artists, and writers.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 12
        ),
        AncientLevel(
            id = "8",
            title = "Ancient Rome: Beginnings",
            description = "Study the foundation of Rome, the Republic, and early Roman institutions.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 14
        ),
        AncientLevel(
            id = "9",
            title = "Roman Republic",
            description = "Explore the height of the Roman Republic, its conquests, and political system.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 16
        ),
        AncientLevel(
            id = "10",
            title = "Roman Empire",
            description = "Learn about the transition to empire, its expansion, and eventual decline.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 18
        ),
        AncientLevel(
            id = "11",
            title = "Mesopotamia",
            description = "Discover the world's first civilizations in the fertile crescent between the Tigris and Euphrates.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 20
        ),
        AncientLevel(
            id = "12",
            title = "Ancient Babylon",
            description = "Study the achievements of Babylonian civilization, including its laws, science, and architecture.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 22
        ),
        AncientLevel(
            id = "13",
            title = "Ancient China",
            description = "Explore the early dynasties of China and the foundations of Chinese civilization.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 24
        ),
        AncientLevel(
            id = "14",
            title = "Chinese Culture",
            description = "Learn about ancient Chinese philosophy, inventions, and cultural traditions.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 26
        ),
        AncientLevel(
            id = "15",
            title = "Ancient India",
            description = "Discover the Indus Valley Civilization, early kingdoms, and the foundations of Hindu culture.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 28
        ),
        AncientLevel(
            id = "16",
            title = "Ancient Persia",
            description = "Study the Persian Empire, its administration, religion, and conflicts with Greece.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 30
        ),
        AncientLevel(
            id = "17",
            title = "Ancient Americas",
            description = "Explore early civilizations of the Americas, including the Maya, Inca, and Olmec.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 32
        ),
        AncientLevel(
            id = "18",
            title = "Ancient Africa",
            description = "Learn about ancient African kingdoms, including Kush, Axum, and early North African civilizations.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 34
        ),
        AncientLevel(
            id = "19",
            title = "Ancient Technologies",
            description = "Discover the innovative technologies and scientific achievements of ancient civilizations.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 36
        ),
        AncientLevel(
            id = "20",
            title = "Legacy of the Ancients",
            description = "Examine how ancient civilizations shaped our modern world in law, politics, science, and culture.",
            imageResId = R.drawable.ic_ancient,
            requiredStars = 38
        )
    )
} 