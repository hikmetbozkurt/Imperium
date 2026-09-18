package com.hikmet.imperium.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialScreenBackground
import com.hikmet.imperium.ui.components.ImperialTypography
import com.hikmet.imperium.ui.profile.ProfileSubpageHeader
import com.hikmet.imperium.ui.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val soundManager: SoundManager) : ViewModel()

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    var effectsEnabled by remember { mutableStateOf(viewModel.soundManager.isSoundEnabled()) }
    var musicEnabled by remember { mutableStateOf(viewModel.soundManager.isMusicEnabled()) }

    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ProfileSubpageHeader(
                    title = stringResource(R.string.settings_title),
                    subtitle = stringResource(R.string.settings_subtitle),
                    onBack = navController::navigateUp,
                )
            },
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp, vertical = 20.dp)) {
                Text(
                    stringResource(R.string.settings_audio).uppercase(),
                    color = ImperialColors.Gold,
                    style = ImperialTypography.Label,
                )
                Spacer(Modifier.height(8.dp))
                Card(
                    shape = ImperialPanelShape,
                    colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
                    border = BorderStroke(1.dp, ImperialColors.Outline),
                ) {
                    SettingToggle(
                        icon = if (effectsEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                        label = stringResource(R.string.profile_sound_effects),
                        checked = effectsEnabled,
                        onCheckedChange = {
                            effectsEnabled = it
                            viewModel.soundManager.setSoundEnabled(it)
                        },
                    )
                    Box(Modifier.fillMaxWidth().height(1.dp).background(ImperialColors.Outline))
                    SettingToggle(
                        icon = Icons.Default.MusicNote,
                        label = stringResource(R.string.profile_background_music),
                        checked = musicEnabled,
                        onCheckedChange = {
                            musicEnabled = it
                            viewModel.soundManager.setMusicEnabled(it)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingToggle(
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
