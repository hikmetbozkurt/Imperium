package com.hikmet.imperium.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
// Import all color definitions from Color.kt
import com.hikmet.imperium.ui.theme.Primary
import com.hikmet.imperium.ui.theme.OnPrimary
import com.hikmet.imperium.ui.theme.PrimaryContainer
import com.hikmet.imperium.ui.theme.OnPrimaryContainer
import com.hikmet.imperium.ui.theme.Secondary
import com.hikmet.imperium.ui.theme.OnSecondary
import com.hikmet.imperium.ui.theme.SecondaryContainer
import com.hikmet.imperium.ui.theme.OnSecondaryContainer
import com.hikmet.imperium.ui.theme.Accent
import com.hikmet.imperium.ui.theme.OnAccent
import com.hikmet.imperium.ui.theme.AccentContainer
import com.hikmet.imperium.ui.theme.OnAccentContainer
import com.hikmet.imperium.ui.theme.Error
import com.hikmet.imperium.ui.theme.OnError
import com.hikmet.imperium.ui.theme.ErrorContainer
import com.hikmet.imperium.ui.theme.OnErrorContainer
import com.hikmet.imperium.ui.theme.Background
import com.hikmet.imperium.ui.theme.OnBackground
import com.hikmet.imperium.ui.theme.Surface
import com.hikmet.imperium.ui.theme.OnSurface
import com.hikmet.imperium.ui.theme.SurfaceVariant
import com.hikmet.imperium.ui.theme.OnSurfaceVariant
import com.hikmet.imperium.ui.theme.Outline
// Add missing dark theme color imports
import com.hikmet.imperium.ui.theme.DarkPrimary
import com.hikmet.imperium.ui.theme.DarkOnPrimary
import com.hikmet.imperium.ui.theme.DarkPrimaryContainer
import com.hikmet.imperium.ui.theme.DarkOnPrimaryContainer
import com.hikmet.imperium.ui.theme.DarkSecondary
import com.hikmet.imperium.ui.theme.DarkOnSecondary
import com.hikmet.imperium.ui.theme.DarkSecondaryContainer
import com.hikmet.imperium.ui.theme.DarkOnSecondaryContainer
import com.hikmet.imperium.ui.theme.DarkAccent
import com.hikmet.imperium.ui.theme.DarkOnAccent
import com.hikmet.imperium.ui.theme.DarkAccentContainer
import com.hikmet.imperium.ui.theme.DarkOnAccentContainer
import com.hikmet.imperium.ui.theme.DarkError
import com.hikmet.imperium.ui.theme.DarkOnError
import com.hikmet.imperium.ui.theme.DarkErrorContainer
import com.hikmet.imperium.ui.theme.DarkOnErrorContainer
import com.hikmet.imperium.ui.theme.DarkBackground
import com.hikmet.imperium.ui.theme.DarkOnBackground
import com.hikmet.imperium.ui.theme.DarkSurface
import com.hikmet.imperium.ui.theme.DarkOnSurface
import com.hikmet.imperium.ui.theme.DarkSurfaceVariant
import com.hikmet.imperium.ui.theme.DarkOnSurfaceVariant
import com.hikmet.imperium.ui.theme.DarkOutline
// Add world wars gradient imports
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart
import com.hikmet.imperium.ui.theme.WorldWarsGradientEnd
import androidx.compose.material3.Typography

/**
 * Haptic feedback utility class for tactile feedback in the app
 */
class HapticFeedback(private val context: Context) {
    companion object {
        const val SUCCESS = 1
        const val ERROR = 2
        const val LIGHT = 3
    }
    
    fun vibrate(type: Int) {
        try {
            val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
            vibrator?.let {
                when (type) {
                    SUCCESS -> {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(50)
                            }
                        } catch (e: Exception) {
                            // Ignore vibration errors
                        }
                    }
                    ERROR -> {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 50, 50), -1))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(longArrayOf(0, 50, 50, 50), -1)
                            }
                        } catch (e: Exception) {
                            // Ignore vibration errors
                        }
                    }
                    LIGHT -> {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(20)
                            }
                        } catch (e: Exception) {
                            // Ignore vibration errors
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Fail silently if vibration is not available
        }
    }
}

// Modern color scheme based on historical themes
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Accent,
    onTertiary = OnAccent,
    tertiaryContainer = AccentContainer,
    onTertiaryContainer = OnAccentContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline
)

// Dark theme version
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkAccent,
    onTertiary = DarkOnAccent,
    tertiaryContainer = DarkAccentContainer,
    onTertiaryContainer = DarkOnAccentContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

// MOVED FROM ModernHistoryTheme.kt - START
data class ImperiumCategoryColors(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val iconColor: Color,
    val progressIndicatorColor: Color,
    val levelCardBackground: Color,
    val levelCardUnlockedTextColor: Color,
    val levelCardLockedTextColor: Color,
    val levelCardUnlockedIconColor: Color,
    val levelCardLockedIconColor: Color
)

@Composable
fun ImperiumCategoryTheme(
    colors: ImperiumCategoryColors,
    typography: Typography, 
    content: @Composable () -> Unit
) {
    content()
}
// MOVED FROM ModernHistoryTheme.kt - END

@Composable
fun ImperiumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Define the specific colors for the World Wars theme
val WorldWarsPrimary = WorldWarsGradientStart
val WorldWarsPrimaryVariant = WorldWarsGradientEnd
val WorldWarsSecondary = Color(0xFFF9B339) // Example accent
val WorldWarsBackground = Color(0xFFF4F6F8) // Light gray background
val WorldWarsSurface = Color.White
val WorldWarsOnPrimary = Color.White
val WorldWarsOnSecondary = Color.Black
val WorldWarsOnBackground = Color(0xFF222222)
val WorldWarsOnSurface = Color(0xFF222222)

val WorldWarsCategoryColorsObject = ImperiumCategoryColors(
    primary = WorldWarsPrimary,
    primaryVariant = WorldWarsPrimaryVariant,
    secondary = WorldWarsSecondary,
    background = WorldWarsBackground,
    surface = WorldWarsSurface,
    onPrimary = WorldWarsOnPrimary,
    onSecondary = WorldWarsOnSecondary,
    onBackground = WorldWarsOnBackground,
    onSurface = WorldWarsOnSurface,
    titleColor = WorldWarsOnPrimary,
    subtitleColor = WorldWarsOnPrimary.copy(alpha = 0.8f),
    iconColor = WorldWarsOnPrimary,
    progressIndicatorColor = WorldWarsSecondary,
    levelCardBackground = WorldWarsSurface,
    levelCardUnlockedTextColor = WorldWarsOnSurface,
    levelCardLockedTextColor = WorldWarsOnSurface.copy(alpha = 0.6f),
    levelCardUnlockedIconColor = WorldWarsPrimary,
    levelCardLockedIconColor = WorldWarsOnSurface.copy(alpha = 0.4f)
)

object WorldWarsTheme {
    val colors: ImperiumCategoryColors @Composable get() = WorldWarsCategoryColorsObject
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