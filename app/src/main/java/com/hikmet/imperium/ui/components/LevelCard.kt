package com.hikmet.imperium.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.ui.theme.ImperiumCategoryColors
import com.hikmet.imperium.ui.util.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelCard(
    level: LevelEntity,
    isUnlocked: Boolean,
    userStarsForLevel: Int,
    colors: ImperiumCategoryColors,
    onClick: () -> Unit
) {
    val cardBackgroundColor = if (isUnlocked) colors.levelCardBackground else colors.levelCardBackground.copy(alpha = 0.6f)
    val contentColor = if (isUnlocked) colors.levelCardUnlockedTextColor else colors.levelCardLockedTextColor
    val iconColor = if (isUnlocked) colors.levelCardUnlockedIconColor else colors.levelCardLockedIconColor

    Card(
        onClick = onClick,
        modifier = Modifier
            .size(width = 88.dp, height = 110.dp), // Unified size across all level screens
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        border = if (!isUnlocked) BorderStroke(1.dp, colors.primary.copy(alpha = 0.5f)) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isUnlocked) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Level ${level.levelNumber}", // Assuming LevelEntity has levelNumber
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = contentColor
            )

            if (isUnlocked) {
                // Display stars if unlocked
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Stars",
                        tint = if (userStarsForLevel > 0) Color.Yellow else iconColor.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "$userStarsForLevel",
                        fontSize = 14.sp,
                        color = contentColor,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                // Potentially display level.title or an icon here if LevelEntity has it
                // Text(text = level.title, fontSize = 10.sp, color = contentColor, maxLines = 1, overflow = TextOverflow.Ellipsis)

            } else {
                // Display lock icon if locked
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Locked",
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp)) // Spacer to push lock icon up a bit
            }
            
            // Placeholder for bottom content or to ensure spacing
             Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

/**
 * Alternative LevelCard implementation for Renaissance and other screens
 * that don't directly use LevelEntity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelCard(
    levelNumber: String,
    isCompleted: Boolean,
    stars: Int,
    requiredStars: Int,
    isUnlocked: Boolean,
    primaryColor: Color,
    gradientStart: Color,
    gradientEnd: Color,
    bestTimeMs: Long? = null,
    onClick: () -> Unit
) {
    val cardBackgroundColor = if (isUnlocked) Color.White else Color.White.copy(alpha = 0.6f)
    val contentColor = if (isUnlocked) Color.Black else Color.Gray
    val iconColor = if (isUnlocked) primaryColor else Color.Gray.copy(alpha = 0.4f)

    Card(
        onClick = onClick,
        modifier = Modifier
            .size(width = 88.dp, height = 110.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        border = if (!isUnlocked) BorderStroke(1.dp, primaryColor.copy(alpha = 0.5f)) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isUnlocked) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Level $levelNumber",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = contentColor
            )

            if (isUnlocked) {
                // Display stars if unlocked
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Stars",
                        tint = if (stars > 0) Color.Yellow else iconColor.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "$stars",
                        fontSize = 14.sp,
                        color = contentColor,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                // Display best time if available
                if (bestTimeMs != null && bestTimeMs > 0) {
                    Text(
                        text = formatTime(bestTimeMs),
                        fontSize = 10.sp,
                        color = contentColor.copy(alpha = 0.7f)
                    )
                }
            } else {
                // Display lock icon if locked
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Locked",
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                
                // Display required stars to unlock
                Text(
                    text = "${requiredStars}★",
                    fontSize = 12.sp,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
} 