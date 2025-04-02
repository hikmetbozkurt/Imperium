package com.hikmet.imperium.data

import com.hikmet.imperium.R

data class MedievalLevel(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val requiredStars: Int = 0,
    val isUnlocked: Boolean = true
)

object MedievalLevels {
    val levels = listOf(
        MedievalLevel(
            id = "1",
            title = "Early Middle Ages",
            description = "Explore the period after the fall of Rome, including the rise of feudalism and the spread of Christianity.",
            imageResId = R.drawable.ic_medieval,
            isUnlocked = true
        ),
        MedievalLevel(
            id = "2",
            title = "The Crusades",
            description = "Learn about the religious wars between Christians and Muslims for control of the Holy Land.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 2
        ),
        MedievalLevel(
            id = "3",
            title = "Medieval Society",
            description = "Discover the social structure, daily life, and culture of medieval Europe.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 4
        ),
        MedievalLevel(
            id = "4",
            title = "The Black Death",
            description = "Study the devastating plague that changed medieval society and its lasting impact.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 6
        ),
        MedievalLevel(
            id = "5",
            title = "The Hundred Years' War",
            description = "Explore the long conflict between England and France that shaped medieval warfare.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 8
        ),
        MedievalLevel(
            id = "6",
            title = "Medieval Technology",
            description = "Learn about technological advances in agriculture, architecture, and warfare.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 10
        ),
        MedievalLevel(
            id = "7",
            title = "The Catholic Church",
            description = "Understand the powerful role of the Church in medieval society and politics.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 12
        ),
        MedievalLevel(
            id = "8",
            title = "Medieval Art & Architecture",
            description = "Study the development of Romanesque and Gothic styles in art and architecture.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 14
        ),
        MedievalLevel(
            id = "9",
            title = "Trade & Commerce",
            description = "Explore medieval trade routes, markets, and the rise of merchant guilds.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 16
        ),
        MedievalLevel(
            id = "10",
            title = "The Late Middle Ages",
            description = "Examine the transition period leading to the Renaissance and modern era.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 18
        ),
        MedievalLevel(
            id = "11",
            title = "Feudal Japan",
            description = "Discover the samurai culture and shogunate system of medieval Japan.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 20
        ),
        MedievalLevel(
            id = "12",
            title = "Byzantine Empire",
            description = "Learn about the Eastern Roman Empire and its influence on medieval Europe.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 22
        ),
        MedievalLevel(
            id = "13",
            title = "Islamic Golden Age",
            description = "Explore the scientific and cultural advancements of the medieval Islamic world.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 24
        ),
        MedievalLevel(
            id = "14",
            title = "Viking Age",
            description = "Study the Norse explorers, raiders, and traders who shaped early medieval Europe.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 26
        ),
        MedievalLevel(
            id = "15",
            title = "Mongol Empire",
            description = "Learn about the largest contiguous land empire in history and its impact on medieval Asia and Europe.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 28
        ),
        MedievalLevel(
            id = "16",
            title = "Medieval Warfare",
            description = "Discover the evolution of military tactics, weapons, and siegecraft in the Middle Ages.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 30
        ),
        MedievalLevel(
            id = "17",
            title = "Medieval Medicine",
            description = "Explore medical practices, beliefs, and healthcare in medieval societies.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 32
        ),
        MedievalLevel(
            id = "18",
            title = "Medieval Music",
            description = "Learn about the development of musical instruments, notation, and styles during the Middle Ages.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 34
        ),
        MedievalLevel(
            id = "19",
            title = "Medieval Literature",
            description = "Study the epics, romances, and poetry that defined medieval literary traditions.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 36
        ),
        MedievalLevel(
            id = "20",
            title = "End of the Middle Ages",
            description = "Examine the events and developments that marked the transition to the early modern period.",
            imageResId = R.drawable.ic_medieval,
            requiredStars = 38
        )
    )
} 