package com.hikmet.imperium.ui.navigation

import android.util.Log
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
import com.hikmet.imperium.ui.level.MedievalLevelScreen
import com.hikmet.imperium.ui.profile.ProfileScreen
import com.hikmet.imperium.ui.progress.ProgressScreen
import com.hikmet.imperium.ui.quiz.QuizScreen
import com.hikmet.imperium.ui.quiz.MedievalQuizScreen
import com.hikmet.imperium.ui.quiz.AncientQuizScreen
import com.hikmet.imperium.ui.results.ResultsScreen
import com.hikmet.imperium.ui.results.MedievalResultsScreen
import com.hikmet.imperium.ui.splash.SplashScreen
import com.hikmet.imperium.ui.viewmodel.CategoryViewModel
import com.hikmet.imperium.ui.viewmodel.QuizViewModel

/**
 * Defines navigation routes for the Imperium app
 */
object NavDestinations {
    const val SPLASH_ROUTE = "splash"
    const val HOME_ROUTE = "home"
    const val CATEGORY_ROUTE = "category"
    const val LEVEL_SELECTION_ROUTE = "levels/{categoryId}"
    const val LEVEL_ROUTE = "level/{categoryId}/{levelId}"
    const val QUIZ_ROUTE = "quiz/{categoryId}/{levelId}/{quizType}"
    const val RESULTS_ROUTE = "results/{categoryId}/{levelId}/{score}/{stars}/{correct}/{total}"
    const val PROGRESS_ROUTE = "progress"
    const val PROFILE_ROUTE = "profile"
    
    // Medieval routes
    const val MEDIEVAL_LEVEL_ROUTE = "medieval_levels"
    const val MEDIEVAL_QUIZ_ROUTE = "medieval_quiz/{levelId}"
    const val MEDIEVAL_RESULTS_ROUTE = "medieval_results/{levelId}/{score}/{stars}/{correctAnswers}/{totalQuestions}"

    // Ancient routes
    const val ANCIENT_QUIZ_ROUTE = "ancient_quiz/{levelId}"
    const val ANCIENT_RESULTS_ROUTE = "ancient_results/{levelId}/{score}/{stars}/{correctAnswers}/{totalQuestions}"

    fun getMedievalQuizRoute(levelId: String): String {
        return MEDIEVAL_QUIZ_ROUTE.replace("{levelId}", levelId)
    }

    fun getMedievalResultsRoute(
        levelId: String, 
        score: Int, 
        stars: Int, 
        correctAnswers: Int, 
        totalQuestions: Int
    ): String {
        return MEDIEVAL_RESULTS_ROUTE
            .replace("{levelId}", levelId)
            .replace("{score}", score.toString())
            .replace("{stars}", stars.toString())
            .replace("{correctAnswers}", correctAnswers.toString())
            .replace("{totalQuestions}", totalQuestions.toString())
    }
    
    fun getAncientQuizRoute(levelId: String): String {
        return ANCIENT_QUIZ_ROUTE.replace("{levelId}", levelId)
    }

    fun getAncientResultsRoute(
        levelId: String, 
        score: Int, 
        stars: Int, 
        correctAnswers: Int, 
        totalQuestions: Int
    ): String {
        return ANCIENT_RESULTS_ROUTE
            .replace("{levelId}", levelId)
            .replace("{score}", score.toString())
            .replace("{stars}", stars.toString())
            .replace("{correctAnswers}", correctAnswers.toString())
            .replace("{totalQuestions}", totalQuestions.toString())
    }
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
        
        // Category screen
        composable(
            route = NavDestinations.CATEGORY_ROUTE + "/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                    defaultValue = "medieval"
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "medieval"
            CategoryScreen(navController = navController, categoryId = categoryId)
        }
        
        // Level selection screen
        composable(
            route = NavDestinations.LEVEL_SELECTION_ROUTE,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            
            // Route to the appropriate level screen based on category
            when (categoryId) {
                "medieval" -> MedievalLevelScreen(navController = navController)
                else -> LevelScreen(
                    navController = navController,
                    categoryId = categoryId
                )
            }
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
            
            // Route to specific quiz screens based on category
            when (categoryId) {
                "ancient" -> AncientQuizScreen(navController, levelId)
                "medieval" -> MedievalQuizScreen(navController, levelId)
                else -> QuizScreen(navController, categoryId, levelId, quizType)
            }
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
            
            // Generic results screen for other categories
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
        
        // Medieval-specific routes
        // Medieval quiz screen
        composable(
            route = NavDestinations.MEDIEVAL_QUIZ_ROUTE,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId") ?: "1"
            MedievalQuizScreen(
                navController = navController,
                levelId = levelId
            )
        }
        
        // Medieval results screen
        composable(
            route = NavDestinations.MEDIEVAL_RESULTS_ROUTE,
            arguments = listOf(
                navArgument("levelId") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("stars") { type = NavType.IntType },
                navArgument("correctAnswers") { type = NavType.IntType },
                navArgument("totalQuestions") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId") ?: "1"
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val stars = backStackEntry.arguments?.getInt("stars") ?: 0
            val correctAnswers = backStackEntry.arguments?.getInt("correctAnswers") ?: 0
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 0
            
            MedievalResultsScreen(
                navController = navController,
                levelId = levelId,
                score = score,
                stars = stars,
                correctAnswers = correctAnswers,
                totalQuestions = totalQuestions
            )
        }
        
        // Ancient-specific routes
        // Ancient quiz screen
        composable(
            route = NavDestinations.ANCIENT_QUIZ_ROUTE,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId") ?: "1"
            AncientQuizScreen(
                navController = navController,
                levelId = levelId
            )
        }
        
        // Ancient results screen
        composable(
            route = NavDestinations.ANCIENT_RESULTS_ROUTE,
            arguments = listOf(
                navArgument("levelId") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("stars") { type = NavType.IntType },
                navArgument("correctAnswers") { type = NavType.IntType },
                navArgument("totalQuestions") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId") ?: "1"
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val stars = backStackEntry.arguments?.getInt("stars") ?: 0
            val correctAnswers = backStackEntry.arguments?.getInt("correctAnswers") ?: 0
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 0
            
            // For now, reuse the MedievalResultsScreen but pass the category ID as "ancient"
            MedievalResultsScreen(
                navController = navController,
                levelId = levelId,
                score = score,
                stars = stars,
                correctAnswers = correctAnswers,
                totalQuestions = totalQuestions,
                categoryId = "ancient"
            )
        }
    }
} 