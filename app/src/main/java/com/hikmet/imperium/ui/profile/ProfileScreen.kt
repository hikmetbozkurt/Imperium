package com.hikmet.imperium.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.Primary

/**
 * User profile data
 */
data class UserProfile(
    val username: String,
    val joinDate: String,
    val avatarResId: Int,
    val achievements: Int,
    val totalScore: Int,
    val quizzesTaken: Int
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
 * Profile screen showing user info and settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userProfile: UserProfile = sampleUserProfile
) {
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
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Settings sections
                SettingsSection()
            }
        }
    )
}

/**
 * User info section with avatar and stats
 */
@Composable
fun UserInfoSection(userProfile: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar with border
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .border(
                    width = 3.dp,
                    color = Primary,
                    shape = CircleShape
                )
                .padding(3.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = painterResource(id = userProfile.avatarResId),
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Username
        Text(
            text = userProfile.username,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        // Join date
        Text(
            text = userProfile.joinDate,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // User stats in a row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = userProfile.totalScore.toString(),
                label = "Total Score"
            )
            
            StatItem(
                value = userProfile.quizzesTaken.toString(),
                label = "Quizzes Taken"
            )
            
            StatItem(
                value = userProfile.achievements.toString(),
                label = "Achievements"
            )
        }
    }
}

/**
 * Settings section with multiple categories
 */
@Composable
fun SettingsSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // App settings
        SettingsSectionHeader(title = "App Settings")
        
        SettingsSwitchItem(
            icon = Icons.Default.VolumeUp,
            title = "Sound",
            initialValue = true
        )
        
        SettingsSwitchItem(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            initialValue = true
        )
        
        SettingsSwitchItem(
            icon = Icons.Default.DarkMode,
            title = "Dark Theme",
            initialValue = false
        )
        
        SettingsClickableItem(
            icon = Icons.Default.Language,
            title = "Language",
            subtitle = "English"
        )
        
        Divider(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        
        // Account settings
        SettingsSectionHeader(title = "Account")
        
        SettingsClickableItem(
            icon = Icons.Default.Person,
            title = "Edit Profile"
        )
        
        SettingsClickableItem(
            icon = Icons.Default.Badge,
            title = "Achievements",
            subtitle = "${sampleUserProfile.achievements} earned"
        )
        
        Divider(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        
        // About section
        SettingsSectionHeader(title = "About")
        
        SettingsClickableItem(
            icon = Icons.Default.Info,
            title = "About Imperium",
            subtitle = "Version 1.0.0"
        )
        
        SettingsClickableItem(
            icon = Icons.Default.Help,
            title = "Help & Feedback"
        )
        
        SettingsClickableItem(
            icon = Icons.Default.Favorite,
            title = "Rate Us"
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Section header for settings
 */
@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Settings item with a switch toggle
 */
@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    initialValue: Boolean
) {
    var isChecked by remember { mutableStateOf(initialValue) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$title setting icon",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = Primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

/**
 * Settings item that can be clicked
 */
@Composable
fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$title setting icon",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate to $title settings",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * User stat item shown in profile header
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ImperiumTheme {
        ProfileScreen(rememberNavController())
    }
} 