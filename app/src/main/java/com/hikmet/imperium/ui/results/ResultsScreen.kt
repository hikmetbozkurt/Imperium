package com.hikmet.imperium.ui.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.AncientGradientEnd
import com.hikmet.imperium.ui.theme.AncientGradientStart
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.MedievalGradientStart
import com.hikmet.imperium.ui.theme.ModernGradientStart
import com.hikmet.imperium.ui.theme.RenaissanceGradientStart
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart
import com.hikmet.imperium.ui.util.formatTime
import kotlinx.coroutines.delay

/**
 * Screen to display results of a completed quiz
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavHostController,
    categoryId: String,
    levelId: String,
    score: Int,
    stars: Int,
    correctAnswers: Int,
    totalQuestions: Int
) {
    // Animation states
    var showScore by remember { mutableStateOf(false) }
    var showStars by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }
    
    // Animate score percentage
    val scorePercentage by animateFloatAsState(
        targetValue = if (showScore) score / 100f else 0f,
        animationSpec = tween(1000),
        label = "Score Animation"
    )
    
    // Ancient Civilizations theme colors
    val ancientTeal = Color(0xFF227C70)
    val ancientLightTeal = Color(0xFF39AEA9)
    
    // Safe defaults for navigation
    val safeLevel = levelId.toIntOrNull() ?: 1
    val nextLevel = safeLevel + 1
    
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
    
    // Get category details safely
    val categoryDetail = remember {
        when (categoryId) {
            "ancient" -> "Ancient Civilizations"
            "medieval" -> "Medieval Period"
            "renaissance" -> "Renaissance"
            "modern" -> "Modern History"
            "world_wars" -> "World Wars"
            else -> "Ancient Civilizations"
        }
    }
    
    // Results title
    val resultsTitle = "$categoryDetail: Level ${safeLevel} Results"
    
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
                    containerColor = ancientTeal
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { 
                            // Simplify navigation to avoid crashes
                            try {
                                navController.popBackStack()
                            } catch (e: Exception) {
                                // If pop fails, try explicit navigation
                                try {
                                    navController.navigate("home")
                                } catch (e: Exception) {
                                    // Last resort, do nothing and let user press back button
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
        }
    ) { paddingValues ->
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .semantics { 
                        contentDescription = "Results screen content"
                    },
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
                            color = ancientLightTeal.copy(alpha = 0.2f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 20f, cap = StrokeCap.Round)
                        )
                        
                        // Foreground progress arc
                        drawArc(
                            color = ancientTeal,
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
                                    color = ancientTeal
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
                                score >= 90 -> ancientTeal // Use Ancient teal for consistency
                                score >= 70 -> ancientLightTeal
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
                                tint = if (index < stars) Color(0xFFFFD700) else Color.LightGray, // Gold color for earned stars
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
                                    iconTint = ancientTeal,
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
                                // Navigate back to the quiz for this level
                                navController.navigate(
                                    NavDestinations.QUIZ_ROUTE
                                        .replace("{categoryId}", categoryId)
                                        .replace("{levelId}", levelId)
                                        .replace("{quizType}", "STANDARD")
                                )
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
                        
                        // Next button - show only if enough stars were earned
                        if (stars > 0) {
                            Button(
                                onClick = {
                                    if (stars > 0) {
                                        // Navigate to the next level
                                        navController.navigate(
                                            NavDestinations.QUIZ_ROUTE
                                                .replace("{categoryId}", categoryId)
                                                .replace("{levelId}", nextLevel.toString())
                                                .replace("{quizType}", "STANDARD")
                                        )
                                    } else {
                                        // Not enough stars, go back to level selection
                                        navController.navigate(
                                            NavDestinations.LEVEL_SELECTION_ROUTE
                                                .replace("{categoryId}", categoryId)
                                        ) {
                                            popUpTo(NavDestinations.LEVEL_SELECTION_ROUTE) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ancientTeal
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Next Level")
                            }
                        } else {
                            // Home button if no stars were earned
                            Button(
                                onClick = {
                                    // Navigate to level selection
                                    navController.navigate(
                                        NavDestinations.LEVEL_SELECTION_ROUTE
                                            .replace("{categoryId}", categoryId)
                                    ) {
                                        popUpTo(NavDestinations.LEVEL_SELECTION_ROUTE) {
                                            inclusive = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ancientTeal
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

/**
 * Column with icon, value and label for statistics
 */
@Composable
fun StatColumn(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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

@Preview
@Composable
fun ResultsScreenPreview() {
    ImperiumTheme {
        ResultsScreen(
            navController = rememberNavController(),
            categoryId = "ancient",
            levelId = "1",
            score = 75,
            stars = 2,
            correctAnswers = 3,
            totalQuestions = 4
        )
    }
} 