package com.hikmet.imperium.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.ImperiumApplication
import com.hikmet.imperium.R
import com.hikmet.imperium.data.entities.AnswerEntity
import com.hikmet.imperium.data.entities.QuestionEntity
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.AncientGradientEnd
import com.hikmet.imperium.ui.theme.AncientGradientStart
import com.hikmet.imperium.ui.theme.CorrectAnswer
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.IncorrectAnswer
import com.hikmet.imperium.ui.theme.MedievalGradientEnd
import com.hikmet.imperium.ui.theme.MedievalGradientStart
import com.hikmet.imperium.ui.theme.ModernGradientEnd
import com.hikmet.imperium.ui.theme.ModernGradientStart
import com.hikmet.imperium.ui.theme.RenaissanceGradientEnd
import com.hikmet.imperium.ui.theme.RenaissanceGradientStart
import com.hikmet.imperium.ui.theme.SelectedAnswer
import com.hikmet.imperium.ui.theme.WorldWarsGradientEnd
import com.hikmet.imperium.ui.theme.WorldWarsGradientStart
import com.hikmet.imperium.ui.util.formatTime
import com.hikmet.imperium.ui.util.rememberQuizTimer
import com.hikmet.imperium.ui.viewmodel.QuizState
import com.hikmet.imperium.ui.viewmodel.QuizViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Quiz question data class
 */
data class Question(
    val id: Int,
    val text: String,
    val imageResId: Int,
    val options: List<String>,
    val correctAnswerIndex: Int
)

/**
 * Ancient Civilizations Questions
 */
val ancientQuestions = listOf(
    Question(
        id = 1,
        text = "Which emperor built the Colosseum in Rome?",
        imageResId = R.drawable.ic_ancient,
        options = listOf("Augustus", "Vespasian", "Nero", "Constantine"),
        correctAnswerIndex = 1
    ),
    Question(
        id = 2,
        text = "The Great Sphinx of Giza has the head of a human and the body of what animal?",
        imageResId = R.drawable.ic_ancient,
        options = listOf("Eagle", "Bull", "Lion", "Horse"),
        correctAnswerIndex = 2
    ),
    Question(
        id = 3,
        text = "Who was the first Emperor of China's Qin Dynasty?",
        imageResId = R.drawable.ic_ancient,
        options = listOf("Qin Shi Huang", "Wu Zetian", "Liu Bang", "Han Gaozu"),
        correctAnswerIndex = 0
    )
)

/**
 * Medieval Period Questions
 */
val medievalQuestions = listOf(
    Question(
        id = 1,
        text = "What event marked the beginning of the Middle Ages in Europe?",
        imageResId = R.drawable.ic_medieval,
        options = listOf("Crowning of Charlemagne", "Fall of Rome", "First Crusade", "Black Death"),
        correctAnswerIndex = 1
    ),
    Question(
        id = 2,
        text = "What was the Magna Carta?",
        imageResId = R.drawable.ic_medieval,
        options = listOf("A medieval weapon", "A royal decree", "A charter of rights", "A religious text"),
        correctAnswerIndex = 2
    ),
    Question(
        id = 3,
        text = "Who was known as 'William the Conqueror'?",
        imageResId = R.drawable.ic_medieval,
        options = listOf("First king of Scotland", "Norman king of England", "Holy Roman Emperor", "French military leader"),
        correctAnswerIndex = 1
    )
)

/**
 * Renaissance Questions
 */
val renaissanceQuestions = listOf(
    Question(
        id = 1,
        text = "Who painted the ceiling of the Sistine Chapel?",
        imageResId = R.drawable.ic_renaissance,
        options = listOf("Leonardo da Vinci", "Raphael", "Michelangelo", "Donatello"),
        correctAnswerIndex = 2
    ),
    Question(
        id = 2,
        text = "In which city did the Renaissance begin?",
        imageResId = R.drawable.ic_renaissance,
        options = listOf("Rome", "Florence", "Venice", "Milan"),
        correctAnswerIndex = 1
    ),
    Question(
        id = 3,
        text = "Who wrote 'The Prince', a political treatise from the Renaissance?",
        imageResId = R.drawable.ic_renaissance,
        options = listOf("Dante Alighieri", "Giovanni Boccaccio", "Niccolò Machiavelli", "Francesco Petrarch"),
        correctAnswerIndex = 2
    )
)

/**
 * Modern History Questions
 */
