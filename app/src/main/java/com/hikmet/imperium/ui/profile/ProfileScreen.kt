package com.hikmet.imperium.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.retrofit.BadgeSection
import com.hikmet.imperium.retrofit.BadgeViewModel
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.Primary
import com.hikmet.imperium.ui.theme.Secondary
import com.hikmet.imperium.ui.util.SoundManager

/**
 * User profile data
 */
data class UserProfile(
    val username: String,
    val joinDate: String,
    val avatarResId: Int,
    val achievements: Int,
    val totalScore: Int,
    val quizzesTaken: Int,
    val totalStars: Int = 75 // For demo purposes, will be calculated from real data later
)

/**
 * Sample user profile for preview
 */
val sampleUserProfile = UserProfile(
    username = "HistoryBuff42",
    joinDate = "Member since March 2023",
    avatarResId = R.drawable.ic_launcher_foreground,
    achievements = 12,
    totalScore = 1243,
    quizzesTaken = 78
)

/**
 * Profile screen showing user info, badges, and sound settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userProfile: UserProfile = sampleUserProfile,
    badgeViewModel: BadgeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val badgeState by badgeViewModel.badgeState.collectAsState()
    
    // Load badges when screen loads
    LaunchedEffect(Unit) {
        badgeViewModel.refreshBadges(userProfile.totalStars)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // User profile section
                UserInfoSection(userProfile = userProfile)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Badge system section
                BadgeSection(
                    badgeState = badgeState,
                    userStars = userProfile.totalStars
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sound settings section (only remaining setting)
                SoundSettingsSection(soundManager = profileViewModel.soundManager)
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    )
}

/**
 * User info section with avatar and stats - Enhanced design
 */
@Composable
fun UserInfoSection(userProfile: UserProfile) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Primary.copy(alpha = 0.1f),
                                Secondary.copy(alpha = 0.05f)
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                // Enhanced Avatar with shadow and border
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            clip = false
                        )
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            width = 4.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Primary, Secondary)
                            )
                        )
                    ) {
                        Image(
                            painter = painterResource(id = userProfile.avatarResId),
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Enhanced Username with gradient text effect
                Text(
                    text = userProfile.username,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Join date with icon
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = userProfile.joinDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Enhanced stats with cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EnhancedStatItem(
                        value = userProfile.totalScore.toString(),
                        label = "Total Score",
                        modifier = Modifier.weight(1f)
                    )
                    
                    EnhancedStatItem(
                        value = userProfile.quizzesTaken.toString(),
                        label = "Quizzes Taken",
                        modifier = Modifier.weight(1f)
                    )
                    
                    EnhancedStatItem(
                        value = userProfile.totalStars.toString(),
                        label = "Total Stars",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}


/**
 * Enhanced sound settings section with SoundManager integration
 */
@Composable
fun SoundSettingsSection(soundManager: SoundManager) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            EnhancedSoundToggle(soundManager = soundManager)
            
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}





/**
 * Enhanced stat item with card design
 */
@Composable
fun EnhancedStatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * User stat item shown in profile header (kept for compatibility)
 */
@Composable
fun StatItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Enhanced sound toggle with SoundManager integration
 */
@Composable
fun EnhancedSoundToggle(soundManager: SoundManager) {
    var isSoundEnabled by remember { mutableStateOf(soundManager.isSoundEnabled()) }
    
    // Update the toggle state when the component recomposes or when soundManager state changes
    LaunchedEffect(soundManager) {
        isSoundEnabled = soundManager.isSoundEnabled()
    }
    
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSoundEnabled) 
                    Primary.copy(alpha = 0.2f) 
                else 
                    MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Sound setting icon",
                        tint = if (isSoundEnabled) Primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sound Effects",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = if (isSoundEnabled) "All sounds enabled" else "All sounds disabled",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Switch(
                checked = isSoundEnabled,
                onCheckedChange = { enabled ->
                    isSoundEnabled = enabled
                    soundManager.setSoundEnabled(enabled)
                    
                    // Also control background music
                    soundManager.setMusicEnabled(enabled)
                    
                    if (enabled) {
                        soundManager.playButtonClick() // Give immediate feedback
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = Primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ImperiumTheme {
        ProfileScreen(rememberNavController())
    }
} 