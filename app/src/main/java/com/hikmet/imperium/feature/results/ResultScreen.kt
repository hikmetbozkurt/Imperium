package com.hikmet.imperium.feature.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.ui.theme.ImperiumMotion
import com.hikmet.imperium.ui.components.ImperiumBackdrop
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    onRetry: (categoryId: String, levelNumber: Int) -> Unit,
    onLevels: (categoryId: String) -> Unit,
    onHome: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val result = state.result

    ImperiumBackdrop(Modifier.fillMaxSize()) {
        Scaffold(containerColor = Color.Transparent) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null || result == null -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        state.error ?: stringResource(R.string.result_not_found),
                        color = MaterialTheme.colorScheme.error,
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onHome) { Text(stringResource(R.string.result_home)) }
                }
                else -> Column(
                    modifier = Modifier
                        .widthIn(max = 720.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val animatedScore = remember(state.attemptId) { Animatable(0f) }
                    var revealedStars by remember(state.attemptId) { mutableIntStateOf(0) }
                    LaunchedEffect(state.attemptId, result.score, result.stars) {
                        animatedScore.snapTo(0f)
                        animatedScore.animateTo(
                            result.score.toFloat(),
                            animationSpec = tween(700),
                        )
                    }
                    LaunchedEffect(state.attemptId, result.stars) {
                        revealedStars = 0
                        repeat(result.stars) { index ->
                            delay(140)
                            revealedStars = index + 1
                        }
                    }
                    val start = Color(state.category?.gradientStartColor ?: 0xFF8C4A00)
                    val end = Color(state.category?.gradientEndColor ?: 0xFF5F4C24)
                    Box(
                        modifier = Modifier
                            .size(132.dp)
                            .background(Brush.linearGradient(listOf(start, end)), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "${animatedScore.value.toInt()}%",
                            color = Color.White,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = stringResource(
                            if (GameRules.isPassingStars(result.stars)) {
                                R.string.result_level_complete
                            } else {
                                R.string.result_keep_learning
                            },
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(
                            R.string.result_level_label,
                            state.category?.title.orEmpty(),
                            result.levelNumber,
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(Modifier.padding(vertical = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(3) { index ->
                            val isEarned = index < result.stars
                            val scale by animateFloatAsState(
                                targetValue = if (index < revealedStars) 1f else if (isEarned) 0.55f else 1f,
                                animationSpec = tween(ImperiumMotion.Standard),
                                label = "result-star-$index",
                            )
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < result.stars) Color(0xFFFFA000) else MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(40.dp).graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                },
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = state.isNewBest || state.addedStars > 0 || state.newlyUnlockedLevel != null,
                        enter = fadeIn(tween(ImperiumMotion.Standard)) +
                            slideInVertically(tween(ImperiumMotion.Standard)) { it / 3 },
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            if (state.isNewBest) {
                                AchievementBadge(
                                    icon = Icons.Default.EmojiEvents,
                                    text = stringResource(R.string.result_new_best),
                                )
                            }
                            if (state.addedStars > 0) {
                                AchievementBadge(
                                    icon = Icons.Default.Star,
                                    text = pluralStringResource(
                                        R.plurals.result_added_stars,
                                        state.addedStars,
                                        state.addedStars,
                                    ),
                                )
                            }
                            state.newlyUnlockedLevel?.let { unlockedLevel ->
                                AchievementBadge(
                                    icon = Icons.Default.LockOpen,
                                    text = stringResource(
                                        R.string.result_level_unlocked,
                                        unlockedLevel.toString(),
                                    ),
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Metric(
                                stringResource(R.string.result_correct),
                                "${result.correctAnswers}/${result.totalQuestions}",
                            )
                            Metric(
                                stringResource(R.string.result_time),
                                stringResource(R.string.result_seconds, result.durationMs / 1_000),
                            )
                            Metric(stringResource(R.string.result_stars), result.stars.toString())
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { onLevels(result.categoryId.value) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        Text(stringResource(R.string.result_continue))
                    }
                    OutlinedButton(
                        onClick = { onRetry(result.categoryId.value, result.levelNumber) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Text("  " + stringResource(R.string.result_try_again))
                    }
                    OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Home, contentDescription = null)
                        Text("  " + stringResource(R.string.result_home))
                    }
                }
                }
            }
        }
    }
}

@Composable
private fun AchievementBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(icon, contentDescription = null)
            Text(text, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun Metric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}
