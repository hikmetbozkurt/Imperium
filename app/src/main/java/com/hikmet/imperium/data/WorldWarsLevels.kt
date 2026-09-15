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
            title = "Causes and World War I Basics",
            description = "Explore the tensions, alliances, weapons, and chronology of the First World War.",
            imageResId = R.drawable.ic_wars,
            isUnlocked = true
        ),
        WorldWarsLevel(
            id = "2",
            title = "World War II Leaders and Turning Points",
            description = "Meet the major leaders and identify decisive events of the Second World War.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 2
        ),
        WorldWarsLevel(
            id = "3",
            title = "World War I Strategy and Settlement",
            description = "Study strategy on the Western Front and the political settlement that followed.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 4
        ),
        WorldWarsLevel(
            id = "4",
            title = "Outbreak and Allied Return",
            description = "Follow the outbreak of war, entry of new belligerents, and the return to Western Europe.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 6
        ),
        WorldWarsLevel(
            id = "5",
            title = "Decisive Campaigns and the Atomic Project",
            description = "Examine major turning points, Allied command, and development of the atomic bomb.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 8
        ),
        WorldWarsLevel(
            id = "6",
            title = "Leadership and the Home Front",
            description = "Explore wartime leadership, international organization, civilian mobilization, and production.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 10
        ),
        WorldWarsLevel(
            id = "7",
            title = "The Holocaust and the Road to Victory",
            description = "Understand Nazi persecution while tracing the final path to victory in Europe and the Pacific.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 12
        ),
        WorldWarsLevel(
            id = "8",
            title = "Grand Strategy and Postwar Division",
            description = "Study coalition strategy, the Eastern Front, and the first division of postwar Europe.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 14
        ),
        WorldWarsLevel(
            id = "9",
            title = "Final Campaigns and International Justice",
            description = "Follow the last campaigns, occupation system, and prosecution of major war crimes.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 16
        ),
        WorldWarsLevel(
            id = "10",
            title = "Global Consequences and Wartime Intelligence",
            description = "Connect postwar independence with coalition politics and wartime codebreaking.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 18
        ),
        WorldWarsLevel(
            id = "11",
            title = "Axis Expansion and Major Land Campaigns",
            description = "Analyze aggression in Africa and Europe and the commanders and forces involved.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 20
        ),
        WorldWarsLevel(
            id = "12",
            title = "Command and Victory in the Pacific",
            description = "Explore commanders, campaigns, atomic warfare, and surrender in the Pacific theater.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 22
        ),
        WorldWarsLevel(
            id = "13",
            title = "Occupation, Intelligence, and Allied Institutions",
            description = "Examine occupation regimes, intelligence operations, military fronts, and Allied diplomacy.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 24
        ),
        WorldWarsLevel(
            id = "14",
            title = "Mobilization and Human Cost",
            description = "Study Allied decisions, women in wartime service, Nazi terror, and the human cost of battle.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 26
        ),
        WorldWarsLevel(
            id = "15",
            title = "Weapons, Treaties, and the Fall of France",
            description = "Understand military technology, diplomatic settlements, and operational planning.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 28
        ),
        WorldWarsLevel(
            id = "16",
            title = "Alliance Politics and Propaganda",
            description = "Explore Axis leadership, wartime alliances, state messaging, and public opinion.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 30
        ),
        WorldWarsLevel(
            id = "17",
            title = "War at Sea and Gallipoli",
            description = "Analyze naval warfare, submarine strategy, imperial leadership, and the Dardanelles campaign.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 32
        ),
        WorldWarsLevel(
            id = "18",
            title = "From World War to Cold War",
            description = "Connect wartime siege and monarchy with the new divided order in Korea.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 34
        ),
        WorldWarsLevel(
            id = "19",
            title = "Final Surrenders and Remembrance",
            description = "Study the last operations in Europe and how their human cost is remembered.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 36
        ),
        WorldWarsLevel(
            id = "20",
            title = "Reconstruction and International Justice",
            description = "Complete an advanced review of reconstruction, international institutions, and war-crimes trials.",
            imageResId = R.drawable.ic_wars,
            requiredStars = 38
        )
    )
}
