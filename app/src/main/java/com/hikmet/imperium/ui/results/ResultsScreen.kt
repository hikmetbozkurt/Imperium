package com.hikmet.imperium.ui.results

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.ui.category.CategoryDetail
import com.hikmet.imperium.ui.category.categoryDetails
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.AncientGradientEnd
import com.hikmet.imperium.ui.theme.AncientGradientStart
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.MedievalGradientStart
import com.hikmet.imperium.ui.theme.ModernGradientStart
import com.hikmet.imperium.ui.theme.RenaissanceGradientStart
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart
import kotlinx.coroutines.delay

/**
 * Results screen showing quiz performance
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavHostController,
    categoryId: String?,
    levelId: String?,
    score: String?
) {
    // Use safe default values to avoid null issues
    val safeCategoryId = categoryId ?: "ancient"
    val safeLevelId = levelId ?: "1"
    val safeScore = score?.toIntOrNull() ?: 0
    
    // Get category details safely
    val categoryDetail = remember {
        categoryDetails[safeCategoryId] ?: categoryDetails["ancient"]!!
    }
    
    // Get gradient colors based on category
    val (gradientStart, gradientEnd) = remember {
        when (safeCategoryId) {
            "ancient" -> Pair(AncientGradientStart, AncientGradientEnd)
            "medieval" -> Pair(MedievalGradientStart, MedievalGradientStart.copy(alpha = 0.7f))
            "renaissance" -> Pair(RenaissanceGradientStart, RenaissanceGradientStart.copy(alpha = 0.7f))
            "modern" -> Pair(ModernGradientStart, ModernGradientStart.copy(alpha = 0.7f))
            "world_wars" -> Pair(WorldWarsGradientStart, WorldWarsGradientStart.copy(alpha = 0.7f))
            else -> Pair(AncientGradientStart, AncientGradientEnd)
        }
    }
    
    // Results title
    val resultsTitle = "${categoryDetail.title}: Level ${safeLevelId} Results"
    
    // Set fixed values for demo
    val totalQuestions = 10 // Hardcoded for demo
    
    // Calculate percentage
    val percentage = (safeScore.toFloat() / totalQuestions) * 100f
    
    // Determine stars based on percentage
    val stars = when {
        percentage >= 90 -> 3
        percentage >= 70 -> 2
        percentage >= 50 -> 1
        else -> 0
    }
    
    // Animation for score counter
    val animatedScore = remember { Animatable(0f) }
    val animatedPercentage = remember { Animatable(0f) }
    var displayedScore by remember { mutableIntStateOf(0) }
    var displayedPercentage by remember { mutableIntStateOf(0) }
    
    // Simplified animation logic
    LaunchedEffect(safeScore) {
        try {
            animatedScore.animateTo(
                targetValue = safeScore.toFloat(),
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
        } catch (e: Exception) {
            // Fallback if animation fails
            displayedScore = safeScore
        }
    }
    
    LaunchedEffect(percentage) {
        try {
            animatedPercentage.animateTo(
                targetValue = percentage,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
        } catch (e: Exception) {
            // Fallback if animation fails
            displayedPercentage = percentage.toInt()
        }
    }
    
    LaunchedEffect(animatedScore.value) {
        displayedScore = animatedScore.value.toInt()
    }
    
    LaunchedEffect(animatedPercentage.value) {
        displayedPercentage = animatedPercentage.value.toInt()
    }
    
    // Show message based on stars
    val performanceMessage = when(stars) {
        3 -> "Excellent!"
        2 -> "Great job!"
        1 -> "Good effort!"
        else -> "Keep practicing!"
    }
    
    // Accessibility description for results
    val resultsAccessibilityDescription = "$performanceMessage You scored $displayedScore out of $totalQuestions, " +
        "that's $displayedPercentage percent. You earned $stars ${if (stars == 1) "star" else "stars"}."
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        resultsTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = gradientStart,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                            tint = MaterialTheme.colorScheme.onPrimary
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            gradientStart.copy(alpha = 0.1f),
                            gradientEnd.copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .semantics { 
                        contentDescription = resultsAccessibilityDescription
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = performanceMessage,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Score circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .semantics { 
                            contentDescription = "Score: $displayedScore out of $totalQuestions"
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$displayedScore",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 48.sp
                            ),
                            color = gradientStart
                        )
                        
                        Text(
                            text = "out of $totalQuestions",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Text(
                            text = "$displayedPercentage%",
                            style = MaterialTheme.typography.bodyLarge,
                            color = gradientStart,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Stars
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.semantics { 
                        contentDescription = "$stars out of 3 stars earned"
                    }
                ) {
                    repeat(3) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = if (index < stars) "Earned star" else "Unearned star",
                            tint = if (index < stars) 
                                gradientStart
                            else 
                                Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Stats card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { 
                            contentDescription = "Statistics: $safeScore correct answers, ${totalQuestions - safeScore} incorrect answers, $stars stars earned"
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Stats",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatColumn(
                                value = "$safeScore",
                                label = "Correct Answers",
                                icon = Icons.Default.CheckCircle,
                                iconTint = Color.Green
                            )
                            
                            StatColumn(
                                value = "${totalQuestions - safeScore}",
                                label = "Incorrect Answers",
                                icon = Icons.Default.Replay,
                                iconTint = Color.Red.copy(alpha = 0.7f)
                            )
                            
                            StatColumn(
                                value = "$stars",
                                label = "Stars Earned",
                                icon = Icons.Default.Star,
                                iconTint = gradientStart
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Try again button
                    OutlinedButton(
                        onClick = {
                            // Simplified navigation logic to prevent crashes
                            try {
                                navController.popBackStack()
                            } catch (e: Exception) {
                                // Fallback navigation
                                try {
                                    navController.navigate("home")
                                } catch (e: Exception) {
                                    // Last resort - do nothing
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .semantics { 
                                contentDescription = "Try quiz again"
                            },
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Try Again"
                        )
                        
                        Spacer(modifier = Modifier.padding(4.dp))
                        
                        Text("Try Again")
                    }
                    
                    // Next level or continue button
                    Button(
                        onClick = {
                            // Simplified navigation logic to prevent crashes
                            try {
                                navController.popBackStack()
                            } catch (e: Exception) {
                                // Fallback navigation
                                try {
                                    navController.navigate("home")
                                } catch (e: Exception) {
                                    // Last resort - do nothing
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .semantics { 
                                contentDescription = if (stars > 0) "Go to next level" else "Return to levels"
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = gradientStart
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(
                            imageVector = if (stars > 0) Icons.Default.CheckCircle else Icons.Default.Home,
                            contentDescription = if (stars > 0) "Next Level" else "Home"
                        )
                        
                        Spacer(modifier = Modifier.padding(4.dp))
                        
                        Text(if (stars > 0) "Next Level" else "Back to Levels")
                    }
                }
            }
        }
    }
}

@Composable
fun StatColumn(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics { 
            contentDescription = "$value $label"
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label icon",
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenPreview() {
    ImperiumTheme {
        ResultsScreen(
            navController = rememberNavController(),
            categoryId = "ancient",
            levelId = "1",
            score = "7"
        )
    }
} 