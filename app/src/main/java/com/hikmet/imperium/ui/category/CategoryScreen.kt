package com.hikmet.imperium.ui.category

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.feature.levels.LevelsViewModel
import com.hikmet.imperium.ui.components.CategoryVisualAssets
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTopBar
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.components.categoryIcon
import com.hikmet.imperium.ui.components.visualAssets
import com.hikmet.imperium.ui.navigation.NavDestinations

@Composable
fun CategoryScreen(
    navController: NavHostController,
    categoryId: String,
    viewModel: LevelsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val category = state.category
    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ImperialTopBar(
                    title = category?.title ?: stringResource(R.string.category_fallback_title),
                    onBack = navController::navigateUp,
                )
            },
        ) { padding ->
            when {
                state.error != null -> Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) { Text(state.error.orEmpty(), color = ImperialColors.Error) }
                category == null -> Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator(color = ImperialColors.Gold) }
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .widthIn(max = 820.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    CategoryHero(category, category.visualAssets())
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ArchiveStat(
                                Modifier.weight(1f),
                                Icons.Default.CheckCircle,
                                category.levels.size.toString(),
                                stringResource(R.string.category_levels),
                            )
                            ArchiveStat(
                                Modifier.weight(1f),
                                Icons.Default.Star,
                                (state.progress?.totalStars ?: 0).toString(),
                                stringResource(R.string.category_stars),
                            )
                            ArchiveStat(
                                Modifier.weight(1f),
                                Icons.Default.Timer,
                                stringResource(R.string.quiz_seconds, GameRules.QUESTION_DURATION_MS / 1_000),
                                stringResource(R.string.category_per_question),
                            )
                        }
                        Card(
                            shape = ImperialPanelShape,
                            colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
                            border = BorderStroke(1.dp, ImperialColors.Outline),
                        ) {
                            Column(Modifier.padding(18.dp)) {
                                Text(
                                    stringResource(R.string.category_about).uppercase(),
                                    color = ImperialColors.Gold,
                                    style = ImperialTypography.Label,
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    category.longDescription,
                                    color = ImperialColors.OnSurfaceVariant,
                                    style = ImperialTypography.Body,
                                )
                            }
                        }
                        Button(
                            onClick = { navController.navigate(NavDestinations.levels(categoryId)) },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = ImperialTileShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ImperialColors.Burgundy,
                                contentColor = ImperialColors.GoldLight,
                            ),
                            border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.55f)),
                        ) {
                            Text(stringResource(R.string.category_explore_levels).uppercase(), style = ImperialTypography.Label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHero(category: HistoryCategory, assets: CategoryVisualAssets) {
    Box(Modifier.fillMaxWidth().height(260.dp)) {
        assets.illustrationResId?.let {
            Image(
                painter = painterResource(it),
                contentDescription = stringResource(R.string.category_illustration_content_description, category.title),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, ImperialColors.Background.copy(alpha = 0.98f)),
                ),
            ),
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier.size(58.dp),
                color = ImperialColors.Burgundy,
                shape = ImperialTileShape,
                border = BorderStroke(1.dp, ImperialColors.Gold),
            ) {
                Icon(
                    category.id.categoryIcon(),
                    null,
                    tint = ImperialColors.GoldLight,
                    modifier = Modifier.padding(14.dp),
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(category.title, color = ImperialColors.OnSurface, style = ImperialTypography.Monument)
            Text(
                category.description,
                color = ImperialColors.OnSurfaceVariant,
                style = ImperialTypography.Body,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ArchiveStat(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
) {
    Card(
        modifier = modifier,
        shape = ImperialTileShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.Surface),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, null, tint = ImperialColors.Gold, modifier = Modifier.size(19.dp))
            Text(value, color = ImperialColors.OnSurface, fontWeight = FontWeight.Bold)
            Text(label, color = ImperialColors.Muted, style = ImperialTypography.Label, textAlign = TextAlign.Center)
        }
    }
}
