package com.hikmet.imperium.feature.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.ui.components.ImperialCoinEmblem
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.theme.ImperiumMotion
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
    ImperialScreenBackground {
        Scaffold(containerColor = Color.Transparent) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                when {
                    state.isLoading -> CircularProgressIndicator(color = ImperialColors.Gold)
                    state.error != null || result == null -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            state.error ?: stringResource(R.string.result_not_found),
                            color = ImperialColors.Error,
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onHome) { Text(stringResource(R.string.result_home)) }
                    }
                    else -> {
                        val passed = GameRules.isPassingStars(result.stars)
                        val animatedScore = remember(state.attemptId) { Animatable(0f) }
                        var revealedStars by remember(state.attemptId) { mutableIntStateOf(0) }
                        LaunchedEffect(state.attemptId, result.score) {
                            animatedScore.snapTo(0f)
                            animatedScore.animateTo(result.score.toFloat(), tween(700))
                        }
                        LaunchedEffect(state.attemptId, result.stars) {
                            revealedStars = 0
                            repeat(result.stars) { index ->
                                delay(140)
                                revealedStars = index + 1
                            }
                        }
                        Column(
                            modifier = Modifier
                                .widthIn(max = 680.dp)
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp, vertical = 28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            ImperialCoinEmblem(Modifier.size(66.dp))
                            Text(
                                stringResource(
                                    if (passed) R.string.result_victory_eyebrow
                                    else R.string.result_recorded_eyebrow,
                                ).uppercase(),
                                color = ImperialColors.Gold,
                                style = ImperialTypography.Label,
                                modifier = Modifier.padding(top = 14.dp),
                            )
                            Text(
                                stringResource(
                                    if (passed) R.string.result_level_complete
                                    else R.string.result_keep_learning,
                                ),
                                color = ImperialColors.OnSurface,
                                style = ImperialTypography.Monument,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                stringResource(
                                    R.string.result_level_label,
                                    state.category?.title.orEmpty(),
                                    result.levelNumber,
                                ),
                                color = ImperialColors.OnSurfaceVariant,
                                style = ImperialTypography.Body,
                            )
                            Row(
                                modifier = Modifier.padding(vertical = 22.dp),
                                horizontalArrangement = Arrangement.spacedBy(11.dp),
                            ) {
                                repeat(3) { index ->
                                    val earned = index < result.stars
                                    val scale by animateFloatAsState(
                                        if (index < revealedStars) 1f else if (earned) 0.55f else 1f,
                                        tween(ImperiumMotion.Standard),
                                        label = "result-star-$index",
                                    )
                                    Surface(
                                        modifier = Modifier.size(58.dp).graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        },
                                        shape = CircleShape,
                                        color = if (earned) ImperialColors.GoldDark else ImperialColors.SurfaceHigh,
                                        border = BorderStroke(
                                            1.dp,
                                            if (earned) ImperialColors.Gold else ImperialColors.Outline,
                                        ),
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            null,
                                            tint = if (earned) ImperialColors.GoldLight else ImperialColors.Outline,
                                            modifier = Modifier.padding(13.dp),
                                        )
                                    }
                                }
                            }
                            ScoreSeal(animatedScore.value.toInt())
                            Spacer(Modifier.height(18.dp))
                            PerformanceCard(
                                correct = "${result.correctAnswers}/${result.totalQuestions}",
                                seconds = result.durationMs / 1_000,
                                stars = result.stars,
                            )
                            AnimatedVisibility(
                                visible = state.isNewBest || state.addedStars > 0 || state.newlyUnlockedLevel != null,
                                enter = fadeIn(tween(ImperiumMotion.Standard)) +
                                    slideInVertically(tween(ImperiumMotion.Standard)) { it / 3 },
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    if (state.isNewBest) {
                                        Achievement(Icons.Default.EmojiEvents, stringResource(R.string.result_new_best))
                                    }
                                    if (state.addedStars > 0) {
                                        Achievement(
                                            Icons.Default.Star,
                                            pluralStringResource(
                                                R.plurals.result_added_stars,
                                                state.addedStars,
                                                state.addedStars,
                                            ),
                                        )
                                    }
                                    state.newlyUnlockedLevel?.let {
                                        Achievement(
                                            Icons.Default.LockOpen,
                                            stringResource(R.string.result_level_unlocked, it.toString()),
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(22.dp))
                            Button(
                                onClick = { onLevels(result.categoryId.value) },
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                shape = ImperialTileShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ImperialColors.Burgundy,
                                    contentColor = ImperialColors.GoldLight,
                                ),
                                border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.55f)),
                            ) {
                                Text(stringResource(R.string.result_continue).uppercase(), style = ImperialTypography.Label)
                            }
                            ResultOutlinedButton(
                                icon = Icons.Default.Refresh,
                                text = stringResource(R.string.result_try_again),
                                onClick = { onRetry(result.categoryId.value, result.levelNumber) },
                            )
                            ResultOutlinedButton(Icons.Default.Home, stringResource(R.string.result_home), onHome)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreSeal(score: Int) {
    Box(
        modifier = Modifier
            .size(112.dp)
            .background(
                Brush.radialGradient(listOf(ImperialColors.Burgundy, ImperialColors.SurfaceLowest)),
                CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$score%", color = ImperialColors.GoldLight, style = ImperialTypography.Monument)
            Text(
                stringResource(R.string.result_score).uppercase(),
                color = ImperialColors.Muted,
                style = ImperialTypography.Label,
            )
        }
    }
}

@Composable
private fun PerformanceCard(correct: String, seconds: Long, stars: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ResultMetric(stringResource(R.string.result_correct), correct)
            ResultMetric(stringResource(R.string.result_time), stringResource(R.string.result_seconds, seconds))
            ResultMetric(stringResource(R.string.result_stars), stars.toString())
        }
    }
}

@Composable
private fun Achievement(icon: ImageVector, text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ImperialColors.SurfaceHigh,
        shape = ImperialTileShape,
        border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.32f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            Icon(icon, null, tint = ImperialColors.Gold)
            Text(text, color = ImperialColors.OnSurface, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ResultMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = ImperialColors.OnSurface, fontWeight = FontWeight.Bold)
        Text(label, color = ImperialColors.Muted, style = ImperialTypography.Label, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ResultOutlinedButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = ImperialTileShape,
        border = BorderStroke(1.dp, ImperialColors.Outline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = ImperialColors.OnSurfaceVariant),
    ) {
        Icon(icon, null)
        Text(text.uppercase(), style = ImperialTypography.Label, modifier = Modifier.padding(start = 8.dp))
    }
}
