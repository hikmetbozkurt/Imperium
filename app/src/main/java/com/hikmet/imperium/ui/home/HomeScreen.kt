package com.hikmet.imperium.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.hikmet.imperium.ui.components.categoryIcon
import com.hikmet.imperium.ui.components.ImperiumBackdrop
import com.hikmet.imperium.ui.navigation.NavDestinations
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val quoteCategory = state.categories
        .firstOrNull { it.content.id == state.featuredQuote.categoryId }
        ?.content

    DisposableEffect(viewModel.soundManager) {
        viewModel.soundManager.startBackgroundMusic()
        onDispose(viewModel.soundManager::pauseBackgroundMusic)
    }

    ImperiumBackdrop(Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)) {
                    NavigationItem(stringResource(R.string.nav_home), Icons.Default.Home, selected = true) { }
                    NavigationItem(stringResource(R.string.nav_progress), Icons.AutoMirrored.Filled.ShowChart) {
                        viewModel.soundManager.playButtonClick()
                        navController.navigate(NavDestinations.PROGRESS_ROUTE)
                    }
                    NavigationItem(stringResource(R.string.nav_profile), Icons.Default.Person) {
                        viewModel.soundManager.playButtonClick()
                        navController.navigate(NavDestinations.PROFILE_ROUTE)
                    }
                }
            }
        ) { padding ->
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item(
                    key = "featured_quote",
                    span = { GridItemSpan(maxLineSpan) },
                ) {
                    QuoteShowcase(
                        quote = state.featuredQuote,
                        category = quoteCategory,
                        onClick = {
                            viewModel.soundManager.playButtonClick()
                            viewModel.showNextQuote()
                        },
                    )
                }
                if (state.isLoading) {
                    item(key = "loading", span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(96.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(state.categories, key = { it.content.id.value }) { category ->
                        CategoryCard(category) {
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
private fun QuoteShowcase(
    quote: HistoricalQuote,
    category: HistoryCategory?,
    onClick: () -> Unit,
) {
    val startColor = category?.let { Color(it.gradientStartColor) }
        ?: MaterialTheme.colorScheme.primary
    val endColor = category?.let { Color(it.gradientEndColor) }
        ?: MaterialTheme.colorScheme.tertiary
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.10f)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 180.dp)
                .background(Brush.linearGradient(listOf(startColor, endColor)))
                .background(Color.Black.copy(alpha = 0.08f)),
        ) {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(84.dp)
                    .alpha(0.10f),
            )
            AnimatedContent(
                targetState = quote,
                modifier = Modifier.fillMaxWidth(),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                contentKey = HistoricalQuote::text,
                label = "featured historical quote",
            ) { displayedQuote ->
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = 0.14f),
                    ) {
                        Text(
                            text = displayedQuote.era.uppercase(Locale.ROOT),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.92f),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "“${displayedQuote.text}”",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "— ${displayedQuote.author} · ${displayedQuote.year}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = stringResource(R.string.home_quote_next),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(start = 5.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: HomeCategory, onClick: () -> Unit) {
    val content = category.content
    val progress = category.progress
    val unlockedFraction = if (content.levels.isEmpty()) {
        0f
    } else {
        (progress.unlockedLevels.toFloat() / content.levels.size).coerceIn(0f, 1f)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(184.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.10f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(Color(content.gradientStartColor), Color(content.gradientEndColor)),
                    ),
                )
                .background(Color.Black.copy(alpha = 0.08f))
                .padding(18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.13f),
                ) {
                    Icon(
                        imageVector = content.id.categoryIcon(),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.padding(9.dp),
                    )
                }
                Text(
                    text = content.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 14.dp),
                )
            }
            Text(
                text = content.description,
                color = Color.White.copy(alpha = 0.88f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).padding(top = 8.dp),
            )
            LinearProgressIndicator(
                progress = { unlockedFraction },
                modifier = Modifier.fillMaxWidth().height(5.dp),
                color = Color.White.copy(alpha = 0.88f),
                trackColor = Color.White.copy(alpha = 0.16f),
                drawStopIndicator = {},
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC857),
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    " " + androidx.compose.ui.res.pluralStringResource(
                        R.plurals.home_stars,
                        progress.totalStars,
                        progress.totalStars,
                    ),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    "  ·  " + androidx.compose.ui.res.pluralStringResource(
                        R.plurals.home_levels_unlocked,
                        progress.unlockedLevels,
                        progress.unlockedLevels,
                        content.levels.size,
                    ),
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f),
                )
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.13f),
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(
                            R.string.category_open_content_description,
                            content.title,
                        ),
                        tint = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.NavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) },
    )
}
