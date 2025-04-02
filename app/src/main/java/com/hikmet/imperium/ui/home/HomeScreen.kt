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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.*
import kotlinx.coroutines.delay
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontStyle
import android.util.Log

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
    val detailDescriptionRes: Int, // Detailed description for modal
    val icon: ImageVector,
    val startColor: Color,
    val endColor: Color
)

/**
 * Sample data for preview
 */
val sampleCategories = listOf(
    HistoryCategory(
        "ancient", 
        R.string.category_ancient_title, 
        R.string.category_ancient_desc,
        R.string.category_ancient_detail,
        Icons.Default.AccountBalance, // Ancient architecture icon
        AncientGradientStart,
        AncientGradientEnd
    ),
    HistoryCategory(
        "medieval", 
        R.string.category_medieval_title, 
        R.string.category_medieval_desc,
        R.string.category_medieval_detail,
        Icons.Default.Castle, // Castle for medieval
        MedievalGradientStart,
        MedievalGradientEnd
    ),
    HistoryCategory(
        "renaissance", 
        R.string.category_renaissance_title,
        R.string.category_renaissance_desc,
        R.string.category_renaissance_detail,
        Icons.Default.Palette, // Art palette for renaissance
        RenaissanceGradientStart,
        RenaissanceGradientEnd
    ),
    HistoryCategory(
        "modern", 
        R.string.category_modern_title, 
        R.string.category_modern_desc,
        R.string.category_modern_detail,
        Icons.Default.HomeWork, // Buildings for modern era
        ModernGradientStart,
        ModernGradientEnd
    ),
    HistoryCategory(
        "world_wars", 
        R.string.category_worldwars_title, 
        R.string.category_worldwars_desc,
        R.string.category_worldwars_detail,
        Icons.Default.Bolt, // Lightning bolt for warfare/conflict
        WorldWarsGradientStart,
        WorldWarsGradientEnd
    )
)

/**
 * Home screen with categories and bottom navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = OnBackground
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Background,
                    titleContentColor = OnBackground
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                containerColor = SurfaceVariant,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                BottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(screen.title),
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        label = { 
                            Text(
                                text = stringResource(screen.title),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Primary,
                            selectedTextColor = Primary,
                            indicatorColor = PrimaryContainer.copy(alpha = 0.7f),
                            unselectedIconColor = OnSurfaceVariant.copy(alpha = 0.8f),
                            unselectedTextColor = OnSurfaceVariant.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Feature highlight section that replaces the welcome section
            FeatureHighlight()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Categories section
            Text(
                text = stringResource(R.string.categories_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = OnBackground,
                modifier = Modifier.padding(start = 4.dp, bottom = 16.dp)
            )
            
            // Display categories in a single column
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                sampleCategories.forEach { category ->
                    CategoryCard(
                        category = category,
                        onClick = {
                            try {
                                // Navigate to the correct category with error handling
                                navController.navigate("category/${category.id}")
                            } catch (e: Exception) {
                                // Log error and prevent crash
                                Log.e("HomeScreen", "Error navigating to category ${category.id}: ${e.message}")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureHighlight() {
    QuoteShowcase()
}

/**
 * Data class to hold quote information
 */
data class HistoricalQuote(
    val text: String,
    val author: String,
    val era: String,
    val year: String,
    val gradientColors: Pair<Color, Color>
)

/**
 * List of historical quotes to display
 */
val historicalQuotes = listOf(
    HistoricalQuote(
        "The die is cast.",
        "Julius Caesar",
        "Ancient Rome",
        "49 BCE",
        Pair(AncientGradientStart, AncientGradientEnd)
    ),
    HistoricalQuote(
        "Knowledge is power.",
        "Francis Bacon",
        "Renaissance",
        "1597",
        Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
    ),
    HistoricalQuote(
        "I think, therefore I am.",
        "René Descartes",
        "Enlightenment",
        "1637",
        Pair(MedievalGradientStart.copy(alpha = 0.8f), MedievalGradientEnd)
    ),
    HistoricalQuote(
        "Let them eat cake.",
        "Marie Antoinette",
        "French Revolution",
        "1789",
        Pair(ModernGradientStart, ModernGradientEnd)
    ),
    HistoricalQuote(
        "We shall fight on the beaches.",
        "Winston Churchill",
        "World War II",
        "1940",
        Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
    )
)

@Composable
private fun QuoteShowcase() {
    var currentQuote by remember { mutableStateOf(QuotesDatabase.getRandomQuote()) }
    
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            currentQuote.gradientColors.first,
                            currentQuote.gradientColors.second
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { 
                    // Get a completely random quote from any category
                    currentQuote = QuotesDatabase.getRandomQuote(null)
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Words that shaped history",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "\"${currentQuote.text}\"",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        letterSpacing = (-0.5).sp,
                        lineHeight = 20.sp
                    ),
                    color = Color.White,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Author and era
                    Column {
                        Text(
                            text = "— ${currentQuote.author}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White
                        )
                        
                        Text(
                            text = "${currentQuote.era}, ${currentQuote.year}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    
                    // Simple tap hint
                    Text(
                        text = "Tap for more",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: HistoryCategory, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 12f else 8f,
        label = "elevation"
    )
    
    // State for the modal bottom sheet
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = elevation.dp,
            pressedElevation = 12.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                category.startColor,
                                category.endColor
                            )
                        )
                    )
            )
            
            // Dark overlay for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )
            
            // More menu button
            IconButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = OnBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Category content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section with icon
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    tonalElevation = 4.dp
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = stringResource(id = category.titleRes),
                        modifier = Modifier
                            .padding(12.dp)
                            .size(32.dp),
                        tint = category.startColor
                    )
                }
                
                // Bottom section with title and description
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(id = category.titleRes),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = stringResource(id = category.descriptionRes),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            letterSpacing = 0.sp,
                            lineHeight = 22.sp
                        ),
                        color = Color.White.copy(alpha = 0.95f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
    
    // Modal bottom sheet for detailed description
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Background,
            contentColor = OnBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Category title
                Text(
                    text = stringResource(id = category.titleRes),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = OnBackground
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Divider with gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    category.startColor,
                                    category.endColor
                                )
                            ),
                            shape = RoundedCornerShape(1.dp)
                        )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Category icon and brief description
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = category.startColor.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(32.dp),
                            tint = category.startColor
                        )
                    }
                    
                    Spacer(modifier = Modifier.size(16.dp))
                    
                    Text(
                        text = stringResource(id = category.descriptionRes),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        ),
                        color = OnBackground.copy(alpha = 0.8f)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Detailed description
                Text(
                    text = stringResource(id = category.detailDescriptionRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ImperiumTheme {
        HomeScreen()
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