package com.hikmet.imperium.ui.progress

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hikmet.imperium.ui.theme.Primary
import com.hikmet.imperium.ui.theme.Secondary
import kotlin.math.roundToInt

/**
 * Detailed progress information container with fancy UI design
 */
@Composable
fun ProgressDetailsContainer(
    currentTimeView: TimeView,
    progressData: DetailedProgressData,
    modifier: Modifier = Modifier
) {
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        // Summary Cards Section
        ProgressSummaryCards(progressData = progressData)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Detailed Stats Section
        DetailedStatsSection(progressData = progressData, timeView = currentTimeView)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Performance Insights Section
        PerformanceInsightsSection(progressData = progressData)
    }
}

/**
 * Summary cards showing key metrics
 */
@Composable
private fun ProgressSummaryCards(progressData: DetailedProgressData) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(progressData.summaryMetrics) { metric ->
            SummaryMetricCard(metric = metric)
        }
    }
}

/**
 * Individual summary metric card with fancy design
 */
@Composable
private fun SummaryMetricCard(metric: SummaryMetric) {
    Card(
        modifier = Modifier.width(140.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            metric.color.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon with gradient background
                Surface(
                    shape = CircleShape,
                    color = metric.color.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = metric.icon,
                            contentDescription = metric.title,
                            tint = metric.color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = metric.value,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = metric.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                if (metric.subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = metric.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp
                        ),
                        color = metric.color,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Detailed statistics section
 */
@Composable
private fun DetailedStatsSection(
    progressData: DetailedProgressData,
    timeView: TimeView
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Detailed Statistics - ${timeView.displayName}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress bars for different categories
            progressData.categoryBreakdown.forEach { category ->
                CategoryProgressItem(category = category)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Additional metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailMetricItem(
                    label = "Average Score",
                    value = "${progressData.averageScore}%",
                    modifier = Modifier.weight(1f)
                )
                
                DetailMetricItem(
                    label = "Best Performance",
                    value = "${progressData.bestScore}%",
                    modifier = Modifier.weight(1f)
                )
                
                DetailMetricItem(
                    label = "Improvement",
                    value = "+${progressData.improvement}%",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Category progress item with animated progress bar
 */
@Composable
private fun CategoryProgressItem(category: CategoryProgress) {
    val animatedProgress by animateFloatAsState(
        targetValue = category.progress,
        animationSpec = tween(durationMillis = 1000),
        label = "category_progress"
    )
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "${(category.progress * 100).roundToInt()}%",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = category.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Performance insights section
 */
@Composable
private fun PerformanceInsightsSection(progressData: DetailedProgressData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary.copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Insights",
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Performance Insights",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            progressData.insights.forEach { insight ->
                InsightItem(insight = insight)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Individual insight item
 */
@Composable
private fun InsightItem(insight: String) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = Secondary,
                    shape = CircleShape
                )
                .padding(top = 6.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = insight,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Detail metric item for additional stats
 */
@Composable
private fun DetailMetricItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Primary
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Data classes for progress information
 */
data class DetailedProgressData(
    val summaryMetrics: List<SummaryMetric>,
    val categoryBreakdown: List<CategoryProgress>,
    val averageScore: Int,
    val bestScore: Int,
    val improvement: Int,
    val insights: List<String>
)

data class SummaryMetric(
    val title: String,
    val value: String,
    val subtitle: String = "",
    val icon: ImageVector,
    val color: Color
)

data class CategoryProgress(
    val name: String,
    val progress: Float,
    val description: String
)

 