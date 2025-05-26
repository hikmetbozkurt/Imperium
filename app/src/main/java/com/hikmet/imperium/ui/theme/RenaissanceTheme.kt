package com.hikmet.imperium.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color

// Color is already imported if used, ImperiumCategoryColors and ImperiumCategoryTheme are now in Theme.kt (same package)

// Define the specific colors for the Renaissance theme
val RenaissancePrimary = Color(0xFF8B0000) // Dark Red
val RenaissancePrimaryVariant = Color(0xFFB22222) // Firebrick
val RenaissanceSecondary = Color(0xFFD4AF37) // Gold
val RenaissanceBackground = Color(0xFFFAF0E6) // Linen
val RenaissanceSurface = Color(0xFFFFF8DC)   // Cornsilk
val RenaissanceOnPrimary = Color.White
val RenaissanceOnSecondary = Color.Black
val RenaissanceOnBackground = Color(0xFF3B2F2F) // Dark brown for text
val RenaissanceOnSurface = Color(0xFF3B2F2F)

val RenaissanceCategoryColorsObject = ImperiumCategoryColors(
    primary = RenaissancePrimary,
    primaryVariant = RenaissancePrimaryVariant,
    secondary = RenaissanceSecondary,
    background = RenaissanceBackground,
    surface = RenaissanceSurface,
    onPrimary = RenaissanceOnPrimary,
    onSecondary = RenaissanceOnSecondary,
    onBackground = RenaissanceOnBackground,
    onSurface = RenaissanceOnSurface,
    titleColor = RenaissanceOnPrimary,
    subtitleColor = RenaissanceOnPrimary.copy(alpha = 0.8f),
    iconColor = RenaissanceOnPrimary,
    progressIndicatorColor = RenaissanceSecondary,
    levelCardBackground = RenaissanceSurface,
    levelCardUnlockedTextColor = RenaissanceOnSurface,
    levelCardLockedTextColor = RenaissanceOnSurface.copy(alpha = 0.6f),
    levelCardUnlockedIconColor = RenaissancePrimary,
    levelCardLockedIconColor = RenaissanceOnSurface.copy(alpha = 0.4f)
)

object RenaissanceTheme {
    val colors: ImperiumCategoryColors @Composable get() = RenaissanceCategoryColorsObject
    // Use the global Typography from Type.kt (which is MaterialTheme.typography or your custom one)
    val typography: Typography @Composable get() = MaterialTheme.typography 

    @Composable
    operator fun invoke(content: @Composable () -> Unit) {
        ImperiumCategoryTheme(
            colors = this.colors,
            typography = this.typography,
            content = content
        )
    }
} 