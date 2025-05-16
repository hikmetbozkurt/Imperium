package com.hikmet.imperium.ui.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hikmet.imperium.R
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.ui.components.LevelCard
import com.hikmet.imperium.ui.theme.ModernHistoryTheme
import com.hikmet.imperium.ui.viewmodel.LevelViewModel
import com.hikmet.imperium.ui.navigation.Screen

@Composable
fun ModernHistoryLevelScreen(
    navController: NavController,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val levelsUiState by viewModel.levelsUiState.collectAsState()
    
    // Load levels for this category when the screen is displayed
    LaunchedEffect(key1 = "modern") {
        viewModel.loadLevelsForCategory("modern")
    }

    ModernHistoryTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(levelsUiState.levels.filter { it.categoryId == "modern" }, key = { it.levelNumber }) { levelData ->
                val isUnlocked = levelsUiState.userProgress?.let {
                    levelData.levelNumber <= it.unlockedLevels || levelData.requiredStarsToUnlock <= it.totalStarsEarned
                } ?: (levelData.levelNumber == 1)

                LevelCard(
                    level = levelData,
                    isUnlocked = isUnlocked,
                    userStarsForLevel = levelsUiState.userProgress?.let {
                        viewModel.getStarsForLevel(it, "modern", levelData.levelNumber)
                    } ?: 0,
                    colors = ModernHistoryTheme.colors,
                    onClick = {
                        if (isUnlocked) {
                            navController.navigate("${Screen.Quiz.route}/modern/${levelData.levelNumber}")
                        }
                    }
                )
            }
        }
    }
} 