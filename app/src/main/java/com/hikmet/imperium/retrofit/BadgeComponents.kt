package com.hikmet.imperium.retrofit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hikmet.imperium.ui.theme.Primary

/**
 * Main badge section for the Profile screen
 */
@Composable
fun BadgeSection(
    badgeState: ApiResult<List<Badge>>,
    userStars: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "My Badges",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        when (badgeState) {
            is ApiResult.Loading -> {
                BadgeLoadingState()
            }
            is ApiResult.Success -> {
                BadgeList(badges = badgeState.data, userStars = userStars)
            }
            is ApiResult.Error -> {
                BadgeErrorState(message = badgeState.message)
            }
        }
    }
}

/**
 * Loading state for badges
 */
@Composable
private fun BadgeLoadingState() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            CircularProgressIndicator(
                color = Primary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

/**
 * Error state for badges
 */
@Composable
private fun BadgeErrorState(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Failed to load badges",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * List of badges
 */
@Composable
private fun BadgeList(
    badges: List<Badge>,
    userStars: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp), // Limit height so it doesn't take up the whole screen
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(badges) { badge ->
            BadgeItem(
                badge = badge,
                isEarned = badge.isEarned(userStars),
                userStars = userStars
            )
        }
    }
}

/**
 * Individual badge item
 */
@Composable
private fun BadgeItem(
    badge: Badge,
    isEarned: Boolean,
    userStars: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isEarned) 6.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned) 
                Primary.copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge icon
            BadgeIcon(
                isEarned = isEarned,
                progress = if (isEarned) 1.0f else badge.getProgress(userStars)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Badge content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isEarned) 
                        Primary 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isEarned) 
                        MaterialTheme.colorScheme.onSurface 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (!isEarned) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${badge.minStars - userStars} more stars needed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Stars requirement
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isEarned) 
                    Primary.copy(alpha = 0.2f) 
                else 
                    MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        tint = if (isEarned) Primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "${badge.minStars}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isEarned) Primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Badge icon with different states
 */
@Composable
private fun BadgeIcon(
    isEarned: Boolean,
    progress: Float
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(56.dp)
    ) {
        // Background circle
        Surface(
            shape = CircleShape,
            color = if (isEarned) 
                Primary.copy(alpha = 0.2f) 
            else 
                MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .size(56.dp)
                .alpha(if (isEarned) 1.0f else 0.6f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isEarned) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Earned badge",
                        tint = Primary,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked badge",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        
        // Progress border for locked badges
        if (!isEarned && progress > 0) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(
                        width = 3.dp,
                        color = Primary.copy(alpha = progress),
                        shape = CircleShape
                    )
            )
        }
    }
} 