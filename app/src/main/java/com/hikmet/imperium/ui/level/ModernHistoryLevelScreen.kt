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
import com.hikmet.imperium.data.ModernHistoryLevels
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.viewmodel.LevelViewModel
import com.hikmet.imperium.ImperiumApplication
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest
import android.util.Log

private const val MODERN_CATEGORY_ID = "modern"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernHistoryLevelScreen(
    navController: NavHostController,
    viewModel: LevelViewModel? = null
) {
    val context = LocalContext.current
    val application = context.applicationContext as ImperiumApplication
    val levelViewModel = viewModel ?: viewModel(
        factory = LevelViewModel.Factory(application.repository)
    )

    val modernPrimary = colorResource(R.color.modern_button)
    val modernBackground = colorResource(R.color.modern_background_light)
    val modernTextPrimary = colorResource(R.color.modern_text_primary)
    val modernGradientStart = colorResource(R.color.modern_gradient_start)
    val modernGradientEnd = colorResource(R.color.modern_gradient_end)

    var levelProgressList by remember { mutableStateOf<List<LevelProgressEntity>>(emptyList()) }
    var totalStars by remember { mutableStateOf(0) }
    var categoryStars by remember { mutableStateOf(0) }
    var userProgress by remember { mutableStateOf<UserProgressEntity?>(null) }

    LaunchedEffect(key1 = true) {
        levelViewModel.getLevelProgressForCategory(MODERN_CATEGORY_ID).collectLatest { progress ->
            levelProgressList = progress
            totalStars = levelViewModel.getTotalStars()
            categoryStars = progress.sumOf { it.starsEarned }
            Log.d("ModernHistoryLevelScreen", "Category stars: $categoryStars, Total stars: $totalStars")
        }
        levelViewModel.repository.getUserProgressForCategory(MODERN_CATEGORY_ID).collectLatest { progress ->
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
                            text = "Modern History",
                            style = MaterialTheme.typography.titleLarge,
                            color = modernTextPrimary
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
                            color = modernTextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = modernPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = modernBackground
                )
            )
        },
        containerColor = modernBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val levels = ModernHistoryLevels.levels.map { modernLevel ->
                val levelNumberInt = modernLevel.id.toInt()
                val levelProgress = levelProgressList.find {
                    it.categoryId == MODERN_CATEGORY_ID &&
                    it.levelNumber == levelNumberInt
                }
                val isUnlocked = when {
                    levelNumberInt == 1 -> true
                    else -> {
                        val unlockedLevels = userProgress?.unlockedLevels ?: 1
                        val previousLevelCompleted = levelProgressList.any {
                            it.categoryId == MODERN_CATEGORY_ID &&
                            it.levelNumber == levelNumberInt - 1 &&
                            it.starsEarned > 0
                        }
                        levelNumberInt <= unlockedLevels || previousLevelCompleted
                    }
                }
                Log.d("ModernHistoryLevelScreen", "Level $levelNumberInt: unlocked=$isUnlocked, " +
                     "userProgressUnlocked=${userProgress?.unlockedLevels ?: 1}, " +
                     "previousLevelCompleted=${levelProgressList.any { 
                         it.categoryId == MODERN_CATEGORY_ID && 
                         it.levelNumber == levelNumberInt - 1 && 
                         it.starsEarned > 0 
                     }}")
                mapOf(
                    "levelNumber" to modernLevel.id,
                    "title" to modernLevel.title,
                    "description" to modernLevel.description,
                    "isCompleted" to ((levelProgress?.starsEarned ?: 0) > 0),
                    "stars" to (levelProgress?.starsEarned ?: 0),
                    "requiredStars" to modernLevel.requiredStars,
                    "isUnlocked" to isUnlocked
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
                        levelNumber = level["levelNumber"] as String,
                        isCompleted = level["isCompleted"] as Boolean,
                        stars = level["stars"] as Int,
                        requiredStars = level["requiredStars"] as Int,
                        isUnlocked = level["isUnlocked"] as Boolean,
                        primaryColor = modernPrimary,
                        gradientStart = modernGradientStart,
                        gradientEnd = modernGradientEnd,
                        onClick = {
                            try {
                                if (level["isUnlocked"] as Boolean) {
                                    navController.navigate(NavDestinations.getModernQuizRoute(level["levelNumber"] as String))
                                }
                            } catch (e: Exception) {
                                Log.e("ModernHistoryLevelScreen", "Error navigating to level: ${e.message}")
                            }
                        }
                    )
                }
            }
        }
    }
} 