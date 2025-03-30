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
import androidx.compose.ui.res.painterResource
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
import androidx.compose.foundation.verticalScroll

/**
 * Bottom navigation items
 */
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(NavDestinations.HOME_ROUTE, "Home", Icons.Default.Home)
    object Progress : BottomNavItem(NavDestinations.PROGRESS_ROUTE, "Progress", Icons.Default.ShowChart)
    object Profile : BottomNavItem(NavDestinations.PROFILE_ROUTE, "Profile", Icons.Default.Person)
}

val BottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Progress,
    BottomNavItem.Profile
)

/**
 * Data class for categories
 */
data class HistoryCategory(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val completion: Int, // percentage of completion
    val gradientStart: Color,
    val gradientEnd: Color
)

/**
 * Sample data for preview
 */
val sampleCategories = listOf(
    HistoryCategory(
        "ancient", 
        "Ancient Civilizations", 
        "Egypt, Greece, Rome and more",
        R.drawable.ic_ancient, 
        75,
        AncientGradientStart,
        AncientGradientEnd
    ),
    HistoryCategory(
        "medieval", 
        "Medieval Period", 
        "Castles, knights, and feudal systems",
        R.drawable.ic_medieval, 
        30,
        MedievalGradientStart,
        MedievalGradientEnd
    ),
    HistoryCategory(
        "renaissance", 
        "Renaissance",
        "Art, science and cultural rebirth",
        R.drawable.ic_renaissance, 
        0,
        RenaissanceGradientStart,
        RenaissanceGradientEnd
    ),
    HistoryCategory(
        "modern", 
        "Modern History", 
        "Industrial revolution to present day",
        R.drawable.ic_modern, 
        10,
        ModernGradientStart,
        ModernGradientEnd
    ),
    HistoryCategory(
        "world_wars", 
        "World Wars", 
        "The two major global conflicts",
        R.drawable.ic_wars, 
        50,
        WorldWarsGradientStart,
        WorldWarsGradientEnd
    )
)

/**
 * Home screen with categories and bottom navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    
    // Single background color - warm beige
    val backgroundColor = Color(0xFFEDE3D2)
    // Text color - dark gray for better contrast
    val textColor = Color(0xFF333333)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Imperium : History Quiz",
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
                            Icon(imageVector = screen.icon, contentDescription = screen.title) 
                        },
                        label = { Text(screen.title) },
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
                            text = "Welcome to",
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = "IMPERIUM",
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
                            color = textColor.copy(alpha = 0.5f),
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
                            text = "Categories",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        
                        Text(
                            text = "Explore",
                            style = MaterialTheme.typography.labelLarge,
                            color = textColor.copy(alpha = 0.7f)
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
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            category.gradientStart,
                            category.gradientEnd
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
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
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
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.9f)
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