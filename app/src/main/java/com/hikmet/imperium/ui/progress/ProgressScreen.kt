package com.hikmet.imperium.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.domain.repository.CategoryAnalytics
import com.hikmet.imperium.domain.repository.ProgressOverview
import com.hikmet.imperium.domain.repository.ProgressPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavHostController,
    viewModel: ProgressViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your progress", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        val overview = state.overview
        if (state.isLoading || overview == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            ProgressContent(overview, Modifier.padding(padding))
        }
    }
}

@Composable
private fun ProgressContent(overview: ProgressOverview, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(Modifier.weight(1f), Icons.Default.Quiz, overview.totalQuizzes.toString(), "Quizzes")
                MetricCard(Modifier.weight(1f), Icons.Default.Star, overview.totalStars.toString(), "Stars")
                MetricCard(Modifier.weight(1f), Icons.Default.EmojiEvents, "${overview.averageScore}%", "Average")
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("Current streak: ${overview.currentStreakDays} days", fontWeight = FontWeight.Bold)
                        Text("Best score ${overview.bestScore}% · Change ${overview.improvementPercent}%")
                    }
                }
            }
        }
        item { ActivityChart(overview.timeline.takeLast(7)) }
        item { Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        items(overview.categories, key = { it.categoryId.value }) { category ->
            CategoryProgressCard(category)
        }
    }
}

@Composable
private fun MetricCard(modifier: Modifier, icon: ImageVector, value: String, label: String) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ActivityChart(points: List<ProgressPoint>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) {
            Text("Recent accuracy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            if (points.isEmpty()) {
                Text("Complete a quiz to start your real activity timeline.")
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    points.forEach { point ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Text("${point.averageScore}", style = MaterialTheme.typography.labelSmall)
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 4.dp, max = 110.dp)
                                    .height((point.averageScore.coerceIn(1, 100) * 1.1f).dp)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp)),
                            )
                            Text(
                                LocalDate.ofEpochDay(point.epochDay).format(DateTimeFormatter.ofPattern("E")),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryProgressCard(category: CategoryAnalytics) {
    val fraction = if (category.totalLevels == 0) 0f else category.completedLevels.toFloat() / category.totalLevels
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(category.title, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Text("${category.stars} ★", color = Color(0xFFFFA000))
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            Text(
                "${category.completedLevels}/${category.totalLevels} levels · ${category.averageScore}% average",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
