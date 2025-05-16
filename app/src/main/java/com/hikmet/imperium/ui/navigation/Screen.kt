package com.hikmet.imperium.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen") // Assuming you have/need a splash screen route
    object Home : Screen("home_screen") // Assuming you have/need a home screen route
    object RenaissanceLevelScreen : Screen("renaissance_level_screen")
    object ModernHistoryLevelScreen : Screen("modern_history_level_screen")
    // Generic quiz route, actual parameters are appended in NavGraph
    object Quiz : Screen("quiz_route") 
    // Add other screens as needed, e.g.:
    // object Profile : Screen("profile_screen")
    // object MedievalLevelScreen : Screen("medieval_level_screen")
} 