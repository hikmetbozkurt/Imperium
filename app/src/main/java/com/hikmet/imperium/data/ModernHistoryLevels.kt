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
            title = "Global War, 1914–1945",
            description = "Trace the alliances, leaders, and conflicts that reshaped the first half of the twentieth century.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 2
        ),
        ModernHistoryLevel(
            id = "3",
            title = "Depression and Postwar Alliances",
            description = "Connect economic crisis, global war, occupation, and the emerging alliance system.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 4
        ),
        ModernHistoryLevel(
            id = "4",
            title = "Independence and Cold War Milestones",
            description = "Follow decolonization, divided cities, regional war, and the first Moon landing.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 6
        ),
        ModernHistoryLevel(
            id = "5",
            title = "War Leadership and Early Cold War",
            description = "Examine wartime leadership, economic recovery, and the first major Cold War crises.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 8
        ),
        ModernHistoryLevel(
            id = "6",
            title = "Building the Postwar Order",
            description = "Study Korea, occupied Germany, the United Nations, and the atomic age.",
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
            title = "Rights, Space, and Divided Worlds",
            description = "Explore civil rights, the space race, Berlin, and revolutionary China.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 14
        ),
        ModernHistoryLevel(
            id = "9",
            title = "Mass Media and International Integration",
            description = "Connect television, early spaceflight, and Europe's first integration projects.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 16
        ),
        ModernHistoryLevel(
            id = "10",
            title = "Détente and Political Change",
            description = "Study lunar exploration, Vietnam, Watergate, and efforts to reduce Cold War tensions.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 18
        ),
        ModernHistoryLevel(
            id = "11",
            title = "Cold War Power Blocs",
            description = "Analyze containment, conflict in Korea, Soviet power, and Western leadership.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 20
        ),
        ModernHistoryLevel(
            id = "12",
            title = "The Transformative 1980s",
            description = "Examine political change, nuclear risk, democratic movements, and the weakening of apartheid.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 22
        ),
        ModernHistoryLevel(
            id = "13",
            title = "Post-Cold War Realignment",
            description = "Explore new states, regional conflicts, economic shifts, and European integration.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 24
        ),
        ModernHistoryLevel(
            id = "14",
            title = "Revolutions and Reunification",
            description = "Study reforms in China, democratic transitions, apartheid's end, and German unity.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 26
        ),
        ModernHistoryLevel(
            id = "15",
            title = "Crises of the New Millennium",
            description = "Analyze terrorism, war, pandemics, and political change in Europe.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 28
        ),
        ModernHistoryLevel(
            id = "16",
            title = "Leaders, Movements, and Emerging Powers",
            description = "Compare modern political leadership, racial justice movements, and China's rise.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 30
        ),
        ModernHistoryLevel(
            id = "17",
            title = "Digital Globalization",
            description = "Explore technology companies, political change, social media, and interconnected economies.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 32
        ),
        ModernHistoryLevel(
            id = "18",
            title = "Innovation, Climate, and the Arab Spring",
            description = "Connect digital industry and space exploration with climate action and popular uprisings.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 34
        ),
        ModernHistoryLevel(
            id = "19",
            title = "Global Milestones",
            description = "Test detailed knowledge of science, public health, sport, and newly independent states.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 36
        ),
        ModernHistoryLevel(
            id = "20",
            title = "Contemporary Institutions and Climate",
            description = "Complete an advanced review of global governance, climate policy, and European change.",
            imageResId = R.drawable.ic_modern,
            requiredStars = 38
        )
    )
}
