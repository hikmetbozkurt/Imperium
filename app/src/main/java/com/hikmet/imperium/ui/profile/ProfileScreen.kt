package com.hikmet.imperium.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.retrofit.BadgeSection
import com.hikmet.imperium.retrofit.BadgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            ProfileHeader()
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Stat(Modifier.weight(1f), Icons.Default.Star, stars.toString(), "Stars")
                Stat(Modifier.weight(1f), Icons.Default.Quiz, (overview?.totalQuizzes ?: 0).toString(), "Quizzes")
                Stat(Modifier.weight(1f), Icons.Default.EmojiEvents, "${overview?.bestScore ?: 0}%", "Best")
            }
            BadgeSection(
                badgeState = badgeState,
                userStars = stars,
            )
            SoundSettings(profileViewModel)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary),
                ),
            )
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.18f)) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(18.dp),
            )
        }
        Spacer(Modifier.height(12.dp))
        Text("History Explorer", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Progress is calculated from your completed quizzes", color = Color.White.copy(alpha = 0.82f))
    }
}

@Composable
private fun Stat(modifier: Modifier, icon: ImageVector, value: String, label: String) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun SoundSettings(viewModel: ProfileViewModel) {
    val soundManager = viewModel.soundManager
    var effectsEnabled by remember { mutableStateOf(soundManager.isSoundEnabled()) }
    var musicEnabled by remember { mutableStateOf(soundManager.isMusicEnabled()) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text("Sound", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
            SoundToggle(
                icon = if (effectsEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                label = "Sound effects",
                checked = effectsEnabled,
                onCheckedChange = {
                    effectsEnabled = it
                    soundManager.setSoundEnabled(it)
                },
            )
            SoundToggle(
                icon = Icons.Default.MusicNote,
                label = "Background music",
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null)
        Text(label, modifier = Modifier.weight(1f).padding(horizontal = 12.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
