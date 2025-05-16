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
import com.hikmet.imperium.data.RenaissanceLevels
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.viewmodel.LevelViewModel
import com.hikmet.imperium.ImperiumApplication
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest
import android.util.Log
import com.hikmet.imperium.ui.theme.RenaissanceGradientStart
import com.hikmet.imperium.ui.theme.RenaissanceGradientEnd
import com.hikmet.imperium.ui.util.formatTime

private const val RENAISSANCE_CATEGORY_ID = "renaissance"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenaissanceLevelScreen(
    navController: NavHostController,
    viewModel: LevelViewModel? = null
) {
    // Get the application context to access the repository
    val context = LocalContext.current
    val application = context.applicationContext as ImperiumApplication
    
    // Use provided viewModel or create one
    val levelViewModel = viewModel ?: viewModel(
        factory = LevelViewModel.Factory(application.repository)
    )
    
    // State variables
    var levelProgressList by remember { mutableStateOf<List<LevelProgressEntity>>(emptyList()) }
    var userProgress by remember { mutableStateOf<UserProgressEntity?>(null) }
    var categoryStars by remember { mutableStateOf(0) }
    
    // Theme colors for Renaissance
    val renaissanceBackground = colorResource(id = R.color.background)
    val renaissancePrimary = RenaissanceGradientStart
    val renaissanceTextPrimary = colorResource(id = R.color.text_primary)
    val renaissanceGradientStart = RenaissanceGradientStart
    val renaissanceGradientEnd = RenaissanceGradientEnd
    
    // Load level progress data
    LaunchedEffect(key1 = RENAISSANCE_CATEGORY_ID) {
        try {
            // Fetch all level progress for this category
            levelViewModel.getLevelProgressForCategory(RENAISSANCE_CATEGORY_ID).collectLatest { progress ->
                levelProgressList = progress
                
                // Calculate total stars for the category
                categoryStars = progress.sumOf { it.starsEarned }
            }
            
            // Fetch user progress for unlocked levels
            levelViewModel.repository.getUserProgressForCategory(RENAISSANCE_CATEGORY_ID).collectLatest { progress ->
                userProgress = progress
            }
        } catch (e: Exception) {
            // Log error and prevent crash
            Log.e("RenaissanceLevelScreen", "Error loading progress: ${e.message}")
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
                            text = "Renaissance",
                            style = MaterialTheme.typography.titleLarge,
                            color = renaissanceTextPrimary
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
                            color = renaissanceTextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = renaissancePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = renaissanceBackground
                )
            )
        },
        containerColor = renaissanceBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Map the RenaissanceLevels.levels to a list that can be displayed in a grid
            val levels = RenaissanceLevels.levels.map { renaissanceLevel ->
                val levelNumberInt = renaissanceLevel.id.toInt()
                val levelProgress = levelProgressList.find { 
                    it.categoryId == RENAISSANCE_CATEGORY_ID && 
                    it.levelNumber == levelNumberInt 
                }
                
                // Calculate if level is unlocked based on user progress
                val isUnlocked = when {
                    // First level is always unlocked
                    levelNumberInt == 1 -> true
                    
                    // For all other levels, check:
                    // 1. User progress "unlockedLevels" field from database
                    // 2. Check if previous level is completed (starsEarned > 0)
                    else -> {
                        val unlockedLevels = userProgress?.unlockedLevels ?: 1
                        val previousLevelCompleted = levelProgressList.any { 
                            it.categoryId == RENAISSANCE_CATEGORY_ID && 
                            it.levelNumber == levelNumberInt - 1 && 
                            it.starsEarned > 0 
                        }
                        
                        // Level is unlocked if either condition is met
                        levelNumberInt <= unlockedLevels || previousLevelCompleted
                    }
                }
                
                // Create a map of level information for the grid
                mapOf(
                    "levelNumber" to renaissanceLevel.id,
                    "title" to renaissanceLevel.title,
                    "description" to renaissanceLevel.description,
                    "isCompleted" to ((levelProgress?.starsEarned ?: 0) > 0),
                    "stars" to (levelProgress?.starsEarned ?: 0),
                    "requiredStars" to renaissanceLevel.requiredStars,
                    "isUnlocked" to isUnlocked,
                    "bestTimeMs" to levelProgress?.bestTimeMs
                )
            }
            
            // Display levels in a grid view
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
                        primaryColor = renaissancePrimary,
                        gradientStart = renaissanceGradientStart,
                        gradientEnd = renaissanceGradientEnd,
                        bestTimeMs = level["bestTimeMs"] as Long?,
                        onClick = {
                            try {
                                if (level["isUnlocked"] as Boolean) {
                                    // Navigate to quiz for this level
                                    navController.navigate(
                                        NavDestinations.QUIZ_ROUTE
                                            .replace("{categoryId}", RENAISSANCE_CATEGORY_ID)
                                            .replace("{levelId}", level["levelNumber"] as String)
                                            .replace("{quizType}", "STANDARD")
                                    )
                                }
                            } catch (e: Exception) {
                                // Prevent crash, just log error
                                Log.e("RenaissanceLevelScreen", "Error navigating to level: ${e.message}")
                            }
                        }
                    )
                }
            }
        }
    }
} 