package com.hikmet.imperium.ui.level

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.ui.category.categoryDetails
import com.hikmet.imperium.ui.theme.AncientGradientStart
import com.hikmet.imperium.ui.theme.CorrectAnswer
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.LockedLevel
import com.hikmet.imperium.ui.theme.MedievalGradientStart
import com.hikmet.imperium.ui.theme.ModernGradientStart
import com.hikmet.imperium.ui.theme.Primary
import com.hikmet.imperium.ui.theme.RenaissanceGradientStart
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart

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
 * Sample levels for each category
 */
val levelsMap = mapOf(
    "ancient" to listOf(
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
    ),
    "medieval" to listOf(
        Level(id = 1, isUnlocked = true, stars = 3),
        Level(id = 2, isUnlocked = true, stars = 2),
        Level(id = 3, isUnlocked = true, stars = 0),
        Level(id = 4, isUnlocked = false, stars = 0),
        Level(id = 5, isUnlocked = false, stars = 0),
        Level(id = 6, isUnlocked = false, stars = 0),
        Level(id = 7, isUnlocked = false, stars = 0),
        Level(id = 8, isUnlocked = false, stars = 0),
        Level(id = 9, isUnlocked = false, stars = 0),
        Level(id = 10, isUnlocked = false, stars = 0),
        Level(id = 11, isUnlocked = false, stars = 0),
        Level(id = 12, isUnlocked = false, stars = 0)
    ),
    "renaissance" to listOf(
        Level(id = 1, isUnlocked = true, stars = 0),
        Level(id = 2, isUnlocked = false, stars = 0),
        Level(id = 3, isUnlocked = false, stars = 0),
        Level(id = 4, isUnlocked = false, stars = 0),
        Level(id = 5, isUnlocked = false, stars = 0),
        Level(id = 6, isUnlocked = false, stars = 0),
        Level(id = 7, isUnlocked = false, stars = 0),
        Level(id = 8, isUnlocked = false, stars = 0),
        Level(id = 9, isUnlocked = false, stars = 0),
        Level(id = 10, isUnlocked = false, stars = 0),
        Level(id = 11, isUnlocked = false, stars = 0),
        Level(id = 12, isUnlocked = false, stars = 0)
    ),
    "modern" to listOf(
        Level(id = 1, isUnlocked = true, stars = 1),
        Level(id = 2, isUnlocked = true, stars = 0),
        Level(id = 3, isUnlocked = false, stars = 0),
        Level(id = 4, isUnlocked = false, stars = 0),
        Level(id = 5, isUnlocked = false, stars = 0),
        Level(id = 6, isUnlocked = false, stars = 0),
        Level(id = 7, isUnlocked = false, stars = 0),
        Level(id = 8, isUnlocked = false, stars = 0),
        Level(id = 9, isUnlocked = false, stars = 0),
        Level(id = 10, isUnlocked = false, stars = 0),
        Level(id = 11, isUnlocked = false, stars = 0),
        Level(id = 12, isUnlocked = false, stars = 0)
    ),
    "world_wars" to listOf(
        Level(id = 1, isUnlocked = true, stars = 3),
        Level(id = 2, isUnlocked = true, stars = 3),
        Level(id = 3, isUnlocked = true, stars = 2),
        Level(id = 4, isUnlocked = true, stars = 1),
        Level(id = 5, isUnlocked = true, stars = 0),
        Level(id = 6, isUnlocked = false, stars = 0),
        Level(id = 7, isUnlocked = false, stars = 0),
        Level(id = 8, isUnlocked = false, stars = 0),
        Level(id = 9, isUnlocked = false, stars = 0),
        Level(id = 10, isUnlocked = false, stars = 0),
        Level(id = 11, isUnlocked = false, stars = 0),
        Level(id = 12, isUnlocked = false, stars = 0)
    )
)

// Default levels for fallback
val defaultLevels = levelsMap["ancient"] ?: emptyList()

