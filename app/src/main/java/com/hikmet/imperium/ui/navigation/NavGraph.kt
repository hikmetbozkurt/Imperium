package com.hikmet.imperium.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hikmet.imperium.ImperiumApplication
import com.hikmet.imperium.ui.category.CategoryScreen
import com.hikmet.imperium.ui.home.HomeScreen
import com.hikmet.imperium.ui.level.LevelScreen
import com.hikmet.imperium.ui.profile.ProfileScreen
import com.hikmet.imperium.ui.progress.ProgressScreen
import com.hikmet.imperium.ui.quiz.QuizScreen
import com.hikmet.imperium.ui.results.ResultsScreen
import com.hikmet.imperium.ui.splash.SplashScreen
import com.hikmet.imperium.ui.viewmodel.CategoryViewModel
import com.hikmet.imperium.ui.viewmodel.QuizViewModel

/**
 * Defines navigation routes for the Imperium app
 */
object NavDestinations {
    const val SPLASH_ROUTE = "splash"
    const val HOME_ROUTE = "home"
    const val CATEGORY_ROUTE = "category/{categoryId}"
    const val LEVEL_SELECTION_ROUTE = "category/{categoryId}/levels"
    const val LEVEL_ROUTE = "level/{categoryId}/{levelId}"
    const val QUIZ_ROUTE = "quiz/{categoryId}/{levelId}/{quizType}"
    const val RESULTS_ROUTE = "results/{categoryId}/{levelId}/{score}/{stars}/{correct}/{total}"
    const val PROGRESS_ROUTE = "progress"
    const val PROFILE_ROUTE = "profile"
}

/**
 * Creates the navigation graph for the application
 */
@Composable
fun ImperiumNavGraph(
    navController: NavHostController,
    startDestination: String = NavDestinations.HOME_ROUTE
) {
    // Get application context for repository access
    val context = LocalContext.current
    val application = context.applicationContext as ImperiumApplication
    val repository = application.repository
    
    // Create ViewModels
    val categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModel.Factory(repository)
    )
    
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
        composable(
            route = NavDestinations.CATEGORY_ROUTE,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "ancient"
            CategoryScreen(navController, categoryId)
        }
        
        // Level selection screen
        composable(
            route = NavDestinations.LEVEL_SELECTION_ROUTE,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "ancient"
            LevelScreen(navController, categoryId)
        }
        
        // Quiz screen
        composable(
            route = NavDestinations.QUIZ_ROUTE,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("levelId") { type = NavType.StringType },
                navArgument("quizType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "ancient"
            val levelId = backStackEntry.arguments?.getString("levelId") ?: "1"
            val quizType = backStackEntry.arguments?.getString("quizType") ?: "STANDARD"
            QuizScreen(navController, categoryId, levelId, quizType)
        }
        
        // Results screen
        composable(
            route = NavDestinations.RESULTS_ROUTE,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("levelId") { type = NavType.StringType },
                navArgument("score") { type = NavType.StringType },
                navArgument("stars") { type = NavType.StringType },
                navArgument("correct") { type = NavType.StringType },
                navArgument("total") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "ancient"
            val levelId = backStackEntry.arguments?.getString("levelId") ?: "1"
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val stars = backStackEntry.arguments?.getString("stars")?.toIntOrNull() ?: 0
            val correct = backStackEntry.arguments?.getString("correct")?.toIntOrNull() ?: 0
            val total = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 4
            ResultsScreen(
                navController = navController,
                categoryId = categoryId,
                levelId = levelId,
                score = score,
                stars = stars,
                correctAnswers = correct,
                totalQuestions = total
            )
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