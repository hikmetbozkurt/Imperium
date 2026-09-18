package com.hikmet.imperium.ui.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.repository.CategoryAnalytics
import com.hikmet.imperium.ui.components.ImperialBottomBar
import com.hikmet.imperium.ui.components.ImperialCoinEmblem
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialDestination
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.navigation.NavDestinations

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val overview by profileViewModel.progress.collectAsStateWithLifecycle()
    val stars = overview?.totalStars ?: 0

    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                ImperialBottomBar(
                    selected = ImperialDestination.Profile,
                    onHome = {
                        navController.navigate(NavDestinations.HOME_ROUTE) {
                            popUpTo(NavDestinations.HOME_ROUTE)
                            launchSingleTop = true
                        }
                    },
                    onCodex = { navController.navigate(NavDestinations.PROGRESS_ROUTE) { launchSingleTop = true } },
                    onProfile = {},
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .widthIn(max = 820.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ProfileArchiveHeader(
                    stars = stars,
                    rank = profileRank(stars),
                    onSettings = { navController.navigate(NavDestinations.SETTINGS_ROUTE) },
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProfileStat(Modifier.weight(1f), Icons.Default.Star, stars.toString(), stringResource(R.string.progress_stars))
                    ProfileStat(
                        Modifier.weight(1f),
                        Icons.Default.Quiz,
                        (overview?.totalQuizzes ?: 0).toString(),
                        stringResource(R.string.progress_quizzes),
                    )
                    ProfileStat(
                        Modifier.weight(1f),
                        Icons.Default.EmojiEvents,
                        "${overview?.bestScore ?: 0}%",
                        stringResource(R.string.profile_best),
                    )
                }
                ProfileMastery(categories = overview?.categories.orEmpty())
                ProfileNavigationCard(
                    icon = Icons.Default.WorkspacePremium,
                    title = stringResource(R.string.profile_badges_cta),
                    description = stringResource(R.string.profile_badges_description),
                    onClick = { navController.navigate(NavDestinations.BADGES_ROUTE) },
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileArchiveHeader(stars: Int, rank: ProfileRank, onSettings: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(ImperialColors.Burgundy, ImperialColors.Background))),
    ) {
        IconButton(
            onClick = onSettings,
            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = stringResource(R.string.profile_settings_content_description),
                tint = ImperialColors.Gold,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImperialCoinEmblem(Modifier.size(76.dp))
            Spacer(Modifier.height(14.dp))
            Text(stringResource(R.string.profile_history_explorer), color = ImperialColors.OnSurface, style = ImperialTypography.Monument)
            Text(rank.label().uppercase(), color = ImperialColors.Gold, style = ImperialTypography.Label)
            Text(
                stringResource(R.string.profile_summary),
                color = ImperialColors.OnSurfaceVariant,
                style = ImperialTypography.Body,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp),
            )
            Text(
                stringResource(R.string.profile_recorded_stars, stars),
                color = ImperialColors.Muted,
                style = ImperialTypography.Label,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun ProfileRank.label(): String = stringResource(
    when (this) {
        ProfileRank.NoviceChronicler -> R.string.profile_rank_novice
        ProfileRank.Archivist -> R.string.profile_rank_archivist
        ProfileRank.Historian -> R.string.profile_rank_historian
        ProfileRank.ImperialScholar -> R.string.profile_rank_imperial_scholar
    },
)

@Composable
private fun ProfileStat(modifier: Modifier, icon: ImageVector, value: String, label: String) {
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
private fun ProfileMastery(categories: List<CategoryAnalytics>) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(stringResource(R.string.profile_mastery).uppercase(), color = ImperialColors.Gold, style = ImperialTypography.Label)
        Spacer(Modifier.height(8.dp))
        Card(
            shape = ImperialPanelShape,
            colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
            border = BorderStroke(1.dp, ImperialColors.Outline),
        ) {
            if (categories.isEmpty()) {
                Text(
                    stringResource(R.string.profile_mastery_empty),
                    color = ImperialColors.OnSurfaceVariant,
                    style = ImperialTypography.Body,
                    modifier = Modifier.padding(18.dp),
                )
            } else {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    categories.forEach { category -> MasteryRow(category) }
                }
            }
        }
    }
}

@Composable
private fun MasteryRow(category: CategoryAnalytics) {
    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                category.title,
                color = ImperialColors.OnSurface,
                style = ImperialTypography.Body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                stringResource(R.string.profile_mastery_levels, category.completedLevels, category.totalLevels),
                color = ImperialColors.Muted,
                style = ImperialTypography.Label,
            )
        }
        LinearProgressIndicator(
            progress = { masteryProgress(category.completedLevels, category.totalLevels) },
            modifier = Modifier.fillMaxWidth().padding(top = 3.dp).height(4.dp),
            color = ImperialColors.Gold,
            trackColor = ImperialColors.SurfaceLowest,
            drawStopIndicator = {},
        )
    }
}

@Composable
private fun ProfileNavigationCard(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.Burgundy),
        border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.45f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(17.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, null, tint = ImperialColors.Gold, modifier = Modifier.size(31.dp))
            Column(Modifier.weight(1f).padding(horizontal = 13.dp)) {
                Text(title, color = ImperialColors.GoldLight, style = ImperialTypography.Section)
                Text(description, color = ImperialColors.OnSurfaceVariant, style = ImperialTypography.Body)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = ImperialColors.Gold)
        }
    }
}
