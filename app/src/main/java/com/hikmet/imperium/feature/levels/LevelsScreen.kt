package com.hikmet.imperium.feature.levels

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.model.HistoryLevel
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTopBar
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.theme.ImperiumMotion

@Composable
fun LevelsScreen(
    onBack: () -> Unit,
    onLevelSelected: (Int) -> Unit,
    viewModel: LevelsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val category = state.category

    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ImperialTopBar(
                    title = category?.title ?: stringResource(R.string.levels_title),
                    onBack = onBack,
                )
            },
        ) { padding ->
            when {
                state.error != null -> Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(state.error.orEmpty(), color = ImperialColors.Error)
                }
                category == null -> Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = ImperialColors.Gold)
                }
                else -> {
                    val progress = state.progress
                    val unlocked = progress?.unlockedLevels ?: 1
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .widthIn(max = 760.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        item {
                            CampaignHeader(
                                description = category.description,
                                stars = progress?.totalStars ?: 0,
                                unlocked = unlocked,
                                total = category.levels.size,
                            )
                        }
                        itemsIndexed(category.levels, key = { _, level -> level.number }) { index, level ->
                            CampaignLevel(
                                level = level,
                                stars = progress?.levelStars?.get(level.number) ?: 0,
                                totalStars = progress?.totalStars ?: 0,
                                unlocked = level.number <= unlocked,
                                active = level.number == unlocked.coerceAtMost(category.levels.size),
                                alignStart = index % 2 == 0,
                                first = index == 0,
                                last = index == category.levels.lastIndex,
                                onClick = { onLevelSelected(level.number) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CampaignHeader(description: String, stars: Int, unlocked: Int, total: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 18.dp),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.Burgundy),
        border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.42f)),
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(ImperialColors.Burgundy, ImperialColors.SurfaceHigh),
                    ),
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                stringResource(R.string.levels_campaign_eyebrow).uppercase(),
                color = ImperialColors.Gold,
                style = ImperialTypography.Label,
            )
            Text(description, color = ImperialColors.OnSurface, style = ImperialTypography.Section)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = ImperialColors.Gold, modifier = Modifier.size(18.dp))
                Text(
                    pluralStringResource(R.plurals.levels_stars_earned, stars, stars),
                    color = ImperialColors.OnSurfaceVariant,
                    style = ImperialTypography.Body,
                    modifier = Modifier.padding(start = 6.dp).weight(1f),
                )
                Text("$unlocked / $total", color = ImperialColors.GoldLight, fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator(
                progress = { if (total == 0) 0f else unlocked.toFloat().div(total).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = ImperialColors.Gold,
                trackColor = ImperialColors.SurfaceHighest,
                drawStopIndicator = {},
            )
        }
    }
}

@Composable
private fun CampaignLevel(
    level: HistoryLevel,
    stars: Int,
    totalStars: Int,
    unlocked: Boolean,
    active: Boolean,
    alignStart: Boolean,
    first: Boolean,
    last: Boolean,
    onClick: () -> Unit,
) {
    val nodeColor by animateColorAsState(
        targetValue = when {
            active -> ImperialColors.Burgundy
            stars >= 2 -> ImperialColors.GoldDark
            unlocked -> ImperialColors.SurfaceHighest
            else -> ImperialColors.SurfaceLow
        },
        animationSpec = tween(ImperiumMotion.Standard),
        label = "campaign-node",
    )
    Box(modifier = Modifier.fillMaxWidth().height(132.dp)) {
        if (!first) {
            Box(
                Modifier.align(Alignment.TopCenter).width(2.dp).height(66.dp)
                    .background(ImperialColors.Outline),
            )
        }
        if (!last) {
            Box(
                Modifier.align(Alignment.BottomCenter).width(2.dp).height(66.dp)
                    .background(ImperialColors.Outline),
            )
        }
        Surface(
            modifier = Modifier.align(Alignment.Center).size(if (active) 58.dp else 50.dp),
            shape = CircleShape,
            color = nodeColor,
            border = BorderStroke(
                width = if (active) 2.dp else 1.dp,
                color = if (unlocked) ImperialColors.Gold else ImperialColors.Outline,
            ),
            shadowElevation = if (active) 8.dp else 2.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (unlocked) {
                    Text(
                        level.number.toString(),
                        color = ImperialColors.GoldLight,
                        style = ImperialTypography.Section,
                    )
                } else {
                    Icon(Icons.Default.Lock, null, tint = ImperialColors.Muted, modifier = Modifier.size(20.dp))
                }
            }
        }
        Card(
            modifier = Modifier
                .align(if (alignStart) Alignment.CenterStart else Alignment.CenterEnd)
                .fillMaxWidth(0.39f)
                .clickable(enabled = unlocked, role = Role.Button, onClick = onClick)
                .alpha(if (unlocked) 1f else 0.58f),
            shape = ImperialTileShape,
            colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
            border = BorderStroke(1.dp, if (active) ImperialColors.Gold else ImperialColors.Outline),
        ) {
            Column(Modifier.fillMaxWidth().padding(11.dp)) {
                Text(
                    level.title,
                    color = ImperialColors.OnSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (alignStart) TextAlign.Start else TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (stars > 0) {
                    Text(
                        "★".repeat(stars),
                        color = ImperialColors.Gold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = if (alignStart) TextAlign.Start else TextAlign.End,
                    )
                } else if (!unlocked) {
                    val needed = (level.requiredStars - totalStars).coerceAtLeast(0)
                    Text(
                        if (needed > 0) pluralStringResource(
                            R.plurals.levels_more_stars_needed,
                            needed,
                            needed,
                        ) else stringResource(R.string.levels_locked),
                        color = ImperialColors.Muted,
                        style = ImperialTypography.Label,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else if (active) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, null, tint = ImperialColors.Gold, modifier = Modifier.size(15.dp))
                        Text(
                            stringResource(R.string.levels_current).uppercase(),
                            color = ImperialColors.Gold,
                            style = ImperialTypography.Label,
                        )
                    }
                }
            }
        }
    }
}
