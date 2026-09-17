package com.hikmet.imperium.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hikmet.imperium.feature.levels.LevelsScreen
import com.hikmet.imperium.feature.quiz.QuizScreen
import com.hikmet.imperium.feature.results.ResultScreen
import com.hikmet.imperium.ui.category.CategoryScreen
import com.hikmet.imperium.ui.home.HomeScreen
import com.hikmet.imperium.ui.home.ExpeditionsScreen
import com.hikmet.imperium.ui.profile.ProfileScreen
import com.hikmet.imperium.ui.progress.ProgressScreen
import com.hikmet.imperium.ui.splash.SplashScreen

object NavDestinations {
    const val SPLASH_ROUTE = "splash"
    const val HOME_ROUTE = "home"
    const val CATEGORY_ROUTE = "category"
    const val EXPEDITIONS_ROUTE = "expeditions"
    const val LEVEL_SELECTION_ROUTE = "levels/{categoryId}"
    const val QUIZ_ROUTE = "quiz/{categoryId}/{levelNumber}"
    const val ATTEMPT_RESULT_ROUTE = "result/{attemptId}"
    const val PROGRESS_ROUTE = "progress"
    const val PROFILE_ROUTE = "profile"

    fun levels(categoryId: String) = "levels/$categoryId"
    fun quiz(categoryId: String, levelNumber: Int) = "quiz/$categoryId/$levelNumber"
    fun result(attemptId: Long) = "result/$attemptId"
}

@Composable
fun ImperiumNavGraph(
    navController: NavHostController,
    startDestination: String = NavDestinations.HOME_ROUTE,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavDestinations.SPLASH_ROUTE) { SplashScreen(navController) }
        composable(NavDestinations.HOME_ROUTE) { HomeScreen(navController) }
        composable(NavDestinations.EXPEDITIONS_ROUTE) { ExpeditionsScreen(navController) }
        composable(
            route = "${NavDestinations.CATEGORY_ROUTE}/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType }),
        ) { entry ->
            CategoryScreen(
                navController = navController,
                categoryId = entry.arguments?.getString("categoryId") ?: "ancient",
            )
        }
        composable(
            route = NavDestinations.LEVEL_SELECTION_ROUTE,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType }),
        ) { entry ->
            val categoryId = entry.arguments?.getString("categoryId") ?: "ancient"
            LevelsScreen(
                onBack = navController::navigateUp,
                onLevelSelected = { level -> navController.navigate(NavDestinations.quiz(categoryId, level)) },
            )
        }
        composable(
            route = NavDestinations.QUIZ_ROUTE,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("levelNumber") { type = NavType.IntType },
            ),
        ) { entry ->
            QuizScreen(
                onBack = navController::navigateUp,
                onResult = { attemptId ->
                    navController.navigate(NavDestinations.result(attemptId)) {
                        popUpTo(entry.destination.id) { inclusive = true }
                    }
                },
            )
        }
        composable(
            route = NavDestinations.ATTEMPT_RESULT_ROUTE,
            arguments = listOf(navArgument("attemptId") { type = NavType.LongType }),
        ) {
            ResultScreen(
                onRetry = { categoryId, level ->
                    navController.navigate(NavDestinations.quiz(categoryId, level))
                },
                onLevels = { categoryId ->
                    navController.navigate(NavDestinations.levels(categoryId)) {
                        popUpTo(NavDestinations.HOME_ROUTE)
                    }
                },
                onHome = {
                    navController.navigate(NavDestinations.HOME_ROUTE) {
                        popUpTo(NavDestinations.HOME_ROUTE) { inclusive = true }
                    }
                },
            )
        }
        composable(NavDestinations.PROGRESS_ROUTE) { ProgressScreen(navController) }
        composable(NavDestinations.PROFILE_ROUTE) { ProfileScreen(navController) }
    }
}
