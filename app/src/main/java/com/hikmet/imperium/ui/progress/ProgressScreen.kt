package com.hikmet.imperium.ui.progress

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.home.HistoryCategory
import com.hikmet.imperium.ui.home.sampleCategories
import com.hikmet.imperium.ui.theme.Accent
import com.hikmet.imperium.ui.theme.Highlight
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.Primary
import com.hikmet.imperium.ui.theme.Secondary
import kotlin.math.min

/**
 * User progress statistics
 */
data class UserProgress(
    val totalQuizzesTaken: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val averageAccuracy: Float, // percentage
    val completedLevels: Int,
    val earnedStars: Int,
    val categoryProgress: List<Pair<String, Float>> // category name and completion percentage
)

/**
 * Sample progress data for preview
 */
val sampleProgress = UserProgress(
    totalQuizzesTaken = 48,
    correctAnswers = 356,
    incorrectAnswers = 124,
    averageAccuracy = 74.2f,
    completedLevels = 15,
    earnedStars = 32,
    categoryProgress = listOf(
        "Ancient Civilizations" to 0.75f,
        "Medieval Period" to 0.3f,
        "Renaissance" to 0.0f,
        "Modern History" to 0.1f,
        "World Wars" to 0.5f
    )
)

/**
 * Progress screen showing user statistics
 */
@Composable
fun ProgressScreen(
    navController: NavHostController,
    progress: UserProgress = sampleProgress
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overall", "Categories", "Time Attack")
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                indicator = { tabPositions ->
                    // Custom indicator implementation if desired
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            
            // Content based on selected tab
            when (selectedTabIndex) {
                0 -> OverallProgressTab(progress)
                1 -> CategoryProgressTab(progress)
                2 -> TimeAttackTab(progress)
            }
        }
    }
}

/**
 * Overall progress tab
 */
@Composable
fun OverallProgressTab(progress: UserProgress) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Circular progress chart
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AccuracyChart(
                accuracy = progress.averageAccuracy / 100f,
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Stats cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Total Quizzes",
                value = "${progress.totalQuizzesTaken}",
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Levels Completed",
                value = "${progress.completedLevels}",
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Correct Answers",
                value = "${progress.correctAnswers}",
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Stars Earned",
                value = "${progress.earnedStars}",
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Recent activity
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
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Sample recent activities
                ActivityItem(
                    category = "Ancient Civilizations",
                    level = "Level 5",
                    score = "8/10",
                    timeAgo = "2 hours ago"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                ActivityItem(
                    category = "Medieval Period",
                    level = "Level 3",
                    score = "6/10",
                    timeAgo = "Yesterday"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                ActivityItem(
                    category = "World Wars",
                    level = "Level 1",
                    score = "9/10",
                    timeAgo = "3 days ago"
                )
            }
        }
    }
}

/**
 * Category progress tab
 */
@Composable
fun CategoryProgressTab(progress: UserProgress) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Category Completion",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Category progress bars
        progress.categoryProgress.forEach { (category, completion) ->
            CategoryProgressBar(
                category = category,
                progress = completion
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Category stats
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
                    text = "Category Stats",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Multi-colored bar chart (simplified)
                CategoryComparisonChart(progress = progress)
            }
        }
    }
}

/**
 * Time attack tab
 */
@Composable
fun TimeAttackTab(progress: UserProgress) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Time Attack Performance",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Simplified time attack stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Best Time",
                value = "2m 34s",
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Avg. Time per Question",
                value = "15.2s",
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Time attack history (simplified)
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
                    text = "Time Attack History",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Simulated time attack history
                TimeAttackHistoryItem(
                    category = "Ancient Civilizations",
                    score = "7/10",
                    time = "3m 12s"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                TimeAttackHistoryItem(
                    category = "World Wars",
                    score = "9/10",
                    time = "2m 54s"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                TimeAttackHistoryItem(
                    category = "Medieval Period",
                    score = "8/10",
                    time = "3m 05s"
                )
            }
        }
    }
}

/**
 * Circular chart showing accuracy
 */
@Composable
fun AccuracyChart(
    accuracy: Float,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = min(canvasWidth, canvasHeight) / 2
            val strokeWidth = radius * 0.2f
            
            // Background circle
            drawArc(
                color = MaterialTheme.colorScheme.surfaceVariant,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(
                    (canvasWidth - radius * 2) / 2,
                    (canvasHeight - radius * 2) / 2
                ),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
            
            // Progress arc
            drawArc(
                color = Primary,
                startAngle = -90f,
                sweepAngle = 360f * accuracy,
                useCenter = false,
                topLeft = Offset(
                    (canvasWidth - radius * 2) / 2,
                    (canvasHeight - radius * 2) / 2
                ),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(accuracy * 100).toInt()}%",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
            
            Text(
                text = "Accuracy",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Bar chart showing category progress comparison
 */
@Composable
fun CategoryComparisonChart(progress: UserProgress) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ColorLegendItem(color = Primary, label = "Ancient")
            ColorLegendItem(color = Secondary, label = "Medieval")
            ColorLegendItem(color = Accent, label = "Renaissance")
            ColorLegendItem(color = Highlight, label = "Modern")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Simplified bar chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / 6
            val maxBarHeight = canvasHeight * 0.9f
            
            // Draw bars (simplified)
            val colors = listOf(Primary, Secondary, Accent, Highlight, Primary)
            val heights = progress.categoryProgress.map { it.second * maxBarHeight }
            
            heights.forEachIndexed { index, height ->
                if (index < colors.size) {
                    drawRect(
                        color = colors[index],
                        topLeft = Offset(
                            (index + 1) * barWidth - barWidth * 0.8f,
                            canvasHeight - height
                        ),
                        size = androidx.compose.ui.geometry.Size(
                            barWidth * 0.6f,
                            height
                        )
                    )
                }
            }
        }
    }
}

/**
 * Category progress bar
 */
@Composable
fun CategoryProgressBar(
    category: String,
    progress: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Primary)
            )
        }
    }
}

/**
 * Stats card for displaying a key metric
 */
@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Recent activity item
 */
@Composable
fun ActivityItem(
    category: String,
    level: String,
    score: String,
    timeAgo: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(32.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$category - $level",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "Score: $score • $timeAgo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Time attack history item
 */
@Composable
fun TimeAttackHistoryItem(
    category: String,
    score: String,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Secondary
        )
    }
}

/**
 * Color legend item
 */
@Composable
fun ColorLegendItem(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    ImperiumTheme {
        ProgressScreen(rememberNavController())
    }
} 