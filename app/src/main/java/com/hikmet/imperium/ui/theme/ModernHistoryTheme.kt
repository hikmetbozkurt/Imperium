package com.hikmet.imperium.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// Define the specific colors for the Modern History theme
val ModernHistoryPrimary = Color(0xFF4A90E2) // A medium, slightly desaturated blue
val ModernHistoryPrimaryVariant = Color(0xFF357ABD) // A darker shade for variants
val ModernHistorySecondary = Color(0xFF50E3C2) // A bright, modern teal/turquoise
val ModernHistoryBackground = Color(0xFFF4F6F8) // A very light, clean gray
val ModernHistorySurface = Color.White
val ModernHistoryOnPrimary = Color.White
val ModernHistoryOnSecondary = Color.Black
val ModernHistoryOnBackground = Color(0xFF222222) // Dark gray for text on light background
val ModernHistoryOnSurface = Color(0xFF222222) // Dark gray for text on surface

val ModernHistoryCategoryColorsObject = ImperiumCategoryColors(
    primary = ModernHistoryPrimary,
    primaryVariant = ModernHistoryPrimaryVariant,
    secondary = ModernHistorySecondary,
    background = ModernHistoryBackground,
    surface = ModernHistorySurface,
    onPrimary = ModernHistoryOnPrimary,
    onSecondary = ModernHistoryOnSecondary,
    onBackground = ModernHistoryOnBackground,
    onSurface = ModernHistoryOnSurface,
    titleColor = ModernHistoryOnPrimary,
    subtitleColor = ModernHistoryOnPrimary.copy(alpha = 0.8f),
    iconColor = ModernHistoryOnPrimary,
    progressIndicatorColor = ModernHistorySecondary,
    levelCardBackground = ModernHistorySurface,
    levelCardUnlockedTextColor = ModernHistoryOnSurface,
    levelCardLockedTextColor = ModernHistoryOnSurface.copy(alpha = 0.6f),
    levelCardUnlockedIconColor = ModernHistoryPrimary,
    levelCardLockedIconColor = ModernHistoryOnSurface.copy(alpha = 0.4f)
)

// The actual theme object that your screens will use
object ModernHistoryTheme {
    val colors: ImperiumCategoryColors @Composable get() = ModernHistoryCategoryColorsObject
    val typography: Typography @Composable get() = MaterialTheme.typography // Or your custom ImperiumTypography

    @Composable
    operator fun invoke(content: @Composable () -> Unit) {
        ImperiumCategoryTheme(
            colors = this.colors,
            typography = this.typography,
            content = content
        )
    }
} 