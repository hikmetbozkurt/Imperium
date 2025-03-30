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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.ImperiumTheme
import kotlinx.coroutines.delay

/**
 * Results screen showing quiz performance
 */
@Composable
fun ResultsScreen(
    navController: NavHostController,
    categoryId: String?,
    levelId: String?,
    score: String?
) {
    // Parse score (would come from the quiz screen in real app)
    val scoreValue = score?.toIntOrNull() ?: 0
    val totalQuestions = 10 // Hardcoded for demo
    
    // Calculate percentage
    val percentage = (scoreValue.toFloat() / totalQuestions) * 100f
    
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
    
    LaunchedEffect(scoreValue) {
        animatedScore.animateTo(
            targetValue = scoreValue.toFloat(),
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
    }
    
    LaunchedEffect(percentage) {
        animatedPercentage.animateTo(
            targetValue = percentage,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
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
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Quiz Results",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
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
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "out of $totalQuestions",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Text(
                        text = "$displayedPercentage%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Stars
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star ${index + 1}",
                        tint = if (index < stars) 
                            MaterialTheme.colorScheme.secondary 
                        else 
                            Color.Gray.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Stats card
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            value = "$scoreValue",
                            label = "Correct Answers"
                        )
                        
                        StatColumn(
                            value = "${totalQuestions - scoreValue}",
                            label = "Incorrect Answers"
                        )
                        
                        StatColumn(
                            value = "$stars",
                            label = "Stars Earned"
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
                        navController.navigate("quiz/$categoryId/$levelId/STANDARD") {
                            popUpTo("results/$categoryId/$levelId/$score") { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f),
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
                        val nextLevelId = levelId?.toIntOrNull()?.plus(1) ?: 1
                        if (stars > 0) {
                            // Next level (if earned at least 1 star)
                            navController.navigate("level/$categoryId/$nextLevelId") {
                                popUpTo("results/$categoryId/$levelId/$score") { inclusive = true }
                            }
                        } else {
                            // Back to levels if failed
                            navController.navigate("level/$categoryId/1") {
                                popUpTo("results/$categoryId/$levelId/$score") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = if (stars > 0) Icons.Default.CheckCircle else Icons.Default.Home,
                        contentDescription = if (stars > 0) "Next Level" else "Home"
                    )
                    
                    Spacer(modifier = Modifier.padding(4.dp))
                    
                    Text(if (stars > 0) "Next Level" else "Levels")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Share button
            IconButton(
                onClick = { /* Share functionality */ },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Results",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

/**
 * Column for displaying a stat value and label
 */
@Composable
fun StatColumn(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
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