package com.hikmet.imperium.data

import com.hikmet.imperium.R

data class ModernHistoryLevel(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val requiredStars: Int = 0,
    val isUnlocked: Boolean = true
)

object ModernHistoryLevels {
    val levels = listOf(
        ModernHistoryLevel(
            id = "1",
            title = "Industrial Revolution",
            description = "Explore the technological and social changes of the Industrial Revolution.",
            imageResId = R.drawable.ic_modern,
            isUnlocked = true
        ),
        ModernHistoryLevel(
            id = "2",
            title = "Age of Imperialism",
            description = "Learn about the expansion of empires and global influence.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 2
        ),
        ModernHistoryLevel(
            id = "3",
            title = "World War I",
            description = "Understand the causes, events, and consequences of the First World War.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 4
        ),
        ModernHistoryLevel(
            id = "4",
            title = "Interwar Period",
            description = "Examine the social and political changes between the world wars.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 6
        ),
        ModernHistoryLevel(
            id = "5",
            title = "World War II",
            description = "Study the global conflict, its leaders, and its impact on the world.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 8
        ),
        ModernHistoryLevel(
            id = "6",
            title = "Cold War Era",
            description = "Explore the rivalry between the US and USSR and the nuclear age.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 10
        ),
        ModernHistoryLevel(
            id = "7",
            title = "Decolonization",
            description = "Learn about the end of empires and the rise of new nations.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 12
        ),
        ModernHistoryLevel(
            id = "8",
            title = "Civil Rights Movements",
            description = "Examine the struggle for equality and justice in the 20th century.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 14
        ),
        ModernHistoryLevel(
            id = "9",
            title = "Technological Revolution",
            description = "Discover the impact of computers, the internet, and modern technology.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 16
        ),
        ModernHistoryLevel(
            id = "10",
            title = "Contemporary World",
            description = "Analyze recent history and the challenges of the 21st century.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 18
        ),
        ModernHistoryLevel(
            id = "11",
            title = "Cold War Begins",
            description = "Explore the origins and early years of the Cold War.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 20
        ),
        ModernHistoryLevel(
            id = "12",
            title = "Space Race",
            description = "Discover the competition between the US and USSR to conquer space.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 22
        ),
        ModernHistoryLevel(
            id = "13",
            title = "Civil Rights Movements",
            description = "Examine the global movements for civil rights and equality.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 24
        ),
        ModernHistoryLevel(
            id = "14",
            title = "Vietnam War",
            description = "Learn about the causes, events, and impact of the Vietnam War.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 26
        ),
        ModernHistoryLevel(
            id = "15",
            title = "End of the Cold War",
            description = "Understand the events leading to the collapse of the Soviet Union.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 28
        ),
        ModernHistoryLevel(
            id = "16",
            title = "Rise of Technology",
            description = "Explore the technological advancements of the late 20th century.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 30
        ),
        ModernHistoryLevel(
            id = "17",
            title = "Globalization",
            description = "Discover the increasing interconnectedness of the modern world.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 32
        ),
        ModernHistoryLevel(
            id = "18",
            title = "21st Century Challenges",
            description = "Learn about the major challenges facing the world in the 21st century.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 34
        ),
        ModernHistoryLevel(
            id = "19",
            title = "Modern Politics",
            description = "Analyze the political changes and leaders of the modern era.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 36
        ),
        ModernHistoryLevel(
            id = "20",
            title = "Modern History Review",
            description = "Test your knowledge of modern history in this final challenge.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 38
        )
    )
} 