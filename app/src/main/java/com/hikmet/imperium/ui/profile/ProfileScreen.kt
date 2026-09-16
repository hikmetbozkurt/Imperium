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
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.retrofit.BadgeSection
import com.hikmet.imperium.retrofit.BadgeViewModel
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
    badgeViewModel: BadgeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val overview by profileViewModel.progress.collectAsStateWithLifecycle()
    val badgeState by badgeViewModel.badgeState.collectAsStateWithLifecycle()
    val stars = overview?.totalStars ?: 0

    LaunchedEffect(overview?.totalStars) {
        overview?.let { badgeViewModel.refreshBadges(it.totalStars) }
    }

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
                ProfileArchiveHeader(stars)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProfileStat(
                        Modifier.weight(1f),
                        Icons.Default.Star,
                        stars.toString(),
                        stringResource(R.string.progress_stars),
                    )
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
                BadgeSection(badgeState = badgeState, userStars = stars)
                SoundSettings(profileViewModel)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileArchiveHeader(stars: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(ImperialColors.Burgundy, ImperialColors.Background),
                ),
            )
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImperialCoinEmblem(Modifier.size(76.dp))
        Spacer(Modifier.height(14.dp))
        Text(
            stringResource(R.string.profile_history_explorer),
            color = ImperialColors.OnSurface,
            style = ImperialTypography.Monument,
        )
        Text(
            stringResource(R.string.profile_archives_subtitle).uppercase(),
            color = ImperialColors.Gold,
            style = ImperialTypography.Label,
        )
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
private fun SoundSettings(viewModel: ProfileViewModel) {
    val soundManager = viewModel.soundManager
    var effectsEnabled by remember { mutableStateOf(soundManager.isSoundEnabled()) }
    var musicEnabled by remember { mutableStateOf(soundManager.isMusicEnabled()) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            stringResource(R.string.profile_preferences).uppercase(),
            color = ImperialColors.Gold,
            style = ImperialTypography.Label,
        )
        Spacer(Modifier.height(8.dp))
        Card(
            shape = ImperialPanelShape,
            colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
            border = BorderStroke(1.dp, ImperialColors.Outline),
        ) {
            SoundToggle(
                icon = if (effectsEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                label = stringResource(R.string.profile_sound_effects),
                checked = effectsEnabled,
                onCheckedChange = {
                    effectsEnabled = it
                    soundManager.setSoundEnabled(it)
                },
            )
            Box(Modifier.fillMaxWidth().height(1.dp).background(ImperialColors.Outline))
            SoundToggle(
                icon = Icons.Default.MusicNote,
                label = stringResource(R.string.profile_background_music),
                checked = musicEnabled,
                onCheckedChange = {
                    musicEnabled = it
                    soundManager.setMusicEnabled(it)
                },
            )
        }
    }
}

@Composable
private fun SoundToggle(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = ImperialColors.Gold)
        Text(
            label,
            color = ImperialColors.OnSurface,
            style = ImperialTypography.Body,
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ImperialColors.GoldLight,
                checkedTrackColor = ImperialColors.Burgundy,
                uncheckedThumbColor = ImperialColors.Muted,
                uncheckedTrackColor = ImperialColors.SurfaceLowest,
            ),
        )
    }
}
