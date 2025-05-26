package com.hikmet.imperium.data

import com.hikmet.imperium.R

data class WorldWarsLevel(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val requiredStars: Int = 0,
    val isUnlocked: Boolean = true
)

object WorldWarsLevels {
    val levels = listOf(
        WorldWarsLevel(
            id = "1",
            title = "Prelude to War",
            description = "Explore the causes and tensions leading up to World War I.",
            imageResId = R.drawable.ic_wars,
            isUnlocked = true
        ),
        WorldWarsLevel(
            id = "2",
            title = "World War I",
            description = "Understand the major battles, alliances, and outcomes of the First World War.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 2
        ),
        WorldWarsLevel(
            id = "3",
            title = "Interwar Years",
            description = "Examine the Treaty of Versailles, economic crises, and rise of totalitarianism.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 4
        ),
        WorldWarsLevel(
            id = "4",
            title = "World War II Begins",
            description = "Learn about the outbreak of WWII and the early years of conflict.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 6
        ),
        WorldWarsLevel(
            id = "5",
            title = "Turning Points",
            description = "Study key battles and events that shifted the course of WWII.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 8
        ),
        WorldWarsLevel(
            id = "6",
            title = "The Home Front",
            description = "Explore the impact of war on civilians and economies.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 10
        ),
        WorldWarsLevel(
            id = "7",
            title = "The Holocaust",
            description = "Understand the atrocities and human cost of the Holocaust.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 12
        ),
        WorldWarsLevel(
            id = "8",
            title = "End of World War II",
            description = "Examine the final campaigns, surrender, and aftermath of WWII.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 14
        ),
        WorldWarsLevel(
            id = "9",
            title = "Postwar World",
            description = "Discover the creation of the United Nations and the start of the Cold War.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 16
        ),
        WorldWarsLevel(
            id = "10",
            title = "Legacy of the World Wars",
            description = "Analyze the long-term effects of the world wars on global politics and society.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 18
        ),
        WorldWarsLevel(
            id = "11",
            title = "WWII in the Pacific",
            description = "Explore the major battles and events in the Pacific theater.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 20
        ),
        WorldWarsLevel(
            id = "12",
            title = "Resistance Movements",
            description = "Learn about the resistance movements during the World Wars.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 22
        ),
        WorldWarsLevel(
            id = "13",
            title = "Espionage and Intelligence",
            description = "Examine the role of spies and intelligence agencies in the wars.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 24
        ),
        WorldWarsLevel(
            id = "14",
            title = "Women in War",
            description = "Discover the contributions of women during the World Wars.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 26
        ),
        WorldWarsLevel(
            id = "15",
            title = "War Technology",
            description = "Understand the technological advancements made during the wars.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 28
        ),
        WorldWarsLevel(
            id = "16",
            title = "Propaganda and Media",
            description = "Explore the use of propaganda and media in shaping public opinion.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 30
        ),
        WorldWarsLevel(
            id = "17",
            title = "Postwar Recovery",
            description = "Learn about the reconstruction and recovery after the wars.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 32
        ),
        WorldWarsLevel(
            id = "18",
            title = "Decolonization",
            description = "Examine the process of decolonization following the World Wars.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 34
        ),
        WorldWarsLevel(
            id = "19",
            title = "Memory and Memorials",
            description = "Discover how the World Wars are remembered and commemorated.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 36
        ),
        WorldWarsLevel(
            id = "20",
            title = "World Wars Review",
            description = "Test your knowledge of the World Wars in this final challenge.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 38
        )
    )
} 