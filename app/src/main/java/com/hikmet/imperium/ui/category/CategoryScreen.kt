package com.hikmet.imperium.ui.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.theme.AncientGradientEnd
import com.hikmet.imperium.ui.theme.AncientGradientStart
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
 * Extended category data with description and statistics
 */
data class CategoryDetail(
    val id: String,
    val title: String,
    val description: String,
    val longDescription: String,
    val imageResId: Int,
    val completion: Int, // percentage of completion
    val totalLevels: Int,
    val completedLevels: Int,
    val unlockedLevels: Int,
    val gradientStart: Color,
    val gradientEnd: Color
)

// Extension properties for compatibility with dependent files
val CategoryDetail.gradientColors: List<Color> get() = listOf(gradientStart, gradientEnd)
val CategoryDetail.imageRes: Int get() = imageResId

/**
 * Collection of category details for all categories
 */
val categoryDetails = mapOf(
    "ancient" to CategoryDetail(
        id = "ancient",
        title = "Ancient Civilizations",
        description = "Egypt, Greece, Rome and more",
        longDescription = "Explore the fascinating world of ancient civilizations that laid the foundation for modern society. " +
                "From the pyramids of Egypt to the forums of Rome, discover the inventions, cultures, and legacies that shaped human history.",
        imageResId = R.drawable.ic_ancient,
        completion = 75,
        totalLevels = 12,
        completedLevels = 9,
        unlockedLevels = 10,
        gradientStart = AncientGradientStart,
        gradientEnd = AncientGradientEnd
    ),
    "medieval" to CategoryDetail(
        id = "medieval",
        title = "Medieval Period",
        description = "Castles, knights, and feudal systems",
        longDescription = "Journey to the Middle Ages, a time of castles, knights, and feudal systems. " +
                "Learn about the rise of kingdoms, the influence of the church, the Crusades, and the cultural developments " +
                "that bridged the ancient world to the Renaissance.",
        imageResId = R.drawable.ic_medieval,
        completion = 30,
        totalLevels = 12,
        completedLevels = 4,
        unlockedLevels = 6,
        gradientStart = MedievalGradientStart,
        gradientEnd = MedievalGradientEnd
    ),
    "renaissance" to CategoryDetail(
        id = "renaissance",
        title = "Renaissance",
        description = "Art, science and cultural rebirth",
        longDescription = "Witness the rebirth of art, science, and culture during the Renaissance. From Michelangelo's " +
                "masterpieces to Leonardo da Vinci's inventions, discover how this period of innovation and enlightenment " +
                "transformed Europe and set the stage for the modern world.",
        imageResId = R.drawable.ic_renaissance,
        completion = 0,
        totalLevels = 12,
        completedLevels = 0,
        unlockedLevels = 3,
        gradientStart = RenaissanceGradientStart,
        gradientEnd = RenaissanceGradientEnd
    ),
    "modern" to CategoryDetail(
        id = "modern",
        title = "Modern History",
        description = "Industrial revolution to present day",
        longDescription = "From the Industrial Revolution to the Digital Age, explore the rapid transformations of the modern era. " +
                "Learn about technological innovations, social movements, political revolutions, and global conflicts that have " +
                "shaped our contemporary world in the past few centuries.",
        imageResId = R.drawable.ic_modern,
        completion = 10,
        totalLevels = 12,
        completedLevels = 1,
        unlockedLevels = 4,
        gradientStart = ModernGradientStart,
        gradientEnd = ModernGradientEnd
    ),
    "world_wars" to CategoryDetail(
        id = "world_wars",
        title = "World Wars",
        description = "The two major global conflicts",
        longDescription = "Delve into the defining conflicts of the 20th century: World War I and World War II. " +
                "Study the complex causes, pivotal battles, technological advancements, humanitarian crises, and lasting " +
                "consequences of these global struggles that reshaped international relations and redefined modern warfare.",
        imageResId = R.drawable.ic_wars,
        completion = 50,
        totalLevels = 12,
        completedLevels = 6,
        unlockedLevels = 8,
        gradientStart = WorldWarsGradientStart,
        gradientEnd = WorldWarsGradientEnd
    )
)

/**
 * Default category for fallback
 */
val defaultCategoryDetail = categoryDetails["ancient"]!!

