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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.ImperiumApplication
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.AncientGradientStart
import com.hikmet.imperium.ui.theme.CorrectAnswer
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.LockedLevel
import com.hikmet.imperium.ui.theme.MedievalGradientStart
import com.hikmet.imperium.ui.theme.ModernGradientStart
import com.hikmet.imperium.ui.theme.Primary
import com.hikmet.imperium.ui.theme.RenaissanceGradientStart
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart
import com.hikmet.imperium.ui.viewmodel.CategoryViewModel
import com.hikmet.imperium.ui.viewmodel.DataState
import com.hikmet.imperium.ui.viewmodel.LevelWithProgress

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
    categoryId: String
) {
    // Get the repository from the application
    val context = LocalContext.current
    val repository = (context.applicationContext as ImperiumApplication).repository
    
    // Initialize ViewModel
    val categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModel.Factory(repository)
    )
    
    // Select category to load its levels
    LaunchedEffect(categoryId) {
        categoryViewModel.selectCategory(categoryId)
    }
    
    // Collect state from ViewModel
    val levelsState by categoryViewModel.levelsState.collectAsState()
    val selectedCategory by categoryViewModel.selectedCategory.collectAsState()
    val totalStars by categoryViewModel.totalStars.collectAsState()
    
    // Set up UI elements
    val categoryTitle = selectedCategory?.title ?: "Select Level"
    
    // Determine primary color for this category
    val primaryColor = when (categoryId) {
        "ancient" -> AncientGradientStart
        "medieval" -> MedievalGradientStart
        "renaissance" -> RenaissanceGradientStart
        "modern" -> ModernGradientStart
        "world_wars" -> WorldWarsGradientStart
        else -> Primary
    }
    
    // Accessibility description
    val levelScreenAccessibilityDescription = "Level selection screen for $categoryTitle"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = categoryTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    // Show total stars earned
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = totalStars.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
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
                .background(Color.White)
                .semantics { 
                    contentDescription = levelScreenAccessibilityDescription
                }
        ) {
            when (levelsState) {
                is DataState.Loading -> {
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
                                color = primaryColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading levels...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                
                is DataState.Empty -> {
                    // No levels found
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text(
                                text = "No levels available",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.popBackStack() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
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
                }
                
                is DataState.Error -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text(
                                text = "Error loading levels",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = (levelsState as DataState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.popBackStack() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
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
                }
                
                is DataState.Success -> {
                    // Content - show level grid with title
                    val levels = (levelsState as DataState.Success<List<LevelWithProgress>>).data
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Select a Level",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                        )
                        
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(levels) { levelWithProgress ->
                                LevelCard(
                                    levelWithProgress = levelWithProgress,
                                    onClick = {
                                        if (levelWithProgress.isUnlocked) {
                                            navController.navigate(NavDestinations.QUIZ_ROUTE
                                                .replace("{categoryId}", categoryId)
                                                .replace("{levelId}", levelWithProgress.level.levelNumber.toString())
                                                .replace("{quizType}", "STANDARD")
                                            )
                                        }
                                    },
                                    categoryColor = primaryColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual level card
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelCard(
    levelWithProgress: LevelWithProgress,
    onClick: () -> Unit,
    categoryColor: Color
) {
    val level = levelWithProgress.level
    val isUnlocked = levelWithProgress.isUnlocked
    val starsEarned = levelWithProgress.starsEarned
    
    val levelAccessibilityDescription = remember(level, isUnlocked, starsEarned) {
        "Level ${level.levelNumber}, " + 
        (if (isUnlocked) "Unlocked, " else "Locked, ") + 
        "$starsEarned stars earned"
    }
    
    Card(
        onClick = onClick,
        enabled = isUnlocked,
        modifier = Modifier
            .size(100.dp)
            .semantics { 
                contentDescription = levelAccessibilityDescription
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isUnlocked) 4.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) 
                Color.White
            else 
                Color.Gray.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isUnlocked) null else androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
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
                if (!isUnlocked) {
                    // Locked level
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked Level",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Level ${level.levelNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    
                    if (levelWithProgress.starsRequired > 0) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700).copy(alpha = 0.5f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = levelWithProgress.starsRequired.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    // Unlocked level - circular background with number
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(categoryColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = level.levelNumber.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = if (levelWithProgress.isCompleted) "Completed" else "Level ${level.levelNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (levelWithProgress.isCompleted) categoryColor else Color.Black,
                        fontWeight = if (levelWithProgress.isCompleted) FontWeight.Bold else FontWeight.Normal
                    )
                    
                    // Show stars if any
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        repeat(3) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null, // Already covered by parent
                                tint = if (index < starsEarned) 
                                    Color(0xFFFFD700) // Gold
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

@Preview
@Composable
fun LevelScreenPreview() {
    ImperiumTheme {
        LevelScreen(
            navController = rememberNavController(),
            categoryId = "ancient"
        )
    }
} 