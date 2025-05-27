package com.hikmet.imperium.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.hikmet.imperium.R
import com.hikmet.imperium.data.MedievalQuizData
import com.hikmet.imperium.data.Question
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.util.formatTime
import com.hikmet.imperium.ui.util.rememberQuizTimer
import com.hikmet.imperium.ui.util.SoundManager
import com.hikmet.imperium.ui.util.calculateStars
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedievalQuizScreen(
    navController: NavHostController,
    levelId: String,
    soundManager: SoundManager = hiltViewModel<MedievalQuizViewModel>().soundManager
) {
    // Coroutine scope for handling delays
    val coroutineScope = rememberCoroutineScope()
    
    // Get questions for this level
    val questions = MedievalQuizData.getQuestionsByLevel(levelId)
    
    // State for the quiz
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var timeRemaining by remember { mutableStateOf(300L) } // 5 minutes in seconds
    var isQuizComplete by remember { mutableStateOf(false) }
    var showTimeExpirationAlert by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableStateOf(0) } // Track correct answers separately
    
    // Medieval theme colors
    val medievalPrimary = colorResource(R.color.medieval_button)
    val medievalBackground = colorResource(R.color.medieval_background_light)
    val medievalTextPrimary = colorResource(R.color.medieval_text_primary)
    
    // Use the QuizTimer utility
    val timerValue = rememberQuizTimer(
        initialTimeMs = 40000L, // 40 seconds in milliseconds
        isStarted = !isQuizComplete,
        onTick = { remainingTimeMs ->
            // Update timeRemaining in seconds for compatibility with existing code
            timeRemaining = remainingTimeMs / 1000
            
            // If time is running out, we could show a visual warning
            if (remainingTimeMs <= 10000 && !showTimeExpirationAlert) {
                // Time is almost up
                println("TIME ALMOST UP: $remainingTimeMs")
            }
        },
        onFinish = {
            // When time expires, navigate to results
            showTimeExpirationAlert = true
            if (!isQuizComplete) {
                // Time's up - play wrong answer sound and navigate to results
                soundManager.playWrongAnswer()
                navController.navigate(
                    NavDestinations.MEDIEVAL_RESULTS_ROUTE
                        .replace("{levelId}", levelId)
                        .replace("{score}", score.toString())
                        .replace("{stars}", calculateStars(score).toString())
                        .replace("{correctAnswers}", correctAnswersCount.toString())
                        .replace("{totalQuestions}", questions.size.toString())
                )
            }
        }
    )
    
    // Timer color changes as time runs out
    val timerColor = when {
        timerValue.value > 30000 -> medievalPrimary // Normal color above 30 seconds
        timerValue.value > 20000 -> Color(0xFFFFA000) // Amber when under 30 seconds
        timerValue.value > 10000 -> Color(0xFFFF6D00) // Orange when under 20 seconds
        else -> Color(0xFFD32F2F) // Red when under 10 seconds
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Medieval Quiz - Level $levelId",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = medievalTextPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        // Play button click sound
                        soundManager.playButtonClick()
                        
                        // Navigate directly to level selection screen instead of going back
                        navController.navigate(NavDestinations.LEVEL_SELECTION_ROUTE.replace("{categoryId}", "medieval")) {
                            // Clear back stack up to the level selection screen
                            popUpTo(NavDestinations.LEVEL_SELECTION_ROUTE.replace("{categoryId}", "medieval")) {
                                inclusive = false
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Levels",
                            tint = medievalTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = medievalBackground
                ),
                actions = {
                    // Timer in top bar removed
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(medievalBackground)
                .padding(16.dp)
        ) {
            // Progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress
                Text(
                    text = "${currentQuestionIndex + 1}/${questions.size}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = medievalTextPrimary
                )
            }

            // Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = questions[currentQuestionIndex].text,
                        style = MaterialTheme.typography.titleLarge,
                        color = medievalTextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            
            // Timer progress bar (updated)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Timer progress bar
                val timerProgress = (timerValue.value.toFloat() / 40000f).coerceIn(0f, 1f)
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Timer label with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = timerColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatTime(timerValue.value),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = timerColor
                        )
                    }
                    
                    // Progress bar
                    LinearProgressIndicator(
                        progress = timerProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = timerColor,
                        trackColor = timerColor.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Answer Options
            questions[currentQuestionIndex].options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index
                val isCorrect = index == questions[currentQuestionIndex].correctAnswerIndex
                val showFeedback = selectedAnswer != null

                // Determine colors based on state
                val backgroundColor = when {
                    showFeedback && isCorrect -> Color(0xFF4CAF50) // Green for correct
                    showFeedback && isSelected && !isCorrect -> Color(0xFFE53935) // Red for wrong selection
                    isSelected -> medievalPrimary // Theme color for selection
                    else -> Color.White // Default white
                }

                val textColor = when {
                    showFeedback && (isCorrect || (isSelected && !isCorrect)) -> Color.White
                    isSelected -> Color.White
                    else -> medievalTextPrimary
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = backgroundColor
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected || (showFeedback && isCorrect)) 4.dp else 1.dp
                    ),
                    onClick = {
                        if (selectedAnswer == null) {
                            selectedAnswer = index
                            isAnswerCorrect = isCorrect
                            
                            // Play sound based on answer correctness
                            if (isCorrect) {
                                soundManager.playCorrectAnswer()
                                score += 25 // 25 points per correct answer
                                correctAnswersCount++
                            } else {
                                soundManager.playWrongAnswer()
                            }

                            // Move to next question after delay
                            coroutineScope.launch {
                                delay(1500) // Show feedback for 1.5 seconds
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedAnswer = null
                                    isAnswerCorrect = null
                                } else {
                                    // Quiz complete - play level complete sound
                                    soundManager.playLevelComplete()
                                    isQuizComplete = true
                                    navController.navigate(
                                        NavDestinations.MEDIEVAL_RESULTS_ROUTE
                                            .replace("{levelId}", levelId)
                                            .replace("{score}", score.toString())
                                            .replace("{stars}", calculateStars(score).toString())
                                            .replace("{correctAnswers}", correctAnswersCount.toString())
                                            .replace("{totalQuestions}", questions.size.toString())
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (isSelected || (showFeedback && isCorrect)) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        // Overlay for time's up alert
        if (showTimeExpirationAlert) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Time expired",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Time's Up!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "You didn't complete the quiz in time. Your progress has been saved.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Button(
                            onClick = {
                                // Play button click sound
                                soundManager.playButtonClick()
                                navController.navigate(
                                    NavDestinations.LEVEL_SELECTION_ROUTE.replace("{categoryId}", "medieval")
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = medievalPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Return to Levels")
                        }
                    }
                }
            }
        }
    }
} 