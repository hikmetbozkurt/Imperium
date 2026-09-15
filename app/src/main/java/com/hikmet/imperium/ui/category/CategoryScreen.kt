package com.hikmet.imperium.ui.category

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
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
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.feature.levels.LevelsViewModel
import com.hikmet.imperium.ui.components.CategoryVisualAssets
import com.hikmet.imperium.ui.components.categoryIcon
import com.hikmet.imperium.ui.components.visualAssets
import com.hikmet.imperium.ui.components.ImperiumBackdrop
import com.hikmet.imperium.ui.navigation.NavDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavHostController,
    categoryId: String,
    viewModel: LevelsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val category = state.category

    ImperiumBackdrop(Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(category?.title ?: stringResource(R.string.category_fallback_title)) },
                    navigationIcon = {
                        IconButton(onClick = navController::navigateUp) {
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
            state.error != null -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error)
            }
            category == null -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            else -> {
                val visualAssets = category.visualAssets()
                Box(Modifier.fillMaxSize().padding(padding)) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 900.dp)
                            .fillMaxSize()
                            .align(Alignment.TopCenter)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        CategoryHero(category)
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            CategoryAboutCard(category, visualAssets)
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatisticCard(
                                    modifier = Modifier.weight(1f),
                                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                                    value = "${category.levels.size}",
                                    label = stringResource(R.string.category_levels),
                                )
                                StatisticCard(
                                    modifier = Modifier.weight(1f),
                                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                                    value = "${state.progress?.totalStars ?: 0}",
                                    label = stringResource(R.string.category_stars),
                                )
                                StatisticCard(
                                    modifier = Modifier.weight(1f),
                                    icon = { Icon(Icons.Default.Timer, contentDescription = null) },
                                    value = stringResource(
                                        R.string.quiz_seconds,
                                        GameRules.QUESTION_DURATION_MS / 1_000,
                                    ),
                                    label = stringResource(R.string.category_per_question),
                                )
                            }
                            Button(
                                onClick = { navController.navigate(NavDestinations.levels(categoryId)) },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                            ) {
                                Text(stringResource(R.string.category_explore_levels))
                            }
                        }
                    }
                }
            }
            }
        }
    }
}

@Composable
private fun CategoryHero(
    category: HistoryCategory,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(category.gradientStartColor), Color(category.gradientEndColor)),
                ),
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(76.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.16f),
        ) {
            Icon(
                imageVector = category.id.categoryIcon(),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(17.dp),
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            category.title,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            category.description,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CategoryAboutCard(
    category: HistoryCategory,
    visualAssets: CategoryVisualAssets,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column {
            visualAssets.illustrationResId?.let { illustrationResId ->
                Image(
                    painter = painterResource(illustrationResId),
                    contentDescription = stringResource(
                        R.string.category_illustration_content_description,
                        category.title,
                    ),
                    modifier = Modifier.fillMaxWidth().height(176.dp),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = RoundedCornerShape(13.dp),
                        color = Color(category.gradientStartColor).copy(alpha = 0.14f),
                    ) {
                        Icon(
                            imageVector = category.id.categoryIcon(),
                            contentDescription = null,
                            tint = Color(category.gradientStartColor),
                            modifier = Modifier.padding(9.dp),
                        )
                    }
                    Text(
                        text = stringResource(R.string.category_about),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(category.longDescription, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun StatisticCard(
    modifier: Modifier,
    icon: @Composable () -> Unit,
    value: String,
    label: String,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.size(28.dp), contentAlignment = Alignment.Center) { icon() }
            Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}
