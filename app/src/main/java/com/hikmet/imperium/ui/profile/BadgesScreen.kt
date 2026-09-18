package com.hikmet.imperium.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.R
import com.hikmet.imperium.retrofit.BadgeSection
import com.hikmet.imperium.retrofit.BadgeViewModel
import com.hikmet.imperium.ui.components.ImperialScreenBackground

@Composable
fun BadgesScreen(
    navController: NavHostController,
    badgeViewModel: BadgeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val overview by profileViewModel.progress.collectAsStateWithLifecycle()
    val badgeState by badgeViewModel.badgeState.collectAsStateWithLifecycle()
    val stars = overview?.totalStars ?: 0

    LaunchedEffect(overview?.totalStars) {
        overview?.let { badgeViewModel.refreshBadges(it.totalStars) }
    }

    ImperialScreenBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ProfileSubpageHeader(
                    title = stringResource(R.string.badges_title),
                    subtitle = stringResource(R.string.badges_subtitle),
                    onBack = navController::navigateUp,
                )
            },
        ) { padding ->
            BadgeSection(
                badgeState = badgeState,
                userStars = stars,
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        }
    }
}
