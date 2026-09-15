package com.hikmet.imperium.feature.levels

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.domain.model.HistoryLevel
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.theme.ImperiumMotion
import com.hikmet.imperium.ui.components.ImperiumBackdrop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelsScreen(
    onBack: () -> Unit,
    onLevelSelected: (Int) -> Unit,
    viewModel: LevelsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val category = state.category

    ImperiumBackdrop(Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(category?.title ?: stringResource(R.string.levels_title)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.common_back),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
        ) { padding ->
            when {
                state.error != null -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error)
                }
                category == null -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                else -> Box(Modifier.fillMaxSize().padding(padding)) {
                    LazyColumn(
                        modifier = Modifier.widthIn(max = 840.dp).fillMaxSize().align(Alignment.TopCenter),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(category.gradientStartColor), Color(category.gradientEndColor)),
                                ),
                                shape = RoundedCornerShape(24.dp),
                            )
                            .padding(20.dp),
                    ) {
                        Text(category.description, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                            Text(
                                text = " " + pluralStringResource(
                                    R.plurals.levels_stars_earned,
                                    state.progress?.totalStars ?: 0,
                                    state.progress?.totalStars ?: 0,
                                ),
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
                itemsIndexed(
                    items = category.levels,
                    key = { _, level -> level.number },
                ) { index, level ->
                    val unlocked = level.number <= (state.progress?.unlockedLevels ?: 1)
                    LevelCard(
                        level = level,
                        stars = state.progress?.levelStars?.get(level.number) ?: 0,
                        totalStars = state.progress?.totalStars ?: 0,
                        unlocked = unlocked,
                        isFirst = index == 0,
                        isLast = index == category.levels.lastIndex,
                        onClick = { if (unlocked) onLevelSelected(level.number) },
                    )
                }
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelCard(
    level: HistoryLevel,
    stars: Int,
    totalStars: Int,
    unlocked: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (unlocked) {
            MaterialTheme.colorScheme.surfaceContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(ImperiumMotion.Standard),
        label = "level-container",
    )
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.width(54.dp).fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            if (!isFirst) {
                Box(
                    Modifier
                        .align(Alignment.TopCenter)
                        .width(2.dp)
                        .fillMaxHeight(0.5f)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
            }
            if (!isLast) {
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .width(2.dp)
                        .fillMaxHeight(0.5f)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
            }
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(14.dp),
                color = when {
                    stars >= 2 -> MaterialTheme.colorScheme.primary
                    unlocked -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                tonalElevation = 3.dp,
                shadowElevation = if (unlocked) 3.dp else 0.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        level.number.toString(),
                        fontWeight = FontWeight.Bold,
                        color = if (stars >= 2) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                }
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = containerColor),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .weight(1f)
                .clickable(enabled = unlocked, role = Role.Button, onClick = onClick),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .animateContentSize(tween(ImperiumMotion.Standard)),
                ) {
                    Text(level.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(level.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                    if (stars > 0) {
                        Text("★".repeat(stars), color = Color(0xFFFFA000), style = MaterialTheme.typography.titleMedium)
                    }
                    if (!unlocked) {
                        val remainingStars = (level.requiredStars - totalStars).coerceAtLeast(0)
                        Text(
                            text = if (remainingStars > 0) {
                                pluralStringResource(
                                    R.plurals.levels_more_stars_needed,
                                    remainingStars,
                                    remainingStars,
                                )
                            } else {
                                stringResource(R.string.levels_pass_previous)
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                AnimatedContent(targetState = unlocked, label = "level-lock-state") { isUnlocked ->
                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.PlayArrow else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}
