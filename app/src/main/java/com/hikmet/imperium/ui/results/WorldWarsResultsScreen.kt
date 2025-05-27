package com.hikmet.imperium.ui.results

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hikmet.imperium.ImperiumApplication
import com.hikmet.imperium.R
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.viewmodel.LevelViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

private const val WORLD_WARS_CATEGORY_ID = "world_wars"
private const val MAX_WORLD_WARS_LEVELS = 20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldWarsResultsScreen(
    navController: NavHostController,
    levelId: String,
    score: Int,
    stars: Int,
    correctAnswers: Int,
    totalQuestions: Int,
    categoryId: String = WORLD_WARS_CATEGORY_ID,
    resultsViewModel: WorldWarsResultsViewModel = hiltViewModel()
) {
    // Get the application context to access the repository
    val context = LocalContext.current
    val application = context.applicationContext as ImperiumApplication
    
    // Initialize ViewModel with repository
    val levelViewModel: LevelViewModel = viewModel(
        factory = LevelViewModel.Factory(application.repository)
    )
    
    // Convert levelId to integer
    val levelNumber = levelId.toIntOrNull() ?: 1
    
    // Next level information
    val nextLevelNumber = if (levelNumber < MAX_WORLD_WARS_LEVELS) levelNumber + 1 else null
    var isNextLevelUnlocked by remember { mutableStateOf(false) }
    var requiredStarsForNextLevel by remember { mutableStateOf(0) }
    var totalStars by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    
    // Result saved to database state
    var resultSaved by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Animation states
    var showScore by remember { mutableStateOf(false) }
    var showStars by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(true) }
    
    // Animate score percentage
    val scorePercentage by animateFloatAsState(
        targetValue = if (showScore) score / 100f else 0f,
        animationSpec = tween(1000),
        label = "Score Animation"
    )
    
    // World Wars theme colors
    val primaryColor = colorResource(R.color.world_wars_button)
    val lightPrimaryColor = primaryColor.copy(alpha = 0.7f)
    val backgroundColor = colorResource(R.color.world_wars_background_light)
    val textPrimaryColor = colorResource(R.color.world_wars_text_primary)
    
    // Check if next level is unlocked and get requirements
    LaunchedEffect(levelId) {
        // Save result to database
        coroutineScope.launch {
            try {
                // DEBUGGING: Check what's in the database BEFORE we try to do anything
                val userProgressBefore = try {
                    levelViewModel.repository.getUserProgressForCategory(categoryId).first()
                } catch (e: Exception) {
                    null
                }
                
                Log.d("WORLD_WARS_RESULTS_DEBUG", "BEFORE: User progress for $categoryId: $userProgressBefore")
                Log.d("WORLD_WARS_RESULTS_DEBUG", "BEFORE: Unlocked levels: ${userProgressBefore?.unlockedLevels ?: 0}")
                
                // Update progress for this level
                val updateResult = levelViewModel.updateLevelProgress(
                    categoryId = categoryId,
                    levelNumber = levelNumber,
                    score = score,
                    stars = stars,
                    timeMs = null
                )
                Log.d("WORLD_WARS_RESULTS_DEBUG", "Level progress update result: $updateResult")
                
                // IMPORTANT: Unlock the next level if stars were earned
                if (stars > 0 && levelNumber < MAX_WORLD_WARS_LEVELS) {
                    val nextLevel = levelNumber + 1
                    
                    try {
                        // Directly unlock the next level
                        levelViewModel.repository.unlockLevel(categoryId, nextLevel)
                        Log.d("WORLD_WARS_RESULTS_DEBUG", "Unlocked next level: $nextLevel for category $categoryId")
                        
                        // Get user progress and make sure unlockedLevels is updated
                        val userProgress = levelViewModel.repository.getUserProgressForCategory(categoryId).first()
                        
                        // If unlockedLevels is less than nextLevel, update it
                        if (userProgress != null && userProgress.unlockedLevels < nextLevel) {
                            val updatedProgress = userProgress.copy(
                                unlockedLevels = maxOf(userProgress.unlockedLevels, nextLevel)
                            )
                            levelViewModel.repository.updateUserProgress(updatedProgress)
                            Log.d("WORLD_WARS_RESULTS_DEBUG", "Updated unlockedLevels from ${userProgress.unlockedLevels} to $nextLevel")
                        }
                    } catch (e: Exception) {
                        Log.e("WORLD_WARS_RESULTS_DEBUG", "Error unlocking next level: ${e.message}", e)
                    }
                }
                
                // DEBUGGING: Check what's in the database AFTER our changes
                val userProgressAfter = try {
                    levelViewModel.repository.getUserProgressForCategory(categoryId).first()
                } catch (e: Exception) {
                    null
                }
                
                Log.d("WORLD_WARS_RESULTS_DEBUG", "AFTER: User progress for $categoryId: $userProgressAfter")
                Log.d("WORLD_WARS_RESULTS_DEBUG", "AFTER: Unlocked levels: ${userProgressAfter?.unlockedLevels ?: 0}")
                
                resultSaved = true
                
                // Get total stars after update
                totalStars = levelViewModel.getCategoryStars(categoryId)
                
                // Check if next level exists and its requirements
                if (nextLevelNumber != null) {
                    // Get required stars for next level - this would come from your level data
                    // For now, we'll use a simple formula: 3 * levelNumber
                    requiredStarsForNextLevel = 3 * nextLevelNumber
                    
                    // Set next level as unlocked if enough stars
                    if (stars > 0) {
                        isNextLevelUnlocked = true
                    } else {
                        // Check database if it's already unlocked
                        isNextLevelUnlocked = levelViewModel.repository.isLevelUnlocked(
                            categoryId = categoryId, 
                            levelNumber = nextLevelNumber
                        )
                    }
                }
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(
                    message = "Couldn't save progress: ${e.message}",
                    duration = SnackbarDuration.Long,
                    withDismissAction = true
                )
            }
        }
    }
    
    // Animation sequence
    LaunchedEffect(Unit) {
        // Sequential animations for a pleasant reveal
        delay(300)
        showScore = true
        delay(1000)
        showStars = true
        delay(500)
        showStats = true
        delay(500)
        showButtons = true
    }

    // Results title
    val categoryTitle = "World Wars"
    val resultsTitle = "$categoryTitle: Level $levelId Results"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        resultsTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { 
                            resultsViewModel.soundManager.playButtonClick()
                            navController.navigate(NavDestinations.WORLD_WARS_LEVEL_ROUTE) {
                                popUpTo(NavDestinations.WORLD_WARS_LEVEL_ROUTE) {
                                    inclusive = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to World Wars Levels",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Score circle
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .size(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Background circle
                    Canvas(
                        modifier = Modifier.size(180.dp)
                    ) {
                        // Background circle (lighter color)
                        drawArc(
                            color = lightPrimaryColor.copy(alpha = 0.2f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 20f, cap = StrokeCap.Round)
                        )
                        
                        // Foreground progress arc
                        drawArc(
                            color = primaryColor,
                            startAngle = -90f,
                            sweepAngle = 360f * scorePercentage,
                            useCenter = false,
                            style = Stroke(width = 20f, cap = StrokeCap.Round)
                        )
                    }
                    
                    // Score text
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedVisibility(
                            visible = showScore,
                            enter = fadeIn(tween(500)) + scaleIn(tween(500))
                        ) {
                            Text(
                                text = "$score%",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                            )
                        }
                        
                        AnimatedVisibility(visible = showScore) {
                            Text(
                                text = "Score",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }
                
                // Performance message
                AnimatedVisibility(visible = showScore) {
                    Text(
                        text = when {
                            score >= 90 -> "Excellent!"
                            score >= 70 -> "Great job!"
                            score >= 50 -> "Good effort!"
                            else -> "Keep practicing!"
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when {
                                score >= 90 -> primaryColor
                                score >= 70 -> lightPrimaryColor
                                score >= 50 -> Color(0xFFFFC107) // Yellow
                                else -> Color(0xFFFF5722) // Orange
                            }
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                
                // Stars earned
                AnimatedVisibility(visible = showStars) {
                    Row(
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        repeat(3) { index ->
                            Icon(
                                imageVector = if (index < stars) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = if (index < stars) "Star earned" else "Star not earned",
                                tint = if (index < stars) Color(0xFFFFD700) else Color.LightGray,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
                
                // Statistics card
                AnimatedVisibility(visible = showStats) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Statistics",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Correct answers
                                StatColumn(
                                    icon = Icons.Filled.CheckCircle,
                                    iconTint = primaryColor,
                                    value = correctAnswers,
                                    label = "Correct"
                                )
                                
                                // Incorrect answers
                                StatColumn(
                                    icon = Icons.Outlined.Cancel,
                                    iconTint = Color(0xFFE57373),
                                    value = totalQuestions - correctAnswers,
                                    label = "Incorrect"
                                )
                                
                                // Stars earned
                                StatColumn(
                                    icon = Icons.Filled.Star,
                                    iconTint = Color(0xFFFFD700),
                                    value = stars,
                                    label = "Stars"
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Action buttons
                AnimatedVisibility(visible = showButtons) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Retry button
                        Button(
                            onClick = {
                                resultsViewModel.soundManager.playButtonClick()
                                navController.navigate(NavDestinations.getWorldWarsQuizRoute(levelId))
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Retry quiz"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retry")
                        }
                        
                        // Next button or Home button
                        if (stars > 0 && nextLevelNumber != null && nextLevelNumber <= MAX_WORLD_WARS_LEVELS) {
                            Button(
                                onClick = {
                                    resultsViewModel.soundManager.playButtonClick()
                                    navController.navigate(NavDestinations.getWorldWarsQuizRoute(nextLevelNumber.toString()))
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Next Level")
                            }
                        } else {
                            Button(
                                onClick = {
                                    resultsViewModel.soundManager.playButtonClick()
                                    navController.navigate(NavDestinations.WORLD_WARS_LEVEL_ROUTE) {
                                        popUpTo(NavDestinations.WORLD_WARS_LEVEL_ROUTE) {
                                            inclusive = false
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Go to levels"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Levels")
                            }
                        }
                    }
                }
            }
        }
    }
} 