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

private const val MEDIEVAL_CATEGORY_ID = "medieval"
private const val MAX_MEDIEVAL_LEVELS = 20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedievalResultsScreen(
    navController: NavHostController,
    levelId: String,
    score: Int,
    stars: Int,
    correctAnswers: Int,
    totalQuestions: Int,
    categoryId: String = MEDIEVAL_CATEGORY_ID
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
    val nextLevelNumber = if (levelNumber < MAX_MEDIEVAL_LEVELS) levelNumber + 1 else null
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
    
    // Theme colors based on category
    val primaryColor = when (categoryId) {
        "ancient" -> colorResource(R.color.ancient_button)
        else -> colorResource(R.color.medieval_button)
    }
    
    val lightPrimaryColor = primaryColor.copy(alpha = 0.7f)
    
    val backgroundColor = when (categoryId) {
        "ancient" -> colorResource(R.color.ancient_background_light)
        else -> colorResource(R.color.medieval_background_light)
    }
    
    val textPrimaryColor = when (categoryId) {
        "ancient" -> colorResource(R.color.ancient_text_primary)
        else -> colorResource(R.color.medieval_text_primary)
    }
    
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
                
                Log.d("RESULTS_DEBUG", "BEFORE: User progress for $categoryId: $userProgressBefore")
                Log.d("RESULTS_DEBUG", "BEFORE: Unlocked levels: ${userProgressBefore?.unlockedLevels ?: 0}")
                
                // No longer forcing level unlocks here - it's now handled properly after updating progress
                
                // Update progress for this level
                val updateResult = levelViewModel.updateLevelProgress(
                    categoryId = categoryId,
                    levelNumber = levelNumber,
                    score = score,
                    stars = stars,
                    timeMs = null
                )
                Log.d("RESULTS_DEBUG", "Level progress update result: $updateResult")
                
                // IMPORTANT: Unlock the next level if stars were earned
                if (stars > 0 && levelNumber < MAX_MEDIEVAL_LEVELS) {
                    val nextLevel = levelNumber + 1
                    
                    try {
                        // Directly unlock the next level
                        levelViewModel.repository.unlockLevel(categoryId, nextLevel)
                        Log.d("RESULTS_DEBUG", "Unlocked next level: $nextLevel for category $categoryId")
                        
                        // Get user progress and make sure unlockedLevels is updated
                        val userProgress = levelViewModel.repository.getUserProgressForCategory(categoryId).first()
                        
                        // If unlockedLevels is less than nextLevel, update it
                        if (userProgress != null && userProgress.unlockedLevels < nextLevel) {
                            val updatedProgress = userProgress.copy(
                                unlockedLevels = maxOf(userProgress.unlockedLevels, nextLevel)
                            )
                            levelViewModel.repository.updateUserProgress(updatedProgress)
                            Log.d("RESULTS_DEBUG", "Updated unlockedLevels from ${userProgress.unlockedLevels} to $nextLevel")
                        }
                    } catch (e: Exception) {
                        Log.e("RESULTS_DEBUG", "Error unlocking next level: ${e.message}", e)
                    }
                }
                
                // DEBUGGING: Check what's in the database AFTER our changes
                val userProgressAfter = try {
                    levelViewModel.repository.getUserProgressForCategory(categoryId).first()
                } catch (e: Exception) {
                    null
                }
                
                Log.d("RESULTS_DEBUG", "AFTER: User progress for $categoryId: $userProgressAfter")
                Log.d("RESULTS_DEBUG", "AFTER: Unlocked levels: ${userProgressAfter?.unlockedLevels ?: 0}")
                
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
    val categoryTitle = when (categoryId) {
        "ancient" -> "Ancient Civilizations"
        else -> "Medieval Period"
    }
    val resultsTitle = "$categoryTitle: Level $levelId Results"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = resultsTitle,
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
                            // Navigate directly to level selection screen
                            navController.navigate(
                                NavDestinations.LEVEL_SELECTION_ROUTE.replace("{categoryId}", categoryId)
                            ) {
                                // Clear back stack up to level selection
                                popUpTo(NavDestinations.LEVEL_SELECTION_ROUTE.replace("{categoryId}", categoryId)) {
                                    inclusive = false
                                }
                            }
                        },
                        modifier = Modifier.semantics { 
                            contentDescription = "Back to levels"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Levels",
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
                .background(Color.White)
        ) {
            // Scrollable content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 50.dp) // Reduced bottom padding from 65dp to 50dp
                    .semantics { 
                        contentDescription = "Results screen content"
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Score circle
                    Box(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background circle
                        Canvas(
                            modifier = Modifier.size(160.dp)
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
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    // Stars earned
                    AnimatedVisibility(visible = showStars) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                repeat(3) { index ->
                                    Icon(
                                        imageVector = if (index < stars) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = if (index < stars) "Star earned" else "Star not earned",
                                        tint = if (index < stars) Color(0xFFFFD700) else Color.LightGray, // Gold color for earned stars
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(horizontal = 4.dp)
                                    )
                                }
                            }
                            
                            // Show message when no stars earned
                            if (stars == 0) {
                                Text(
                                    text = "You need at least 1 star to unlock the next level",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFFF5722), // Orange error color
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
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
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    // Correct answers
                                    MedievalStatColumn(
                                        icon = Icons.Filled.CheckCircle,
                                        iconTint = primaryColor,
                                        value = correctAnswers,
                                        label = "Correct"
                                    )
                                    
                                    // Incorrect answers
                                    MedievalStatColumn(
                                        icon = Icons.Outlined.Cancel,
                                        iconTint = Color(0xFFE57373),
                                        value = totalQuestions - correctAnswers,
                                        label = "Incorrect"
                                    )
                                    
                                    // Stars earned
                                    MedievalStatColumn(
                                        icon = Icons.Filled.Star,
                                        iconTint = Color(0xFFFFD700),
                                        value = stars,
                                        label = "Stars"
                                    )
                                }
                                
                                // Show total stars earned in category if available
                                if (resultSaved) {
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Total $categoryTitle Stars",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = textPrimaryColor
                                        )
                                        
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFD700),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "$totalStars",
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = textPrimaryColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Fixed buttons at bottom
            AnimatedVisibility(
                visible = true, // Always show buttons
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp) // Just padding for positioning
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 8.dp), // Padding inside the white background
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Retry button
                    Button(
                        onClick = {
                            // Navigate back to the quiz for this level
                            if (categoryId == "ancient") {
                                navController.navigate(
                                    NavDestinations.getAncientQuizRoute(levelId)
                                )
                            } else {
                                navController.navigate(
                                    NavDestinations.getMedievalQuizRoute(levelId)
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(40.dp), // Restore to original 40dp height
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
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Retry")
                    }
                    
                    // Home button renamed to Levels
                    Button(
                        onClick = {
                            // Navigate directly to level selection
                            navController.navigate(
                                NavDestinations.LEVEL_SELECTION_ROUTE.replace("{categoryId}", categoryId)
                            ) {
                                // Clear back stack
                                popUpTo(NavDestinations.LEVEL_SELECTION_ROUTE.replace("{categoryId}", categoryId)) {
                                    inclusive = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC107), // Amber/Gold color
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Go to levels"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Levels")
                    }
                    
                    // Next Level button - conditionally shown
                    Button(
                        onClick = {
                            if (levelNumber < MAX_MEDIEVAL_LEVELS && stars > 0) {
                                // If stars earned, go to next level
                                val nextLevel = levelNumber + 1
                                
                                // Navigate to the next level quiz
                                if (categoryId == "ancient") {
                                    navController.navigate(
                                        NavDestinations.getAncientQuizRoute(nextLevel.toString())
                                    )
                                } else {
                                    navController.navigate(
                                        NavDestinations.getMedievalQuizRoute(nextLevel.toString())
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(40.dp), // Restore to original 40dp height
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        // Only enable if there's a next level and stars were earned
                        enabled = levelNumber < MAX_MEDIEVAL_LEVELS && stars > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next level"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Next")
                    }
                }
            }
        }
    }
}

/**
 * Column with icon, value and label for statistics
 */
@Composable
fun MedievalStatColumn(
    icon: ImageVector,
    iconTint: Color,
    value: Int,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
} 