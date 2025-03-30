package com.hikmet.imperium.ui.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.ui.theme.CorrectAnswer
import com.hikmet.imperium.ui.theme.Disabled
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.LockedLevel
import com.hikmet.imperium.ui.theme.Primary

/**
 * Level data class
 */
data class Level(
    val id: Int,
    val isUnlocked: Boolean,
    val stars: Int, // 0-3 stars
    val quizTypes: List<String> = listOf("STANDARD", "TIME_ATTACK", "IMAGE_BASED")
)

/**
 * Sample levels for preview
 */
val sampleLevels = listOf(
    Level(id = 1, isUnlocked = true, stars = 3),
    Level(id = 2, isUnlocked = true, stars = 2),
    Level(id = 3, isUnlocked = true, stars = 1),
    Level(id = 4, isUnlocked = true, stars = 0),
    Level(id = 5, isUnlocked = false, stars = 0),
    Level(id = 6, isUnlocked = false, stars = 0),
    Level(id = 7, isUnlocked = false, stars = 0),
    Level(id = 8, isUnlocked = false, stars = 0),
    Level(id = 9, isUnlocked = false, stars = 0),
    Level(id = 10, isUnlocked = false, stars = 0),
    Level(id = 11, isUnlocked = false, stars = 0),
    Level(id = 12, isUnlocked = false, stars = 0)
)

/**
 * Category data for the level screen
 */
data class LevelScreenCategory(
    val id: String,
    val title: String
)

/**
 * Sample category for preview
 */
val sampleCategory = LevelScreenCategory(
    id = "ancient",
    title = "Ancient Civilizations"
)

/**
 * Level selection screen with grid of levels
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    navController: NavHostController,
    categoryId: String?,
    levelId: String? = null,
    category: LevelScreenCategory = sampleCategory,
    levels: List<Level> = sampleLevels
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            LevelGrid(
                levels = levels,
                onLevelClick = { level ->
                    if (level.isUnlocked) {
                        // Navigate to level detail or directly to quiz
                        navController.navigate("quiz/${categoryId}/${level.id}/STANDARD")
                    } else {
                        // Could show a toast message that the level is locked
                    }
                },
                contentPadding = paddingValues
            )
        }
    )
}

/**
 * Grid of level cards
 */
@Composable
fun LevelGrid(
    levels: List<Level>,
    onLevelClick: (Level) -> Unit,
    contentPadding: PaddingValues
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(levels) { level ->
            LevelCard(
                level = level,
                onClick = { onLevelClick(level) }
            )
        }
    }
}

/**
 * Individual level card
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelCard(
    level: Level,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !level.isUnlocked -> LockedLevel
        level.stars > 0 -> CorrectAnswer
        else -> MaterialTheme.colorScheme.surface
    }
    
    val textColor = when {
        !level.isUnlocked -> Disabled
        level.stars > 0 -> Primary
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    Card(
        onClick = onClick,
        modifier = Modifier.size(110.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (level.isUnlocked) 2.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Level content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (!level.isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked Level",
                        tint = Disabled,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(0.7f)
                    )
                }
                
                Text(
                    text = level.id.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                
                if (level.isUnlocked) {
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Star rating
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(3) { index ->
                            Icon(
                                imageVector = if (index < level.stars) 
                                    Icons.Filled.Star 
                                else 
                                    Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (index < level.stars) 
                                    MaterialTheme.colorScheme.secondary 
                                else 
                                    Disabled,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LevelScreenPreview() {
    ImperiumTheme {
        LevelScreen(
            navController = rememberNavController(),
            categoryId = "ancient"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LevelCardPreview() {
    ImperiumTheme {
        LevelCard(
            level = sampleLevels[0],
            onClick = {}
        )
    }
} 