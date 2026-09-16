package com.hikmet.imperium.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.ui.components.ImperialBrandHeader
import com.hikmet.imperium.ui.components.ImperialBottomBar
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialDestination
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialSectionTitle
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.components.categoryIcon
import com.hikmet.imperium.ui.components.visualAssets
import com.hikmet.imperium.ui.navigation.NavDestinations
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentExpedition = selectCurrentExpedition(state.categories)
    val quoteCategory = state.categories
        .firstOrNull { it.content.id == state.featuredQuote.categoryId }
        ?.content

    DisposableEffect(viewModel.soundManager) {
        viewModel.soundManager.startBackgroundMusic()
        onDispose(viewModel.soundManager::pauseBackgroundMusic)
    }

    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                ImperialBottomBar(
                    selected = ImperialDestination.Home,
                    onHome = {},
                    onCodex = {
                        viewModel.soundManager.playButtonClick()
                        navController.navigate(NavDestinations.PROGRESS_ROUTE) { launchSingleTop = true }
                    },
                    onProfile = {
                        viewModel.soundManager.playButtonClick()
                        navController.navigate(NavDestinations.PROFILE_ROUTE) { launchSingleTop = true }
                    },
                )
            },
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    ImperialBrandHeader(
                        title = stringResource(R.string.app_name),
                        subtitle = stringResource(R.string.home_archive_subtitle),
                        trailing = {
                            Surface(
                                shape = ImperialTileShape,
                                color = ImperialColors.SurfaceHigh,
                                border = BorderStroke(1.dp, ImperialColors.Outline),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = ImperialColors.Gold,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Text(
                                        text = state.categories.sumOf { it.progress.totalStars }.toString(),
                                        color = ImperialColors.OnSurface,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 5.dp),
                                    )
                                }
                            }
                        },
                    )
                }
                if (state.isLoading) {
                    item {
                        Box(Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ImperialColors.Gold)
                        }
                    }
                } else {
                    currentExpedition?.let { expedition ->
                        item {
                            CurrentExpeditionCard(
                                category = expedition,
                                onContinue = {
                                    viewModel.soundManager.playButtonClick()
                                    navController.navigate(NavDestinations.levels(expedition.content.id.value))
                                },
                            )
                        }
                    }
                    item {
                        ImperialSectionTitle(
                            eyebrow = stringResource(R.string.home_collections_eyebrow),
                            title = stringResource(R.string.home_collections_title),
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                    items(state.categories, key = { it.content.id.value }) { category ->
                        CategoryArchiveCard(category) {
                            viewModel.soundManager.playButtonClick()
                            navController.navigate(
                                "${NavDestinations.CATEGORY_ROUTE}/${category.content.id.value}",
                            )
                        }
                    }
                    item {
                        QuoteShowcase(
                            quote = state.featuredQuote,
                            category = quoteCategory,
                            onClick = {
                                viewModel.soundManager.playButtonClick()
                                viewModel.showNextQuote()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentExpeditionCard(category: HomeCategory, onContinue: () -> Unit) {
    val content = category.content
    val progress = category.progress
    val fraction = if (content.levels.isEmpty()) 0f
    else progress.unlockedLevels.toFloat().div(content.levels.size).coerceIn(0f, 1f)
    val image = content.visualAssets().illustrationResId

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.Surface),
        border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.30f)),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Box(Modifier.fillMaxWidth().heightIn(min = 232.dp)) {
            image?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.38f,
                )
            }
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, ImperialColors.Burgundy.copy(alpha = 0.88f)),
                    ),
                ),
            )
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart).padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                Text(
                    stringResource(R.string.home_current_expedition).uppercase(),
                    color = ImperialColors.Gold,
                    style = ImperialTypography.Label,
                )
                Text(content.title, color = ImperialColors.OnSurface, style = ImperialTypography.Monument)
                Text(
                    pluralStringResource(
                        R.plurals.home_levels_unlocked,
                        progress.unlockedLevels,
                        progress.unlockedLevels,
                        content.levels.size,
                    ),
                    color = ImperialColors.OnSurfaceVariant,
                    style = ImperialTypography.Body,
                )
                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier.fillMaxWidth().height(5.dp),
                    color = ImperialColors.Gold,
                    trackColor = ImperialColors.SurfaceHighest,
                    drawStopIndicator = {},
                )
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = ImperialTileShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ImperialColors.Gold,
                        contentColor = ImperialColors.SurfaceLowest,
                    ),
                ) {
                    Text(stringResource(R.string.home_continue_expedition).uppercase(), style = ImperialTypography.Label)
                }
            }
        }
    }
}

@Composable
private fun CategoryArchiveCard(category: HomeCategory, onClick: () -> Unit) {
    val content = category.content
    val progress = category.progress
    val fraction = if (content.levels.isEmpty()) 0f
    else progress.unlockedLevels.toFloat().div(content.levels.size).coerceIn(0f, 1f)
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialTileShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = ImperialColors.Burgundy,
                border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.42f)),
            ) {
                Icon(
                    content.id.categoryIcon(),
                    contentDescription = null,
                    tint = ImperialColors.GoldLight,
                    modifier = Modifier.padding(12.dp),
                )
            }
            Column(Modifier.weight(1f).padding(horizontal = 13.dp)) {
                Text(
                    content.title,
                    color = ImperialColors.OnSurface,
                    style = ImperialTypography.Section,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    pluralStringResource(R.plurals.home_stars, progress.totalStars, progress.totalStars),
                    color = ImperialColors.Muted,
                    style = ImperialTypography.Body,
                )
                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier.fillMaxWidth().padding(top = 7.dp).height(3.dp),
                    color = ImperialColors.Gold,
                    trackColor = ImperialColors.SurfaceHighest,
                    drawStopIndicator = {},
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.category_open_content_description, content.title),
                tint = ImperialColors.Gold,
            )
        }
    }
}

@Composable
private fun QuoteShowcase(
    quote: HistoricalQuote,
    category: HistoryCategory?,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceLow),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Box(Modifier.fillMaxWidth().heightIn(min = 176.dp)) {
            Icon(
                Icons.Default.FormatQuote,
                contentDescription = null,
                tint = ImperialColors.Gold,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).size(76.dp).alpha(0.08f),
            )
            AnimatedContent(
                targetState = quote,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                contentKey = HistoricalQuote::text,
                label = "featured historical quote",
            ) { displayedQuote ->
                Column(Modifier.fillMaxWidth().padding(18.dp)) {
                    Text(
                        text = (category?.title ?: displayedQuote.era).uppercase(Locale.ROOT),
                        color = ImperialColors.Gold,
                        style = ImperialTypography.Label,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "“${displayedQuote.text}”",
                        color = ImperialColors.OnSurface,
                        style = ImperialTypography.Section,
                        fontStyle = FontStyle.Italic,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "— ${displayedQuote.author} · ${displayedQuote.year}",
                        color = ImperialColors.OnSurfaceVariant,
                        style = ImperialTypography.Body,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.Refresh, null, tint = ImperialColors.Muted, modifier = Modifier.size(15.dp))
                        Text(
                            stringResource(R.string.home_quote_next).uppercase(),
                            color = ImperialColors.Muted,
                            style = ImperialTypography.Label,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }
                }
            }
        }
    }
}
