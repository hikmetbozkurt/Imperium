package com.hikmet.imperium.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hikmet.imperium.ui.navigation.NavDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val categories by viewModel.categoriesState.collectAsStateWithLifecycle()

    DisposableEffect(viewModel.soundManager) {
        viewModel.soundManager.startBackgroundMusic()
        onDispose(viewModel.soundManager::pauseBackgroundMusic)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Imperium", fontWeight = FontWeight.Bold) }) },
        bottomBar = {
            NavigationBar {
                NavigationItem("Home", Icons.Default.Home, selected = true) { }
                NavigationItem("Progress", Icons.AutoMirrored.Filled.ShowChart) {
                    navController.navigate(NavDestinations.PROGRESS_ROUTE)
                }
                NavigationItem("Profile", Icons.Default.Person) {
                    navController.navigate(NavDestinations.PROFILE_ROUTE)
                }
            }
        },
    ) { padding ->
        if (categories.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Text("Choose an era", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Build your knowledge one carefully designed level at a time.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                }
                items(categories, key = { it.content.id.value }) { category ->
                    CategoryCard(category) {
                        navController.navigate("${NavDestinations.CATEGORY_ROUTE}/${category.content.id.value}")
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: HomeCategory, onClick: () -> Unit) {
    val content = category.content
    val progress = category.progress
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(190.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(listOf(Color(content.gradientStartColor), Color(content.gradientEndColor))),
                )
                .padding(20.dp),
        ) {
            Text(content.title, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(content.description, color = Color.White.copy(alpha = 0.88f), modifier = Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                Text(" ${progress.totalStars}", color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(
                    "  ·  ${progress.unlockedLevels}/${content.levels.size} unlocked",
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.weight(1f),
                )
                Box(
                    Modifier.size(42.dp).background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Open", tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.NavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) },
    )
}
