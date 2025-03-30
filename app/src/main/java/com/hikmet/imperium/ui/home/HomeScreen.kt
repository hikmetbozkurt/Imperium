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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.navigation.NavDestinations
import kotlinx.coroutines.delay

/**
 * Data class for categories
 */
data class HistoryCategory(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val completion: Int // percentage of completion
)

/**
 * Sample data for preview
 */
val sampleCategories = listOf(
    HistoryCategory(
        "ancient", 
        "Ancient Civilizations", 
        "Egypt, Greece, Rome and more",
        R.drawable.ic_launcher_foreground, 
        75
    ),
    HistoryCategory(
        "medieval", 
        "Medieval Period", 
        "Castles, knights, and feudal systems",
        R.drawable.ic_launcher_foreground, 
        30
    ),
    HistoryCategory(
        "renaissance", 
        "Renaissance",
        "Art, science and cultural rebirth",
        R.drawable.ic_launcher_foreground, 
        0
    ),
    HistoryCategory(
        "modern", 
        "Modern History", 
        "Industrial revolution to present day",
        R.drawable.ic_launcher_foreground, 
        10
    ),
    HistoryCategory(
        "world_wars", 
        "World Wars", 
        "The two major global conflicts",
        R.drawable.ic_launcher_foreground, 
        50
    )
)

/**
 * Home screen with categories and bottom navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    var showWelcome by remember { mutableStateOf(true) }

    // Hide welcome banner after 3 seconds
    LaunchedEffect(key1 = Unit) {
        delay(3000)
        showWelcome = false
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Imperium",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "User Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = { 
                        selectedItem = 0 
                        navController.navigate(NavDestinations.HOME_ROUTE) {
                            popUpTo(NavDestinations.HOME_ROUTE) { inclusive = true }
                        }
                    },
                    icon = { 
                        Icon(
                            if (selectedItem == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        ) 
                    },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = { 
                        selectedItem = 1 
                        navController.navigate(NavDestinations.PROGRESS_ROUTE)
                    },
                    icon = { 
                        Icon(
                            if (selectedItem == 1) Icons.Filled.ShowChart else Icons.Outlined.ShowChart,
                            contentDescription = "Progress"
                        ) 
                    },
                    label = { Text("Progress") }
                )
                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = { 
                        selectedItem = 2 
                        navController.navigate(NavDestinations.PROFILE_ROUTE)
                    },
                    icon = { 
                        Icon(
                            if (selectedItem == 2) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile"
                        ) 
                    },
                    label = { Text("Profile") }
                )
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize()) {
                // Welcome Banner with animation
                AnimatedVisibility(
                    visible = showWelcome,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500))
                ) {
                    WelcomeBanner()
                }

                // Main content
                CategoryList(
                    categories = sampleCategories,
                    onCategoryClick = { category ->
                        navController.navigate("category/${category.id}")
                    },
                    contentPadding = paddingValues
                )
            }
        }
    )
}

/**
 * Welcome banner that appears on app launch
 */
@Composable
fun WelcomeBanner() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Continue your historical journey with Imperium.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

/**
 * List of categories
 */
@Composable
fun CategoryList(
    categories: List<HistoryCategory>,
    onCategoryClick: (HistoryCategory) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        itemsIndexed(categories) { index, category ->
            // Add animation for each item
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 100 * index
                    )
                ) + slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    initialOffsetY = { it * 2 } // slide from below
                )
            ) {
                Column {
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Card for a single category
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: HistoryCategory,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category image
            Image(
                painter = painterResource(id = category.imageResId),
                contentDescription = category.title,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Category text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress bar
                if (category.completion > 0) {
                    Column {
                        LinearProgressIndicator(
                            progress = category.completion / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "${category.completion}% Complete",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Completion badge
            if (category.completion == 100) {
                BadgedBox(
                    badge = {
                        Badge {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "Completed",
                                modifier = Modifier.size(12.dp),
                                tint = Color.White
                            )
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    MaterialTheme {
        CategoryCard(
            category = sampleCategories[0],
            onClick = {}
        )
    }
} 