/**
 * Level selection screen with grid of levels
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    navController: NavHostController,
    categoryId: String,
    levelSetId: String
) {
    // Get category details, with fallback to Ancient History if not found
    val categoryDetail = remember(categoryId) {
        categoryDetails[categoryId] ?: categoryDetails["ancient"]!!
    }
    
    val gradientColors = remember<List<Color>>(categoryDetail) {
        listOf(categoryDetail.gradientStart, categoryDetail.gradientEnd)
    }
    
    // Loading state
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    
    // Simulate loading delay for demo purposes
    LaunchedEffect(categoryId, levelSetId) {
        isLoading = true
        hasError = false
        
        // Simulate network delay
        kotlinx.coroutines.delay(800)
        
        // Check if we have data for this category
        if (levelsMap.containsKey(categoryId)) {
            isLoading = false
        } else {
            isLoading = false
            hasError = true
        }
    }
    
    // Get levels for selected category, with fallback
    val categoryLevels = remember<List<Level>>(categoryId, isLoading) {
        if (!isLoading) {
            levelsMap[categoryId] ?: levelsMap["ancient"] ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    val levelScreenAccessibilityDescription = remember(categoryDetail, categoryLevels.size) {
        "${categoryDetail.title} Levels - ${categoryLevels.size} levels available"
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "${categoryDetail.title}: Levels",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = gradientColors[0],
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { 
                            navController.popBackStack() 
                        },
                        modifier = Modifier.semantics { 
                            contentDescription = "Back to categories"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back to categories"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            gradientColors[0].copy(alpha = 0.1f),
                            gradientColors[1].copy(alpha = 0.05f)
                        )
                    )
                )
                .semantics { 
                    contentDescription = levelScreenAccessibilityDescription
                }
        ) {
            if (isLoading) {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.semantics { contentDescription = "Loading levels" }
                    ) {
                        CircularProgressIndicator(
                            color = gradientColors[0]
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading levels...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else if (hasError || categoryLevels.isEmpty()) {
                // Error state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(32.dp)
                            .semantics { contentDescription = "No levels available for this category" }
                    ) {
                        Text(
                            text = "No levels available",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "We couldn't find any levels for ${categoryDetail.title}. Please try another category.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.semantics { contentDescription = "Return to categories" },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = gradientColors[0]
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Back to Categories")
                        }
                    }
                }
            } else {
                // Content - show levels grid
                LevelGrid(
                    levels = categoryLevels,
                    navController = navController,
                    categoryId = categoryId,
                    categoryColor = gradientColors[0]
                )
            }
        }
    }
}

/**
 * Grid of level cards
 */
@Composable
fun LevelGrid(
    levels: List<Level>,
    navController: NavHostController,
    categoryId: String,
    categoryColor: Color
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(levels) { level ->
            LevelCard(
                level = level,
                onClick = {
                    if (level.isUnlocked) {
                        navController.navigate("quiz/$categoryId/${level.id}/STANDARD")
                    }
                },
                categoryColor = categoryColor
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
    onClick: () -> Unit,
    categoryColor: Color
) {
    val levelAccessibilityDescription = remember(level) {
        "Level ${level.id}, " + 
        (if (level.isUnlocked) "Unlocked, " else "Locked, ") + 
        "${level.stars} stars earned"
    }
    
    Card(
        onClick = onClick,
        enabled = level.isUnlocked,
        modifier = Modifier
            .size(100.dp)
            .semantics { 
                contentDescription = levelAccessibilityDescription
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (level.isUnlocked) 2.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (level.isUnlocked) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                Color.Gray.copy(alpha = 0.2f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (!level.isUnlocked) {
                    // Locked level
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked Level",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    // Level number
                    Text(
                        text = level.id.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = categoryColor,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Show stars if any
                    if (level.isUnlocked) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            repeat(3) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null, // Already covered by parent
                                    tint = if (index < level.stars) 
                                        categoryColor 
                                    else 
                                        Color.Gray.copy(alpha = 0.3f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
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
            categoryId = "ancient",
            levelSetId = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LevelCardPreview() {
    ImperiumTheme {
        LevelCard(
            level = levelsMap["ancient"]!![0],
            onClick = {},
            categoryColor = AncientGradientStart
        )
    }
} 