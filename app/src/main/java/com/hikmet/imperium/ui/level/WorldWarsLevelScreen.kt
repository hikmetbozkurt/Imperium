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
import com.hikmet.imperium.data.WorldWarsLevels
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.viewmodel.LevelViewModel
import com.hikmet.imperium.ImperiumApplication
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest
import android.util.Log
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart
import com.hikmet.imperium.ui.theme.WorldWarsGradientEnd
import com.hikmet.imperium.ui.theme.WorldWarsTheme
import com.hikmet.imperium.ui.util.formatTime
import androidx.hilt.navigation.compose.hiltViewModel
import com.hikmet.imperium.ui.components.LevelCard

private const val WORLD_WARS_CATEGORY_ID = "world_wars"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldWarsLevelScreen(
    navController: NavHostController,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val categoryId = WORLD_WARS_CATEGORY_ID
    val uiState by viewModel.levelsUiState.collectAsState()
    LaunchedEffect(categoryId) {
        viewModel.loadLevelsForCategory(categoryId)
    }

    // Theme colors for World Wars
    val background = colorResource(id = R.color.background)
    val primary = WorldWarsGradientStart
    val textPrimary = colorResource(id = R.color.text_primary)
    val gradientStart = WorldWarsGradientStart
    val gradientEnd = WorldWarsGradientEnd

    WorldWarsTheme {
        Scaffold(
            containerColor = background,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "World Wars",
                                style = MaterialTheme.typography.titleLarge,
                                color = textPrimary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = uiState.userProgress?.totalStarsEarned?.toString() ?: "0",
                                style = MaterialTheme.typography.titleMedium,
                                color = textPrimary
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = background
                    )
                )
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.levels.filter { it.categoryId == categoryId }, key = { it.levelNumber }) { level ->
                    val isUnlocked = uiState.userProgress?.let {
                        level.levelNumber <= it.unlockedLevels || level.requiredStarsToUnlock <= it.totalStarsEarned
                    } ?: (level.levelNumber == 1)
                    LevelCard(
                        level = level,
                        isUnlocked = isUnlocked,
                        userStarsForLevel = uiState.userProgress?.let { viewModel.getStarsForLevel(it, categoryId, level.levelNumber) } ?: 0,
                        colors = WorldWarsTheme.colors,
                        onClick = {
                            if (isUnlocked) {
                                navController.navigate(
                                    NavDestinations.QUIZ_ROUTE
                                        .replace("{categoryId}", categoryId)
                                        .replace("{levelId}", level.levelNumber.toString())
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