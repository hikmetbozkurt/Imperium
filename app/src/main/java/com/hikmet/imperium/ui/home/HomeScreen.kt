package com.hikmet.imperium.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.category.categoryDetails
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.AncientGradientEnd
import com.hikmet.imperium.ui.theme.AncientGradientStart
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.MedievalGradientEnd
import com.hikmet.imperium.ui.theme.MedievalGradientStart
import com.hikmet.imperium.ui.theme.ModernGradientEnd
import com.hikmet.imperium.ui.theme.ModernGradientStart
import com.hikmet.imperium.ui.theme.RenaissanceGradientEnd
import com.hikmet.imperium.ui.theme.RenaissanceGradientStart
import com.hikmet.imperium.ui.theme.WorldWarsGradientEnd
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart
import kotlinx.coroutines.delay

/**
 * Bottom navigation items
 */
sealed class BottomNavItem(val route: String, val title: Int, val icon: ImageVector) {
    object Home : BottomNavItem(NavDestinations.HOME_ROUTE, R.string.nav_home, Icons.Default.Home)
    object Progress : BottomNavItem(NavDestinations.PROGRESS_ROUTE, R.string.nav_progress, Icons.Default.ShowChart)
    object Profile : BottomNavItem(NavDestinations.PROFILE_ROUTE, R.string.nav_profile, Icons.Default.Person)
}

val BottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Progress,
    BottomNavItem.Profile
)

/**
 * Color scheme data class for category cards
 */
data class CategoryColorScheme(
    val startColorResId: Int,
    val endColorResId: Int
)

/**
 * Map of category color schemes
 */
val categoryColorSchemes = mapOf(
    "ancient" to CategoryColorScheme(R.color.ancient_gradient_start, R.color.ancient_gradient_end),
    "medieval" to CategoryColorScheme(R.color.medieval_gradient_start, R.color.medieval_gradient_end),
    "renaissance" to CategoryColorScheme(R.color.renaissance_gradient_start, R.color.renaissance_gradient_end),
    "modern" to CategoryColorScheme(R.color.modern_gradient_start, R.color.modern_gradient_end),
    "world_wars" to CategoryColorScheme(R.color.worldwars_gradient_start, R.color.worldwars_gradient_end)
)

/**
 * Data class for categories
 */
data class HistoryCategory(
    val id: String,
    val titleRes: Int,
    val descriptionRes: Int,
    val imageResId: Int,
    val completion: Int, // percentage of completion
    val colorScheme: CategoryColorScheme
)

/**
 * Sample data for preview
 */
val sampleCategories = listOf(
    HistoryCategory(
        "ancient", 
        R.string.category_ancient_title, 
        R.string.category_ancient_desc,
        R.drawable.ic_ancient, 
        75,
        categoryColorSchemes["ancient"]!!
    ),
    HistoryCategory(
        "medieval", 
        R.string.category_medieval_title, 
        R.string.category_medieval_desc,
        R.drawable.ic_medieval, 
        30,
        categoryColorSchemes["medieval"]!!
    ),
    HistoryCategory(
        "renaissance", 
        R.string.category_renaissance_title,
        R.string.category_renaissance_desc,
        R.drawable.ic_renaissance, 
        0,
        categoryColorSchemes["renaissance"]!!
    ),
    HistoryCategory(
        "modern", 
        R.string.category_modern_title, 
        R.string.category_modern_desc,
        R.drawable.ic_modern, 
        10,
        categoryColorSchemes["modern"]!!
    ),
    HistoryCategory(
        "world_wars", 
        R.string.category_worldwars_title, 
        R.string.category_worldwars_desc,
        R.drawable.ic_wars, 
        50,
        categoryColorSchemes["world_wars"]!!
    )
)

/**
 * Home screen with categories and bottom navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    
    // Get colors from resources
    val backgroundColor = colorResource(id = R.color.home_background)
    val textColor = colorResource(id = R.color.home_text_color)
    val textSecondaryColor = colorResource(id = R.color.home_text_secondary)
    val dividerColor = colorResource(id = R.color.home_divider)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.app_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = textColor
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textColor
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = backgroundColor,
                contentColor = textColor
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                BottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(imageVector = screen.icon, contentDescription = stringResource(screen.title)) 
                        },
                        label = { Text(stringResource(screen.title)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Welcome text centered with modern styling
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.welcome_prefix),
                            style = MaterialTheme.typography.titleMedium,
                            color = textSecondaryColor,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = stringResource(R.string.welcome_title),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            ),
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Divider(
                            modifier = Modifier.width(80.dp),
                            color = dividerColor,
                            thickness = 2.dp
                        )
                    }
                }
                
                // Categories header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.categories_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        
                        Text(
                            text = stringResource(R.string.explore_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = textSecondaryColor
                        )
                    }
                }
                
                // Using fixed height for the grid to avoid the infinity constraints error
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.height(600.dp) // Fixed height to prevent infinite height constraints
                ) {
                    items(sampleCategories) { category ->
                        CategoryCard(category = category, onClick = {
                            try {
                                navController.navigate("category/${category.id}")
                            } catch (e: Exception) {
                                // Fail silently
                            }
                        })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: HistoryCategory, onClick: () -> Unit) {
    // Get overlay colors from resources
    val overlayStartColor = colorResource(id = R.color.category_overlay_start)
    val overlayEndColor = colorResource(id = R.color.category_overlay_end)
    
    // Get the gradient colors for this specific category from resources
    val gradientStartColor = colorResource(id = category.colorScheme.startColorResId)
    val gradientEndColor = colorResource(id = category.colorScheme.endColorResId)
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientStartColor,
                            gradientEndColor
                        )
                    )
                )
        ) {
            // Add a subtle pattern/overlay for modern gaming feel
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                overlayStartColor,
                                overlayEndColor
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(category.titleRes),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = colorResource(id = R.color.white)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = stringResource(category.descriptionRes),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(id = R.color.white).copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ImperiumTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    ImperiumTheme {
        CategoryCard(
            category = sampleCategories.first(),
            onClick = {}
        )
    }
} 