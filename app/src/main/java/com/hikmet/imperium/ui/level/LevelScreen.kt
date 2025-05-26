package com.hikmet.imperium.ui.level

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.hikmet.imperium.R
import com.hikmet.imperium.data.AncientLevels
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.*
import com.hikmet.imperium.ui.viewmodel.CategoryViewModel
import com.hikmet.imperium.ui.viewmodel.DataState
import com.hikmet.imperium.ui.viewmodel.LevelWithProgress

/**
 * Data class for level info
 */
data class Level(
    val id: Int,
    val title: String = "Level $id",
    val description: String = "Level $id",
    val imageResId: Int = R.drawable.ic_ancient,
    val isUnlocked: Boolean = false,
    val stars: Int = 0,
    val requiredStars: Int = 0
)

/**
 * Sample data for levels
 */
val levelsMap = mapOf(
    "ancient" to listOf(
        Level(id = 1, isUnlocked = true, stars = 2),
        Level(id = 2, isUnlocked = true, stars = 1),
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
    
    // Calculate category-specific stars
    var categoryStars by remember { mutableStateOf(0) }
    
    // Update category stars when levelsState changes
    LaunchedEffect(levelsState) {
        if (levelsState is DataState.Success) {
            val levels = (levelsState as DataState.Success<List<LevelWithProgress>>).data
            categoryStars = levels.sumOf { it.starsEarned }
        }
    }
    
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
                    IconButton(onClick = { 
                        navController.navigate(NavDestinations.HOME_ROUTE) {
                            // Clear back stack up to home
                            popUpTo(NavDestinations.HOME_ROUTE) {
                                inclusive = false
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home"
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
                            text = categoryStars.toString(),
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
            // For Ancient category, use specific handling just like Medieval
            if (categoryId == "ancient") {
                AncientLevelGrid(navController, totalStars, levelsState)
            } else {
                // Other categories use the generic state handling
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
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = primaryColor,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No levels found for this category",
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Please check back later",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    
                    is DataState.Error -> {
                        // Error state
                        val error = (levelsState as DataState.Error).message
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Error loading levels",
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = error,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    
                    is DataState.Success -> {
                        // Successful data load
                        val levelsWithProgress = (levelsState as DataState.Success<List<LevelWithProgress>>).data
                        
                        // Convert LevelWithProgress to Level
                        val levels = levelsWithProgress.map { lwp ->
                            Level(
                                id = lwp.level.levelNumber,
                                title = "Level ${lwp.level.levelNumber}",
                                description = lwp.level.description ?: "Level ${lwp.level.levelNumber}",
                                imageResId = R.drawable.ic_ancient, // Default - should be determined by category
                                isUnlocked = lwp.isUnlocked,
                                stars = lwp.starsEarned,
                                requiredStars = lwp.starsRequired
                            )
                        }
                        
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
                                        // Navigate to the quiz for this level if unlocked
                                        if (level.isUnlocked) {
                                            navController.navigate(
                                                NavDestinations.QUIZ_ROUTE
                                                    .replace("{categoryId}", categoryId)
                                                    .replace("{levelId}", level.id.toString())
                                                    .replace("{quizType}", "STANDARD")
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AncientLevelGrid(
    navController: NavHostController,
    totalStars: Int,
    levelsState: DataState<List<LevelWithProgress>>
) {
    // Get user progress for Ancient category from the DB state
    val userProgress = when (levelsState) {
        is DataState.Success -> levelsState.data
        else -> emptyList()
    }
    
    // Use the AncientLevels data and map it to UI state
    val levels = AncientLevels.levels.mapIndexed { index, ancientLevel ->
        // Find matching progress if available
        val progressForThisLevel = userProgress.find { it.level.levelNumber == index + 1 }
        
        Level(
            id = ancientLevel.id.toInt(),
            title = ancientLevel.title,
            description = ancientLevel.description,
            imageResId = ancientLevel.imageResId,
            isUnlocked = progressForThisLevel?.isUnlocked ?: (index == 0 || totalStars >= ancientLevel.requiredStars),
            stars = progressForThisLevel?.starsEarned ?: 0,
            requiredStars = ancientLevel.requiredStars
        )
    }
    
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
                    // Navigate to the appropriate quiz screen
                    if (level.isUnlocked) {
                        navController.navigate(
                            NavDestinations.ANCIENT_QUIZ_ROUTE
                                .replace("{levelId}", level.id.toString())
                        )
                    }
                }
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
    val levelAccessibilityDescription = remember(level, level.isUnlocked) {
        "Level ${level.id}, " + 
        (if (level.isUnlocked) "Unlocked" else "Locked")
    }
    
    // Check if level is completed (has stars)
    val isCompleted = level.stars > 0
    
    // Colors for ancient level cards
    val ancientPrimary = AncientGradientStart
    val gradientStart = AncientGradientStart
    val gradientEnd = AncientGradientEnd
    
    Card(
        onClick = onClick,
        enabled = level.isUnlocked,
        modifier = Modifier
            .size(100.dp)
            .semantics { 
                contentDescription = levelAccessibilityDescription
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (level.isUnlocked) 4.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (level.isUnlocked && !isCompleted) 
                Color.White
            else if (!level.isUnlocked)
                Color.Gray.copy(alpha = 0.1f)
            else
                Color.White // For completed levels, we'll add a gradient background
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (level.isUnlocked && !isCompleted) 
            androidx.compose.foundation.BorderStroke(1.dp, ancientPrimary.copy(alpha = 0.3f))
        else if (!level.isUnlocked) 
            androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
        else null
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Add gradient background for completed levels
            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(gradientStart, gradientEnd)
                            )
                        )
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (!level.isUnlocked) {
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
                        text = "Level ${level.id}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    
                    if (level.requiredStars > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "${level.requiredStars}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                } else if (isCompleted) {
                    // Completed level with stars
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = level.id.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = ancientPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(3) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < level.stars) Color.Yellow else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    // Unlocked but not completed level
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, ancientPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = level.id.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = ancientPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Level ${level.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
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