package com.hikmet.imperium.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialSectionTitle
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTopBar
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.components.categoryIcon
import com.hikmet.imperium.ui.navigation.NavDestinations

@Composable
fun ExpeditionsScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val summary = summarizeExpeditions(state.categories)
    DisposableEffect(viewModel.soundManager) {
        viewModel.soundManager.startBackgroundMusic()
        onDispose(viewModel.soundManager::pauseBackgroundMusic)
    }

    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ImperialTopBar(
                    title = stringResource(R.string.expeditions_title),
                    onBack = navController::navigateUp,
                )
            },
        ) { padding ->
            if (state.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ImperialColors.Gold)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        ImperialSectionTitle(
                            eyebrow = stringResource(R.string.expeditions_eyebrow),
                            title = stringResource(R.string.expeditions_subtitle),
                        )
                    }
                    item { ExpeditionLedger(summary) }
                    items(state.categories, key = { it.content.id.value }) { category ->
                        ExpeditionCategoryCard(category) {
                            viewModel.soundManager.playButtonClick()
                            navController.navigate(
                                "${NavDestinations.CATEGORY_ROUTE}/${category.content.id.value}",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpeditionLedger(summary: ExpeditionSummary) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = ImperialTileShape,
        color = ImperialColors.SurfaceLow,
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                stringResource(R.string.expeditions_eras_count, summary.categoryCount),
                color = ImperialColors.OnSurfaceVariant,
                style = ImperialTypography.Body,
            )
            Text(
                stringResource(R.string.expeditions_levels_count, summary.levelCount),
                color = ImperialColors.Gold,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ExpeditionCategoryCard(category: HomeCategory, onClick: () -> Unit) {
    val content = category.content
    val progress = category.progress
    val fraction = if (content.levels.isEmpty()) 0f
    else progress.unlockedLevels.toFloat().div(content.levels.size).coerceIn(0f, 1f)
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = CircleShape,
                color = ImperialColors.Burgundy,
                border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.48f)),
            ) {
                Icon(
                    content.id.categoryIcon(),
                    null,
                    tint = ImperialColors.GoldLight,
                    modifier = Modifier.padding(13.dp),
                )
            }
            Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(
                    content.title,
                    color = ImperialColors.OnSurface,
                    style = ImperialTypography.Section,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = ImperialColors.Gold, modifier = Modifier.size(15.dp))
                    Text(
                        pluralStringResource(R.plurals.home_stars, progress.totalStars, progress.totalStars),
                        color = ImperialColors.Muted,
                        style = ImperialTypography.Body,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                    Text(
                        " · ${progress.unlockedLevels}/${content.levels.size}",
                        color = ImperialColors.Muted,
                        style = ImperialTypography.Body,
                    )
                }
                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(4.dp),
                    color = ImperialColors.Gold,
                    trackColor = ImperialColors.SurfaceHighest,
                    drawStopIndicator = {},
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                stringResource(R.string.category_open_content_description, content.title),
                tint = ImperialColors.Gold,
            )
        }
    }
}
