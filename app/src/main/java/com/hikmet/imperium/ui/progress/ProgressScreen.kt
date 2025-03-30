package com.hikmet.imperium.ui.progress

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import kotlinx.coroutines.delay

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
    val tabs = listOf(
        TabItem("Overall", Icons.Default.Timeline),
        TabItem("Categories", Icons.Default.CheckCircle),
        TabItem("Time Attack", Icons.Default.EmojiEvents)
    )
    
    // Animation states for each tab
    val tabStates = remember {
        List(tabs.size) { MutableTransitionState(false).apply { targetState = false } }
    }
    
    // Trigger animation for the selected tab
    LaunchedEffect(selectedTabIndex) {
        tabStates.forEachIndexed { index, state ->
            state.targetState = false
        }
        delay(100)
        tabStates[selectedTabIndex].targetState = true
    }
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header with title
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Your Progress",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    
                    Text(
                        text = "Track your learning journey",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
            
            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                divider = {}
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (selectedTabIndex == index) 
                                            FontWeight.Bold 
                                        else 
                                            FontWeight.Normal
                                    )
                                )
                            }
                        }
                    )
                }
            }
            
            // Content based on selected tab
            when (selectedTabIndex) {
                0 -> OverallProgressTab(progress, tabStates[0])
                1 -> CategoryProgressTab(progress, tabStates[1])
                2 -> TimeAttackTab(progress, tabStates[2])
            }
        }
    }
}

data class TabItem(val title: String, val icon: ImageVector)

/**
 * Overall progress tab
 */
@Composable
fun OverallProgressTab(progress: UserProgress, animationState: MutableTransitionState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Animated container for accuracy chart
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(500)) +
                    slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        initialOffsetY = { -it }
                    )
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Accuracy",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Circular progress chart
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AccuracyChart(
                            accuracy = progress.averageAccuracy / 100f,
                            modifier = Modifier
                                .size(220.dp)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Stats cards with animation
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(500, delayMillis = 300)) +
                    slideInHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        initialOffsetX = { it / 2 }
                    )
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Total Quizzes",
                        value = "${progress.totalQuizzesTaken}",
                        icon = Icons.Default.Quiz,
                        iconTint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatCard(
                        title = "Levels Completed",
                        value = "${progress.completedLevels}",
                        icon = Icons.Default.CheckCircle,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Correct Answers",
                        value = "${progress.correctAnswers}",
                        icon = Icons.Default.CheckCircle,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatCard(
                        title = "Stars Earned",
                        value = "${progress.earnedStars}",
                        icon = Icons.Default.Star,
                        iconTint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Recent activity with animation
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(500, delayMillis = 600)) +
                    slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        initialOffsetY = { it / 2 }
                    )
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Recent Activity",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Use the composable function for sample activities
                    RecentActivityList()
                }
            }
        }
    }
}

/**
 * Composable function to display recent activities
 */
@Composable
private fun RecentActivityList() {
    // Sample recent activities
    ActivityItem(
        category = "Ancient Civilizations",
        level = "Level 5",
        score = "8/10",
        timeAgo = "2 hours ago"
    )
    
    Divider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
    
    ActivityItem(
        category = "Medieval Period",
        level = "Level 3",
        score = "6/10",
        timeAgo = "Yesterday"
    )
    
    Divider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
    
    ActivityItem(
        category = "World Wars",
        level = "Level 1",
        score = "9/10",
        timeAgo = "3 days ago"
    )
}

/**
 * Category progress tab
 */
@Composable
fun CategoryProgressTab(progress: UserProgress, animationState: MutableTransitionState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Animated heading
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(300)) +
                    slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { -it / 2 }
                    )
        ) {
            Text(
                text = "Category Completion",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Use RenderCategoryProgressBars instead of direct calls
        RenderCategoryProgressBars(progress)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Category stats with animation
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(500, delayMillis = 600)) +
                    slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        initialOffsetY = { it / 2 }
                    )
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Category Stats",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Multi-colored bar chart
                    CategoryComparisonChart(progress = progress)
                }
            }
        }
    }
}

/**
 * Composable function to render all category progress bars
 */
