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

private const val RENAISSANCE_CATEGORY_ID = "renaissance"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenaissanceLevelScreen(
    navController: NavHostController,
    viewModel: LevelViewModel? = null
) {
    val context = LocalContext.current
    val application = context.applicationContext as ImperiumApplication
    val levelViewModel = viewModel ?: viewModel(
        factory = LevelViewModel.Factory(application.repository)
    )

    val renaissancePrimary = colorResource(R.color.renaissance_button)
    val renaissanceBackground = colorResource(R.color.renaissance_background_light)
    val renaissanceTextPrimary = colorResource(R.color.renaissance_text_primary)
    val renaissanceGradientStart = colorResource(R.color.renaissance_gradient_start)
    val renaissanceGradientEnd = colorResource(R.color.renaissance_gradient_end)

    var levelProgressList by remember { mutableStateOf<List<LevelProgressEntity>>(emptyList()) }
    var totalStars by remember { mutableStateOf(0) }
    var categoryStars by remember { mutableStateOf(0) }
    var userProgress by remember { mutableStateOf<UserProgressEntity?>(null) }

    LaunchedEffect(key1 = true) {
        levelViewModel.getLevelProgressForCategory(RENAISSANCE_CATEGORY_ID).collectLatest { progress ->
            levelProgressList = progress
            totalStars = levelViewModel.getTotalStars()
            categoryStars = progress.sumOf { it.starsEarned }
            Log.d("RenaissanceLevelScreen", "Category stars: $categoryStars, Total stars: $totalStars")
        }
        levelViewModel.repository.getUserProgressForCategory(RENAISSANCE_CATEGORY_ID).collectLatest { progress ->
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
            val levels = RenaissanceLevels.levels.map { renaissanceLevel ->
                val levelNumberInt = renaissanceLevel.id.toInt()
                val levelProgress = levelProgressList.find {
                    it.categoryId == RENAISSANCE_CATEGORY_ID &&
                    it.levelNumber == levelNumberInt
                }
                val isUnlocked = when {
                    levelNumberInt == 1 -> true
                    else -> {
                        val unlockedLevels = userProgress?.unlockedLevels ?: 1
                        val previousLevelCompleted = levelProgressList.any {
                            it.categoryId == RENAISSANCE_CATEGORY_ID &&
                            it.levelNumber == levelNumberInt - 1 &&
                            it.starsEarned > 0
                        }
                        levelNumberInt <= unlockedLevels || previousLevelCompleted
                    }
                }
                Log.d("RenaissanceLevelScreen", "Level $levelNumberInt: unlocked=$isUnlocked, " +
                     "userProgressUnlocked=${userProgress?.unlockedLevels ?: 1}, " +
                     "previousLevelCompleted=${levelProgressList.any { 
                         it.categoryId == RENAISSANCE_CATEGORY_ID && 
                         it.levelNumber == levelNumberInt - 1 && 
                         it.starsEarned > 0 
                     }}")
                mapOf(
                    "levelNumber" to renaissanceLevel.id,
                    "title" to renaissanceLevel.title,
                    "description" to renaissanceLevel.description,
                    "isCompleted" to ((levelProgress?.starsEarned ?: 0) > 0),
                    "stars" to (levelProgress?.starsEarned ?: 0),
                    "requiredStars" to renaissanceLevel.requiredStars,
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
                        primaryColor = renaissancePrimary,
                        gradientStart = renaissanceGradientStart,
                        gradientEnd = renaissanceGradientEnd,
                        onClick = {
                            try {
                                if (level["isUnlocked"] as Boolean) {
                                    navController.navigate(NavDestinations.getRenaissanceQuizRoute(level["levelNumber"] as String))
                                }
                            } catch (e: Exception) {
                                Log.e("RenaissanceLevelScreen", "Error navigating to level: ${e.message}")
                            }
                        }
                    )
                }
            }
        }
    }
} 