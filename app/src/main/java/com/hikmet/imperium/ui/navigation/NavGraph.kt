package com.hikmet.imperium.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hikmet.imperium.ui.category.CategoryScreen
import com.hikmet.imperium.ui.home.HomeScreen
import com.hikmet.imperium.ui.level.LevelScreen
import com.hikmet.imperium.ui.profile.ProfileScreen
import com.hikmet.imperium.ui.progress.ProgressScreen
import com.hikmet.imperium.ui.quiz.QuizScreen
import com.hikmet.imperium.ui.results.ResultsScreen
import com.hikmet.imperium.ui.splash.SplashScreen

/**
 * Defines navigation routes for the Imperium app
 */
object NavDestinations {
    const val SPLASH_ROUTE = "splash"
    const val HOME_ROUTE = "home"
    const val CATEGORY_ROUTE = "category/{categoryId}"
    const val LEVEL_ROUTE = "level/{categoryId}/{levelId}"
    const val QUIZ_ROUTE = "quiz/{categoryId}/{levelId}/{quizType}"
    const val RESULTS_ROUTE = "results/{categoryId}/{levelId}/{score}"
    const val PROGRESS_ROUTE = "progress"
    const val PROFILE_ROUTE = "profile"
}

/**
 * Creates the navigation graph for the application
 */
@Composable
fun ImperiumNavGraph(
    navController: NavHostController,
    startDestination: String = NavDestinations.SPLASH_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash screen
        composable(NavDestinations.SPLASH_ROUTE) {
            SplashScreen(navController)
        }
        
        // Home screen
        composable(NavDestinations.HOME_ROUTE) {
            HomeScreen(navController)
        }
        
        // Category detail screen
        composable(NavDestinations.CATEGORY_ROUTE) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            CategoryScreen(navController, categoryId)
        }
        
        // Level selection screen
        composable(NavDestinations.LEVEL_ROUTE) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val levelId = backStackEntry.arguments?.getString("levelId")
            LevelScreen(navController, categoryId, levelId)
        }
        
        // Quiz screen
        composable(NavDestinations.QUIZ_ROUTE) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val levelId = backStackEntry.arguments?.getString("levelId")
            val quizType = backStackEntry.arguments?.getString("quizType")
            QuizScreen(navController, categoryId, levelId, quizType)
        }
        
        // Results screen
        composable(NavDestinations.RESULTS_ROUTE) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val levelId = backStackEntry.arguments?.getString("levelId")
            val score = backStackEntry.arguments?.getString("score")
            ResultsScreen(navController, categoryId, levelId, score)
        }
        
        // Progress screen
        composable(NavDestinations.PROGRESS_ROUTE) {
            ProgressScreen(navController)
        }
        
        // Profile screen
        composable(NavDestinations.PROFILE_ROUTE) {
            ProfileScreen(navController)
        }
    }
} 