@Composable
private fun RenderCategoryProgressBars(progress: UserProgress) {
    // Ancient civilizations progress
    CategoryProgressBar(
        category = "Ancient Civilizations",
        progress = progress.categoryProgress.find { it.first == "Ancient Civilizations" }?.second ?: 0f
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    // Medieval progress
    CategoryProgressBar(
        category = "Medieval Period",
        progress = progress.categoryProgress.find { it.first == "Medieval Period" }?.second ?: 0f
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    // Renaissance progress
    CategoryProgressBar(
        category = "Renaissance",
        progress = progress.categoryProgress.find { it.first == "Renaissance" }?.second ?: 0f
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    // Modern history progress
    CategoryProgressBar(
        category = "Modern History",
        progress = progress.categoryProgress.find { it.first == "Modern History" }?.second ?: 0f
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    // World Wars progress
    CategoryProgressBar(
        category = "World Wars",
        progress = progress.categoryProgress.find { it.first == "World Wars" }?.second ?: 0f
    )
}

/**
 * Time attack tab
 */
@Composable
fun TimeAttackTab(progress: UserProgress, animationState: MutableTransitionState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Animated heading
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(300)) +
                    slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { -it / 2 }
                    )
        ) {
            Text(
                text = "Time Attack Performance",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Simplified time attack stats with animation
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(500, delayMillis = 300)) +
                    slideInHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        initialOffsetX = { it / 2 }
                    )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Best Time",
                    value = "2m 34s",
                    icon = Icons.Default.Timeline,
                    iconTint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Avg. Time per Question",
                    value = "15.2s",
                    icon = Icons.Default.Timeline,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Time attack history with animation
        AnimatedVisibility(
            visibleState = animationState,
            enter = fadeIn(animationSpec = tween(500, delayMillis = 600)) +
                    slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        initialOffsetY = { it / 2 }
                    )
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Time Attack History",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Use the dedicated Composable function
                    TimeAttackHistoryList()
                }
            }
        }
    }
}

/**
 * Composable function to display time attack history items
 */
@Composable
private fun TimeAttackHistoryList() {
    // Simulated time attack history
    TimeAttackHistoryItem(
        category = "Ancient Civilizations",
        score = "7/10",
        time = "3m 12s"
    )
    
    Divider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
    
    TimeAttackHistoryItem(
        category = "World Wars",
        score = "9/10",
        time = "2m 54s"
    )
    
    Divider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
    
    TimeAttackHistoryItem(
        category = "Medieval Period",
        score = "8/10",
        time = "3m 05s"
    )
}

/**
 * Circular chart showing accuracy
 */
@Composable
fun AccuracyChart(
    accuracy: Float,
    modifier: Modifier = Modifier
) {
    // Store color outside of Canvas scope
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary
    
    // Animate the progress
    val animatedProgress by animateFloatAsState(
        targetValue = accuracy,
        animationSpec = tween(1500, easing = LinearEasing),
        label = "progress_animation"
    )
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = min(canvasWidth, canvasHeight) / 2
            val strokeWidth = radius * 0.15f
            
            // Background circle
            drawArc(
                color = surfaceVariantColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(
                    (canvasWidth - radius * 2) / 2,
                    (canvasHeight - radius * 2) / 2
                ),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
            
            // Progress arc with gradient
            val gradient = Brush.linearGradient(
                colors = listOf(
                    primaryColor,
                    MaterialTheme.colorScheme.tertiary
                )
            )
            
            // Progress arc
            drawArc(
                brush = gradient,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(
                    (canvasWidth - radius * 2) / 2,
                    (canvasHeight - radius * 2) / 2
                ),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Accuracy",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ColorLegendItem(color = MaterialTheme.colorScheme.primary, label = "Ancient")
            ColorLegendItem(color = MaterialTheme.colorScheme.secondary, label = "Medieval")
            ColorLegendItem(color = MaterialTheme.colorScheme.tertiary, label = "Renaissance")
            ColorLegendItem(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), label = "Modern")
        }
        
        // Animated bar chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / 6
            val maxBarHeight = canvasHeight * 0.9f
            val verticalPadding = canvasHeight * 0.05f
            
            // Draw bars with rounded corners
            val colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
            )
            
            val heights = progress.categoryProgress.map { it.second * maxBarHeight }
            
            heights.forEachIndexed { index, height ->
                if (index < colors.size) {
                    drawRoundRect(
                        color = colors[index],
                        topLeft = Offset(
                            (index + 1) * barWidth - barWidth * 0.8f,
                            canvasHeight - height - verticalPadding
                        ),
                        size = androidx.compose.ui.geometry.Size(
                            barWidth * 0.6f,
                            height
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
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
    // Animate the progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1500, easing = LinearEasing),
        label = "category_progress_animation"
    )
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                )
            }
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
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon in a circle
            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$category - $level",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
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
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
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
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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