package com.hikmet.imperium.ui.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.repository.CategoryAnalytics
import com.hikmet.imperium.domain.repository.ProgressOverview
import com.hikmet.imperium.domain.repository.ProgressPoint
import com.hikmet.imperium.ui.components.ImperialBrandHeader
import com.hikmet.imperium.ui.components.ImperialBottomBar
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialDestination
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialSectionTitle
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.navigation.NavDestinations
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProgressScreen(
    navController: NavHostController,
    viewModel: ProgressViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                ImperialBottomBar(
                    selected = ImperialDestination.Codex,
                    onHome = {
                        navController.navigate(NavDestinations.HOME_ROUTE) {
                            popUpTo(NavDestinations.HOME_ROUTE)
                            launchSingleTop = true
                        }
                    },
                    onCodex = {},
                    onProfile = { navController.navigate(NavDestinations.PROFILE_ROUTE) { launchSingleTop = true } },
                )
            },
        ) { padding ->
            val overview = state.overview
            if (state.isLoading || overview == null) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ImperialColors.Gold)
                }
            } else {
                CodexContent(overview, Modifier.fillMaxSize().padding(padding))
            }
        }
    }
}

@Composable
private fun CodexContent(overview: ProgressOverview, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ImperialBrandHeader(
                title = stringResource(R.string.codex_title),
                subtitle = stringResource(R.string.codex_subtitle),
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CodexMetric(
                    Modifier.weight(1f),
                    Icons.Default.Quiz,
                    overview.totalQuizzes.toString(),
                    stringResource(R.string.progress_quizzes),
                )
                CodexMetric(
                    Modifier.weight(1f),
                    Icons.Default.Star,
                    overview.totalStars.toString(),
                    stringResource(R.string.progress_stars),
                )
                CodexMetric(
                    Modifier.weight(1f),
                    Icons.Default.EmojiEvents,
                    "${overview.averageScore}%",
                    stringResource(R.string.progress_average),
                )
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = ImperialPanelShape,
                colors = CardDefaults.cardColors(containerColor = ImperialColors.Burgundy),
                border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.35f)),
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.TrendingUp,
                        null,
                        tint = ImperialColors.Gold,
                        modifier = Modifier.size(30.dp),
                    )
                    Column(Modifier.padding(start = 13.dp)) {
                        Text(
                            pluralStringResource(
                                R.plurals.progress_streak,
                                overview.currentStreakDays,
                                overview.currentStreakDays,
                            ),
                            color = ImperialColors.OnSurface,
                            style = ImperialTypography.Section,
                        )
                        Text(
                            stringResource(
                                R.string.progress_best_change,
                                overview.bestScore,
                                overview.improvementPercent,
                            ),
                            color = ImperialColors.OnSurfaceVariant,
                            style = ImperialTypography.Body,
                        )
                    }
                }
            }
        }
        item { AccuracyArchive(overview.timeline.takeLast(7)) }
        item {
            ImperialSectionTitle(
                eyebrow = stringResource(R.string.codex_volumes_eyebrow),
                title = stringResource(R.string.codex_volumes_title),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        items(overview.categories, key = { it.categoryId.value }) { category ->
            CodexVolume(category)
        }
    }
}

@Composable
private fun CodexMetric(modifier: Modifier, icon: ImageVector, value: String, label: String) {
    Card(
        modifier = modifier,
        shape = ImperialTileShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, null, tint = ImperialColors.Gold, modifier = Modifier.size(19.dp))
            Text(value, color = ImperialColors.OnSurface, fontWeight = FontWeight.Bold)
            Text(label, color = ImperialColors.Muted, style = ImperialTypography.Label, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun AccuracyArchive(points: List<ProgressPoint>) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceLow),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Column(Modifier.padding(17.dp)) {
            Text(
                stringResource(R.string.progress_recent_accuracy).uppercase(),
                color = ImperialColors.Gold,
                style = ImperialTypography.Label,
            )
            Spacer(Modifier.height(15.dp))
            if (points.isEmpty()) {
                Text(
                    stringResource(R.string.progress_empty_timeline),
                    color = ImperialColors.OnSurfaceVariant,
                    style = ImperialTypography.Body,
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    points.forEach { point ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Text(
                                point.averageScore.toString(),
                                color = ImperialColors.OnSurfaceVariant,
                                style = ImperialTypography.Label,
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 4.dp, max = 100.dp)
                                    .height((point.averageScore.coerceIn(1, 100)).dp)
                                    .background(ImperialColors.Burgundy, ImperialTileShape),
                            )
                            Text(
                                LocalDate.ofEpochDay(point.epochDay).format(DateTimeFormatter.ofPattern("E")),
                                color = ImperialColors.Muted,
                                style = ImperialTypography.Label,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CodexVolume(category: CategoryAnalytics) {
    val fraction = if (category.totalLevels == 0) 0f
    else category.completedLevels.toFloat().div(category.totalLevels).coerceIn(0f, 1f)
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialTileShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(category.title, color = ImperialColors.OnSurface, style = ImperialTypography.Section)
                    Text(
                        pluralStringResource(
                            R.plurals.progress_category_summary,
                            category.completedLevels,
                            category.completedLevels,
                            category.totalLevels,
                            category.averageScore,
                        ),
                        color = ImperialColors.Muted,
                        style = ImperialTypography.Body,
                    )
                }
                Text("${category.stars} ★", color = ImperialColors.Gold, fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier.fillMaxWidth().padding(top = 11.dp).height(4.dp),
                color = ImperialColors.Gold,
                trackColor = ImperialColors.SurfaceHighest,
                drawStopIndicator = {},
            )
        }
    }
}
