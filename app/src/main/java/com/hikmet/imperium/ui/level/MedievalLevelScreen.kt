package com.hikmet.imperium.ui.level

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.data.MedievalLevels
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.viewmodel.LevelViewModel
import com.hikmet.imperium.ImperiumApplication
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest
import android.util.Log

private const val MEDIEVAL_CATEGORY_ID = "medieval"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedievalLevelScreen(
    navController: NavHostController,
    viewModel: LevelViewModel? = null
) {
    // Get the application context to access the repository if viewModel is not provided
    val context = LocalContext.current
    val application = context.applicationContext as ImperiumApplication
    
    // Use provided viewModel or create a new one with repository
    val levelViewModel = viewModel ?: viewModel(
        factory = LevelViewModel.Factory(application.repository)
    )
    
    // Theme colors
    val medievalPrimary = colorResource(R.color.medieval_button)
    val medievalBackground = colorResource(R.color.medieval_background_light)
    val medievalTextPrimary = colorResource(R.color.medieval_text_primary)
    val medievalGradientStart = colorResource(R.color.medieval_gradient_start)
    val medievalGradientEnd = colorResource(R.color.medieval_gradient_end)

    // State for level progress
    var levelProgressList by remember { mutableStateOf<List<LevelProgressEntity>>(emptyList()) }
    var totalStars by remember { mutableStateOf(0) }
    var categoryStars by remember { mutableStateOf(0) } // Category-specific stars
    var userProgress by remember { mutableStateOf<UserProgressEntity?>(null) }
    
    // Load level progress from repository
    LaunchedEffect(key1 = true) {
        // Get level progress and total stars
        levelViewModel.getLevelProgressForCategory(MEDIEVAL_CATEGORY_ID).collectLatest { progress ->
            levelProgressList = progress
            totalStars = levelViewModel.getTotalStars()
            
            // Calculate stars for this specific category
            categoryStars = progress.sumOf { it.starsEarned }
            
            // Alternative way to get category stars if needed
            // categoryStars = levelViewModel.getCategoryStars(MEDIEVAL_CATEGORY_ID)
            
            // Log to help debug
            Log.d("MedievalLevelScreen", "Category stars: $categoryStars, Total stars: $totalStars")
        }
        
        // Get user progress for unlocked levels
        levelViewModel.repository.getUserProgressForCategory(MEDIEVAL_CATEGORY_ID).collectLatest { progress ->
            userProgress = progress
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Medieval Period",
                            style = MaterialTheme.typography.titleLarge,
                            color = medievalTextPrimary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Stars",
                            tint = Color.Yellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$categoryStars",
                            style = MaterialTheme.typography.titleMedium,
                            color = medievalTextPrimary
                        )
                    }
                },
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
                            contentDescription = "Back to Home",
                            tint = medievalPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = medievalBackground
                )
            )
        },
        containerColor = medievalBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Map the MedievalLevels.levels to a list that can be displayed in a grid
            val levels = MedievalLevels.levels.map { medievalLevel ->
                val levelNumberInt = medievalLevel.id.toInt()
                val levelProgress = levelProgressList.find { 
                    it.categoryId == MEDIEVAL_CATEGORY_ID && 
                    it.levelNumber == levelNumberInt 
                }
                
                // Calculate if level is unlocked based on user progress - improved logic
                val isUnlocked = when {
                    // First level is always unlocked
                    levelNumberInt == 1 -> true
                    
                    // For all other levels, check:
                    // 1. User progress "unlockedLevels" field from database
                    // 2. Check if previous level is completed (starsEarned > 0)
                    else -> {
                        val unlockedLevels = userProgress?.unlockedLevels ?: 1
                        val previousLevelCompleted = levelProgressList.any { 
                            it.categoryId == MEDIEVAL_CATEGORY_ID && 
                            it.levelNumber == levelNumberInt - 1 && 
                            it.starsEarned > 0 
                        }
                        
                        // Level is unlocked if either:
                        // - It's marked as unlocked in user progress
                        // - OR the previous level is completed with at least 1 star
                        levelNumberInt <= unlockedLevels || previousLevelCompleted
                    }
                }
                
                // Debug log to see what's happening with each level
                Log.d("MedievalLevelScreen", "Level $levelNumberInt: unlocked=$isUnlocked, " +
                     "userProgressUnlocked=${userProgress?.unlockedLevels ?: 1}, " +
                     "previousLevelCompleted=${levelProgressList.any { 
                         it.categoryId == MEDIEVAL_CATEGORY_ID && 
                         it.levelNumber == levelNumberInt - 1 && 
                         it.starsEarned > 0 
                     }}")
                
                // Create a map of level information for the grid
                mapOf(
                    "levelNumber" to medievalLevel.id,
                    "title" to medievalLevel.title,
                    "description" to medievalLevel.description,
                    "isCompleted" to ((levelProgress?.starsEarned ?: 0) > 0),
                    "stars" to (levelProgress?.starsEarned ?: 0),
                    "requiredStars" to medievalLevel.requiredStars,
                    "isUnlocked" to isUnlocked
                )
            }
            
            // Display levels in a grid view like AncientLevelGrid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(levels) { level ->
                    LevelCard(
                        levelNumber = level["levelNumber"] as String,
                        isCompleted = level["isCompleted"] as Boolean,
                        stars = level["stars"] as Int,
                        requiredStars = level["requiredStars"] as Int,
                        isUnlocked = level["isUnlocked"] as Boolean,
                        primaryColor = medievalPrimary,
                        gradientStart = medievalGradientStart,
                        gradientEnd = medievalGradientEnd,
                        onClick = {
                            try {
                                if (level["isUnlocked"] as Boolean) {
                                    navController.navigate(NavDestinations.getMedievalQuizRoute(level["levelNumber"] as String))
                                }
                            } catch (e: Exception) {
                                // Prevent crash, just log error
                                Log.e("MedievalLevelScreen", "Error navigating to level: ${e.message}")
                            }
                        }
                    )
                }
            }
        }
    }
} 