/**
 * Category detail screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavHostController,
    categoryId: String
) {
    // Get category details, with fallback
    val categoryDetail = remember<CategoryDetail>(categoryId) {
        categoryDetails[categoryId] ?: categoryDetails["ancient"]!!
    }
    
    // Loading state
    var isLoading by remember { mutableStateOf(true) }
    
    // Simulate loading delay for demo purposes
    LaunchedEffect(categoryId) {
        isLoading = true
        delay(600)
        isLoading = false
    }
    
    val gradientColors = categoryDetail.gradientColors
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        categoryDetail.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (categoryId == "ancient") Color.Black else MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (categoryId == "ancient") Color.White else gradientColors.first(),
                    titleContentColor = if (categoryId == "ancient") Color.Black else MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.semantics { 
                            contentDescription = "Navigate back to home" 
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = if (categoryId == "ancient") Color(0xFF227C70) else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            if (isLoading) {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.semantics { 
                            contentDescription = "Loading category information" 
                        }
                    ) {
                        CircularProgressIndicator(
                            color = gradientColors.first()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading ${categoryDetail.title}...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                // Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // About section with illustration for Ancient Civilizations
                    if (categoryId == "ancient") {
                        // Special design for Ancient Civilizations page
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { 
                                    contentDescription = "About ${categoryDetail.title}" 
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF0F9F8)  // Light teal background
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    ) {
                                        // Teal circle with i icon
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF227C70)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "i",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.width(12.dp))
                                        
                                        Text(
                                            text = "About",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    Text(
                                        text = categoryDetail.longDescription,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                                
                                // Image of pyramids and temple
                                Image(
                                    painter = painterResource(R.drawable.ancient_page_image),
                                    contentDescription = "Ancient Civilizations Illustration",
                                    modifier = Modifier
                                        .size(width = 160.dp, height = 160.dp)
                                        .padding(start = 8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    } else {
                        // Standard About card for other categories
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { 
                                    contentDescription = "About ${categoryDetail.title}" 
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    ) {
                                        // Circular icon
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(gradientColors.first()),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "Information about this category",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.width(12.dp))
                                        
                                        Text(
                                            text = "About",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    Text(
                                        text = categoryDetail.longDescription,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    
                    // What would you like to do section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { 
                                contentDescription = "Category actions for ${categoryDetail.title}" 
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "What would you like to do?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            // Start learning button - Updated for Ancient
                            Button(
                                onClick = { navController.navigate("level/$categoryId/1") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .semantics { 
                                        contentDescription = "Start learning levels in ${categoryDetail.title}" 
                                    },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (categoryId == "ancient") 
                                        Color(0xFF227C70) 
                                    else 
                                        gradientColors.first()
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Start learning icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Start Learning",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            
                            // Take a quiz button - Updated for Ancient
                            OutlinedButton(
                                onClick = { navController.navigate("quiz/$categoryId/1/STANDARD") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .semantics { 
                                        contentDescription = "Take a quiz on ${categoryDetail.title}" 
                                    },
                                shape = RoundedCornerShape(8.dp),
                                colors = if (categoryId == "ancient") {
                                    ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF227C70)
                                    )
                                } else {
                                    ButtonDefaults.outlinedButtonColors(
                                        contentColor = gradientColors.first()
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Help,
                                    contentDescription = "Quiz icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Take a Quiz",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                    
                    // Your Progress Section - Updated for Ancient
                    if (categoryId == "ancient") {
                        // Modern circular progress with stats
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Your Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Circle with number
                                    Box(
                                        modifier = Modifier.size(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            progress = 0.75f, // 9 out of 12 levels completed
                                            modifier = Modifier.size(100.dp),
                                            strokeWidth = 8.dp,
                                            color = Color(0xFF227C70),
                                            trackColor = Color(0xFFE0E0E0)
                                        )
                                        
                                        Text(
                                            text = "9",
                                            style = MaterialTheme.typography.displayMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF227C70)
                                        )
                                    }
                                    
                                    // Stats columns
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "10",
                                                    style = MaterialTheme.typography.headlineMedium,
                                                    color = Color(0xFF227C70),
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Unlocked",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.Gray
                                                )
                                            }
                                            
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "12",
                                                    style = MaterialTheme.typography.headlineMedium,
                                                    color = Color(0xFF227C70),
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Total",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Standard progress display for other categories
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { 
                                    contentDescription = "Your progress in ${categoryDetail.title}" 
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Your Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    ProgressStat(
                                        value = "${categoryDetail.completedLevels}",
                                        label = "Levels completed",
                                        color = gradientColors.first()
                                    )
                                    
                                    ProgressStat(
                                        value = "${categoryDetail.unlockedLevels}",
                                        label = "Unlocked",
                                        color = gradientColors.first()
                                    )
                                    
                                    ProgressStat(
                                        value = "${categoryDetail.totalLevels}",
                                        label = "Total",
                                        color = gradientColors.first()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressStat(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics { 
            contentDescription = "$value $label" 
        }
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    CategoryScreen(
        navController = rememberNavController(),
        categoryId = "ancient"
    )
} 