val modernQuestions = listOf(
    Question(
        id = 1,
        text = "When did the Industrial Revolution begin?",
        imageResId = R.drawable.ic_modern,
        options = listOf("Late 17th century", "Mid 18th century", "Early 19th century", "Mid 19th century"),
        correctAnswerIndex = 1
    ),
    Question(
        id = 2,
        text = "Who invented the telephone?",
        imageResId = R.drawable.ic_modern,
        options = listOf("Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "Guglielmo Marconi"),
        correctAnswerIndex = 1
    ),
    Question(
        id = 3,
        text = "What event sparked the French Revolution?",
        imageResId = R.drawable.ic_modern,
        options = listOf("Storming of the Bastille", "Execution of Louis XVI", "Napoleonic Wars", "Seven Years' War"),
        correctAnswerIndex = 0
    )
)

/**
 * World Wars Questions
 */
val worldWarsQuestions = listOf(
    Question(
        id = 1,
        text = "In which year did World War II end?",
        imageResId = R.drawable.ic_wars,
        options = listOf("1943", "1944", "1945", "1946"),
        correctAnswerIndex = 2
    ),
    Question(
        id = 2,
        text = "What was the immediate cause of World War I?",
        imageResId = R.drawable.ic_wars,
        options = listOf("Sinking of the Lusitania", "Assassination of Archduke Franz Ferdinand", "German invasion of Poland", "Russian Revolution"),
        correctAnswerIndex = 1
    ),
    Question(
        id = 3,
        text = "Which battle is considered the turning point of World War II in the Pacific?",
        imageResId = R.drawable.ic_wars,
        options = listOf("Iwo Jima", "Okinawa", "Midway", "Guadalcanal"),
        correctAnswerIndex = 2
    )
)

enum class QuizType {
    STANDARD,
    TIME_ATTACK,
    IMAGE_BASED
}

/**
 * Quiz screen with questions and answers
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavHostController,
    categoryId: String,
    levelId: String,
    quizType: String
) {
    // Get context for accessing resources
    val context = LocalContext.current
    
    // Get the repository from the application
    val repository = (context.applicationContext as ImperiumApplication).repository
    
    // Initialize ViewModel
    val quizViewModel: QuizViewModel = viewModel(
        factory = QuizViewModel.Factory(repository)
    )
    
    // Collect quiz state
    val quizState by quizViewModel.quizState.collectAsState()
    val currentQuestionIndex by quizViewModel.currentQuestionIndex.collectAsState()
    val selectedAnswerId by quizViewModel.selectedAnswerId.collectAsState()
    val quizProgress by quizViewModel.quizProgress.collectAsState()
    val timerState by quizViewModel.timerState.collectAsState()
    val quizResults by quizViewModel.quizResults.collectAsState()
    
    // Current question and answers
    val currentQuestion = quizViewModel.getCurrentQuestion()
    val currentAnswers = quizViewModel.getCurrentAnswers()
    
    // Load quiz data when the screen is first displayed
    LaunchedEffect(categoryId, levelId) {
        quizViewModel.loadQuiz(categoryId, levelId.toIntOrNull() ?: 1)
    }
    
    // Start the quiz when it's ready
    LaunchedEffect(quizState) {
        if (quizState == QuizState.Ready) {
            quizViewModel.startQuiz()
        }
    }
    
    // Get gradient colors based on category
    val gradientStart = when (categoryId) {
        "ancient" -> colorResource(R.color.ancient_gradient_start)
        "medieval" -> colorResource(R.color.medieval_gradient_start)
        "renaissance" -> colorResource(R.color.renaissance_gradient_start)
        "modern" -> colorResource(R.color.modern_gradient_start)
        "world_wars" -> colorResource(R.color.worldwars_gradient_start)
        else -> colorResource(R.color.ancient_gradient_start)
    }
    
    val gradientEnd = when (categoryId) {
        "ancient" -> colorResource(R.color.ancient_gradient_end)
        "medieval" -> colorResource(R.color.medieval_gradient_end)
        "renaissance" -> colorResource(R.color.renaissance_gradient_end)
        "modern" -> colorResource(R.color.modern_gradient_end)
        "world_wars" -> colorResource(R.color.worldwars_gradient_end)
        else -> colorResource(R.color.ancient_gradient_end)
    }
    
    // Use different style for ancient category
    val isAncient = categoryId == "ancient"
    val appBarColor = if (isAncient) colorResource(R.color.ancient_background_light) else gradientStart
    val textColor = if (isAncient) colorResource(R.color.ancient_text_primary) else MaterialTheme.colorScheme.onPrimary
    val iconTint = if (isAncient) colorResource(R.color.ancient_button) else MaterialTheme.colorScheme.onPrimary
    val primaryColor = if (isAncient) colorResource(R.color.ancient_button) else gradientStart
    
    // Get color resources for quiz answers
    val answerCorrectColor = if (isAncient) colorResource(R.color.ancient_button) else colorResource(R.color.answer_correct)
    val answerIncorrectColor = colorResource(R.color.answer_incorrect)
    val answerSelectedColor = if (isAncient) colorResource(R.color.ancient_button) else colorResource(R.color.answer_selected)
    val answerUnselectedColor = colorResource(R.color.answer_unselected)
    val answerTextLight = colorResource(R.color.answer_text_light)
    val answerTextDark = colorResource(R.color.answer_text_dark)
    val answerBorderLight = colorResource(R.color.answer_border_light)
    val cardBackgroundLight = colorResource(R.color.ancient_background_light)
    
    // State for time expiration alert
    var showTimeExpirationAlert by remember { mutableStateOf(false) }
    
    // Timer for UI display
    val timerValue = rememberQuizTimer(
        initialTimeMs = 30000L, // 30 seconds per level as required
        isStarted = quizState == QuizState.Active,
        onTick = { remainingTimeMs ->
            quizViewModel.updateTimer(remainingTimeMs)
            // If time is running out, show a warning
            if (remainingTimeMs <= 5000 && !showTimeExpirationAlert) {
                // Time is almost up
                println("TIME ALMOST UP: $remainingTimeMs")
            }
        },
        onFinish = {
            // When time expires, show the alert dialog
            println("TIMER FINISHED - SHOWING ALERT")
            showTimeExpirationAlert = true
        }
    )
    
    // Parse quiz type
    val quizTypeEnum = remember<QuizType>(quizType) {
        try {
            QuizType.valueOf(quizType)
        } catch (e: IllegalArgumentException) {
            QuizType.STANDARD
        }
    }
    
    // Create a meaningful title for the quiz
    val quizTitle = remember<String>(categoryId, quizTypeEnum) {
        "Level $levelId: ${
            when (quizTypeEnum) {
                QuizType.STANDARD -> "Quiz"
                QuizType.TIME_ATTACK -> "Time Challenge"
                QuizType.IMAGE_BASED -> "Image Quiz"
            }
        }"
    }
    
    // Coroutine scope for animations and delayed actions
    val coroutineScope = rememberCoroutineScope()
    
    // Calculate progress
    val progressValue = when {
        quizState == QuizState.Completed -> 1f
        currentQuestion == null -> 0f
        else -> currentQuestionIndex.toFloat() / QuizViewModel.QUESTIONS_PER_QUIZ.toFloat()
    }
    
    // Accessibility description for current question
    val questionAccessibilityDesc = when (quizState) {
        is QuizState.Loading -> "Loading quiz questions"
        is QuizState.Error -> "Error loading quiz: ${(quizState as QuizState.Error).message}"
        else -> currentQuestion?.let { "Question ${currentQuestionIndex + 1} of ${QuizViewModel.QUESTIONS_PER_QUIZ}: ${it.text}" } ?: "Loading quiz questions"
    }
    
    // Timer color changes as time runs out
    val timerColor = when {
        isAncient -> colorResource(R.color.ancient_progress)
        timerValue.value > 30000 -> primaryColor // Normal color for most of the time
        timerValue.value > 20000 -> Color(0xFFFFA000) // Amber when under 30 seconds
        timerValue.value > 10000 -> Color(0xFFFF6D00) // Orange when under 20 seconds
        else -> Color(0xFFD32F2F) // Red when under 10 seconds
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        quizTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = textColor
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarColor,
                    titleContentColor = textColor
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { 
                            navController.popBackStack() 
                        },
                        modifier = Modifier.semantics { 
                            contentDescription = "Go back to levels"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back to levels",
                            tint = iconTint
                        )
                    }
                },
                actions = {
                    // Show timer
                    if (quizState == QuizState.Active) {
                        Box(
                            modifier = Modifier
                                .semantics { 
                                    contentDescription = "Remaining time: ${formatTime(timerState.remainingTimeMs)}"
                                }
                                .padding(end = 8.dp)
                                .clip(CircleShape)
                                .background(if (isAncient) primaryColor else Color.Transparent)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = formatTime(timerState.remainingTimeMs),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = when {
                                    isAncient -> answerTextLight
                                    timerState.remainingTimeMs > 60000 -> MaterialTheme.colorScheme.onPrimary
                                    timerState.remainingTimeMs > 30000 -> Color.Yellow
                                    else -> Color.Red
                                }
                            )
                        }
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
            // Loading state
            when (quizState) {
                QuizState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.semantics { 
                                contentDescription = "Loading quiz questions"
                            }
                        ) {
                            CircularProgressIndicator(
                                color = primaryColor,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading Questions...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                
                is QuizState.Error -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Failed to load questions",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = (quizState as QuizState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    navController.popBackStack()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Navigate back to levels"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Back to Levels")
                            }
                        }
                    }
                }
                
                QuizState.Completed -> {
                    // Results are shown on the ResultsScreen after navigation
                    LaunchedEffect(Unit) {
                        val results = quizResults
                        if (results != null) {
                            // Navigate to results screen
                            navController.navigate(
                                NavDestinations.RESULTS_ROUTE.replace(
                                    "{categoryId}", categoryId
                                ).replace(
                                    "{levelId}", levelId
                                ).replace(
                                    "{score}", results.score.toString()
                                ).replace(
                                    "{stars}", results.stars.toString()
                                ).replace(
                                    "{correct}", results.correctAnswers.toString()
                                ).replace(
                                    "{total}", results.totalQuestions.toString()
                                )
                            ) {
                                // Pop up to the level selection screen
                                popUpTo(NavDestinations.LEVEL_SELECTION_ROUTE) {
                                    inclusive = false
                                }
                            }
                        } else {
                            // Something went wrong, just go back
                            navController.popBackStack()
                        }
                    }
                }
                
                else -> {
                    // Quiz content - ready or active state
                    if (currentQuestion != null && currentAnswers.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                                .semantics { 
                                    contentDescription = questionAccessibilityDesc
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Progress indicator
                            LinearProgressIndicator(
                                progress = progressValue,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = primaryColor,
                                trackColor = answerUnselectedColor
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Question card
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 4.dp
                                ),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = if (isAncient) cardBackgroundLight else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Question number
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(primaryColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${currentQuestionIndex + 1}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = answerTextLight,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Question
                                    Text(
                                        text = currentQuestion.text,
                                        style = MaterialTheme.typography.titleLarge,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Timer section
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 8.dp)
                                    .semantics { contentDescription = "Timer" }
                            ) {
                                // Background
                                val timerProgress = (timerValue.value.toFloat() / 30000f).coerceIn(0f, 1f)
                                
                                LinearProgressIndicator(
                                    progress = timerProgress,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp),
                                    color = timerColor,
                                    trackColor = timerColor.copy(alpha = 0.2f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Answer options
                            currentAnswers.forEach { answer ->
                                val isSelected = selectedAnswerId == answer.id
                                val isCorrect = answer.isCorrect
                                val showFeedback = selectedAnswerId != null
                                
                                // Determine colors based on state
                                val answerBackgroundColor = when {
                                    // If showing feedback and this is the correct answer, always show green regardless of selection
                                    showFeedback && isCorrect -> answerCorrectColor
                                    // If showing feedback and this was selected but incorrect, show red
                                    showFeedback && isSelected -> answerIncorrectColor
                                    // If selected but not showing feedback yet, show the selected color
                                    isSelected -> answerSelectedColor
                                    // Otherwise, show default color
                                    else -> answerUnselectedColor
                                }
                                
                                // Text is white on colored backgrounds, black on white background
                                val textColor = when {
                                    showFeedback && isCorrect -> answerTextLight
                                    showFeedback && isSelected -> answerTextLight
                                    isSelected -> answerTextLight
                                    else -> answerTextDark
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Answer card
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Only process click if no answer is selected yet
                                            if (selectedAnswerId == null && quizState == QuizState.Active) {
                                                quizViewModel.selectAnswer(answer.id)
                                                
                                                // Use the coroutine scope to delay before moving to next question
                                                coroutineScope.launch {
                                                    // Show feedback for 1.5 seconds
                                                    delay(1500)
                                                    quizViewModel.nextQuestion()
                                                }
                                            }
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = answerBackgroundColor,
                                        disabledContainerColor = answerBackgroundColor
                                    ),
                                    elevation = CardDefaults.elevatedCardElevation(
                                        defaultElevation = if (isSelected || (showFeedback && isCorrect)) 4.dp else 1.dp,
                                        disabledElevation = if (isSelected || (showFeedback && isCorrect)) 4.dp else 1.dp
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = answer.text,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = if (isSelected || (showFeedback && isCorrect)) 
                                                    FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = textColor,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Overlay for time's up alert
            if (showTimeExpirationAlert) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .clickable(enabled = false) { /* Prevent clicks passing through */ },
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
                                imageVector = Icons.Default.Close,
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
                                text = "You didn't complete the quiz in time. This level is still locked. Please try again.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Button(
                                onClick = {
                                    navController.navigate(
                                        NavDestinations.LEVEL_SELECTION_ROUTE
                                            .replace("{categoryId}", categoryId)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
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
}

@Preview
@Composable
fun QuizScreenPreview() {
    ImperiumTheme {
        QuizScreen(
            navController = rememberNavController(),
            categoryId = "ancient",
            levelId = "1",
            quizType = "STANDARD"
        )
    }
} 