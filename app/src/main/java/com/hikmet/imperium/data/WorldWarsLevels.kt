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
            title = "Causes of WWI",
            description = "Explore the complex factors that led to the outbreak of the First World War.",
            imageResId = R.drawable.ic_wars,
            isUnlocked = true
        ),
        WorldWarsLevel(
            id = "2",
            title = "WWI: Western Front",
            description = "Study the brutal trench warfare and major battles in Western Europe.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 2
        ),
        WorldWarsLevel(
            id = "3",
            title = "WWI: Other Fronts",
            description = "Learn about the Eastern Front, Italian Front, and other theaters of World War I.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 4
        ),
        WorldWarsLevel(
            id = "4",
            title = "New Technology in WWI",
            description = "Discover the military innovations that transformed warfare during WWI.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 6
        ),
        WorldWarsLevel(
            id = "5",
            title = "Treaty of Versailles",
            description = "Examine the peace settlement that ended WWI and set the stage for WWII.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 8
        ),
        WorldWarsLevel(
            id = "6",
            title = "Rise of Fascism",
            description = "Study the emergence of totalitarian regimes in Italy, Germany, and elsewhere.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 10
        ),
        WorldWarsLevel(
            id = "7",
            title = "Causes of WWII",
            description = "Learn about the events and policies that led to the Second World War.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 12
        ),
        WorldWarsLevel(
            id = "8",
            title = "Blitzkrieg & Early WWII",
            description = "Explore Germany's lightning warfare and early victories from 1939-1941.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 14
        ),
        WorldWarsLevel(
            id = "9",
            title = "The Holocaust",
            description = "Study the Nazi genocide against Jews and other groups during WWII.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 16
        ),
        WorldWarsLevel(
            id = "10",
            title = "Pacific Theater",
            description = "Learn about the war between Japan and the Allies in the Pacific region.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 18
        ),
        WorldWarsLevel(
            id = "11",
            title = "Eastern Front",
            description = "Discover the massive campaigns between Nazi Germany and the Soviet Union.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 20
        ),
        WorldWarsLevel(
            id = "12",
            title = "D-Day & Liberation",
            description = "Study the Allied invasion of Normandy and the liberation of Western Europe.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 22
        ),
        WorldWarsLevel(
            id = "13",
            title = "End of WWII",
            description = "Examine the final battles, atomic bombings, and surrender of the Axis powers.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 24
        ),
        WorldWarsLevel(
            id = "14",
            title = "Aftermath & Legacy",
            description = "Explore the consequences of WWII, including the Cold War and decolonization.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 26
        ),
        WorldWarsLevel(
            id = "15",
            title = "Remembrance & Memory",
            description = "Learn how the world wars are commemorated and their lessons for today.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 28
        ),
        WorldWarsLevel(
            id = "16",
            title = "Post-WWII & Cold War",
            description = "Understand the origins of the Cold War and post-war reconstruction.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 30
        ),
        WorldWarsLevel(
            id = "17",
            title = "Decolonization",
            description = "Explore the wave of independence movements after WWII.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 32
        ),
        WorldWarsLevel(
            id = "18",
            title = "United Nations Formation",
            description = "Study the creation and purpose of the UN.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 34
        ),
        WorldWarsLevel(
            id = "19",
            title = "Cold War Conflicts",
            description = "Learn about proxy wars and tensions between superpowers.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 36
        ),
        WorldWarsLevel(
            id = "20",
            title = "Legacy of the Wars",
            description = "Examine the long-term impacts and memories of the world wars.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 38
        )
    )
} 