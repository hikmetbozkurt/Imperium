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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.category.CategoryDetail
import com.hikmet.imperium.ui.category.categoryDetails
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
    // Get category details
    val categoryDetail = remember<CategoryDetail>(categoryId) {
        categoryDetails[categoryId] ?: categoryDetails["ancient"]!!
    }
    
    // Get gradient colors based on category
    val (gradientStart, gradientEnd) = remember<Pair<Color, Color>>(categoryId) {
        when (categoryId) {
            "ancient" -> Pair(AncientGradientStart, AncientGradientEnd)
            "medieval" -> Pair(MedievalGradientStart, MedievalGradientStart.copy(alpha = 0.7f))
            "renaissance" -> Pair(RenaissanceGradientStart, RenaissanceGradientStart.copy(alpha = 0.7f))
            "modern" -> Pair(ModernGradientStart, ModernGradientStart.copy(alpha = 0.7f))
            "world_wars" -> Pair(WorldWarsGradientStart, WorldWarsGradientStart.copy(alpha = 0.7f))
            else -> Pair(AncientGradientStart, AncientGradientEnd)
        }
    }
    
    // Parse quiz type
    val quizTypeEnum = remember<QuizType>(quizType) {
        try {
            QuizType.valueOf(quizType)
        } catch (e: IllegalArgumentException) {
            QuizType.STANDARD
        }
    }
    
    // Create a meaningful title for the quiz
    val quizTitle = remember<String>(categoryDetail, quizTypeEnum) {
        "${categoryDetail.title}: ${
            when (quizTypeEnum) {
                QuizType.STANDARD -> "Quiz"
                QuizType.TIME_ATTACK -> "Time Challenge"
                QuizType.IMAGE_BASED -> "Image Quiz"
            }
        }"
    }
    
    // State for loading questions
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    
    // Get questions for this category and level
    val questions = remember<List<Question>>(categoryId, levelId) {
        when (categoryId) {
            "ancient" -> ancientQuestions
            "medieval" -> medievalQuestions
            "renaissance" -> renaissanceQuestions
            "modern" -> modernQuestions
            "world_wars" -> worldWarsQuestions
            else -> ancientQuestions
        }
    }
    
    // Simulate loading delay for demo purposes
    LaunchedEffect(categoryId, levelId) {
        isLoading = true
        hasError = false
        
        // Simulate network delay
        delay(800)
        
        // Check if we have questions for this category
        if (questions.isNotEmpty()) {
            isLoading = false
        } else {
            isLoading = false
            hasError = true
        }
    }
    
    // State for the quiz
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var showFeedback by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    var quizCompleted by remember { mutableStateOf(false) }
    
    // Timer for time attack mode
    var remainingTime by remember { mutableIntStateOf(if (quizTypeEnum == QuizType.TIME_ATTACK) 30 else -1) }
    
    // Start the timer if in TIME_ATTACK mode
    LaunchedEffect(quizTypeEnum, currentQuestionIndex) {
        if (quizTypeEnum == QuizType.TIME_ATTACK && !quizCompleted) {
            remainingTime = 30
            while (remainingTime > 0 && !quizCompleted) {
                delay(1000)
                remainingTime--
            }
            
            // Time's up - auto move to next question
            if (remainingTime <= 0 && !quizCompleted && selectedAnswerIndex == -1) {
                selectedAnswerIndex = -2 // Special value for timeout
                showFeedback = true
                delay(1500) // Show timeout feedback briefly
                
                // Move to next question or end quiz
                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                    selectedAnswerIndex = -1
                    showFeedback = false
                } else {
                    quizCompleted = true
                    // Simple navigation - just go back to levels
                    navController.popBackStack()
                }
            }
        }
    }
    
    // Coroutine scope for animations and delayed actions
    val coroutineScope = rememberCoroutineScope()
    
    // Calculate progress
    val progress = remember<Float>(currentQuestionIndex, questions.size) {
        if (questions.isEmpty()) 0f else (currentQuestionIndex.toFloat() / questions.size.toFloat())
    }
    
    // Accessibility description for current question
    val questionAccessibilityDesc = if (!isLoading && !hasError && questions.isNotEmpty() && currentQuestionIndex < questions.size) {
        "Question ${currentQuestionIndex + 1} of ${questions.size}: ${questions[currentQuestionIndex].text}"
    } else {
        "Loading quiz questions"
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
                        color = MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = gradientStart,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                            contentDescription = "Navigate back to levels"
                        )
                    }
                },
                actions = {
                    if (quizTypeEnum == QuizType.TIME_ATTACK && !quizCompleted && !isLoading && !hasError) {
                        Box(
                            modifier = Modifier
                                .semantics { 
                                    contentDescription = "Remaining time: $remainingTime seconds"
                                }
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = remainingTime.toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = when {
                                    remainingTime > 10 -> MaterialTheme.colorScheme.onPrimary
                                    remainingTime > 5 -> Color.Yellow
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            gradientStart.copy(alpha = 0.1f),
                            gradientEnd.copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            // Loading state
            if (isLoading) {
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
                            color = gradientStart
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading questions...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            // Error state
            else if (hasError || questions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(32.dp)
                            .semantics { 
                                contentDescription = "Error loading questions for this quiz"
                            }
                    ) {
                        Text(
                            text = "No questions available",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "We couldn't find any questions for ${categoryDetail.title}. Please try another category.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.semantics { 
                                contentDescription = "Return to levels" 
                            },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = gradientStart
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
            // Quiz content
            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .semantics { 
                            contentDescription = questionAccessibilityDesc
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Progress indicator
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .semantics { 
                                contentDescription = "Question ${currentQuestionIndex + 1} of ${questions.size}"
                            },
                        color = gradientStart,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Question number
                    Text(
                        text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = gradientStart,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Question
                    Text(
                        text = questions[currentQuestionIndex].text,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Answer options
                    questions[currentQuestionIndex].options.forEachIndexed { index, option ->
                        val isSelected = selectedAnswerIndex == index
                        val isCorrect = index == questions[currentQuestionIndex].correctAnswerIndex
                        
                        val backgroundColor = when {
                            showFeedback && isCorrect -> CorrectAnswer
                            showFeedback && isSelected && !isCorrect -> IncorrectAnswer
                            isSelected -> gradientStart.copy(alpha = 0.7f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                        
                        val textColor = when {
                            showFeedback && (isCorrect || isSelected) -> Color.White
                            isSelected -> Color.White
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                        
                        val answerState = when {
                            showFeedback && isCorrect -> "Correct answer"
                            showFeedback && isSelected && !isCorrect -> "Incorrect answer"
                            isSelected -> "Selected"
                            else -> "Option ${index + 1}"
                        }
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .semantics { 
                                    contentDescription = "$answerState: $option"
                                },
                            onClick = {
                                // Only process click if we're in a valid state
                                if (!showFeedback && !quizCompleted && selectedAnswerIndex == -1) {
                                    // Update state
                                    selectedAnswerIndex = index
                                    showFeedback = true
                                    
                                    // Check if answer is correct
                                    if (index == questions[currentQuestionIndex].correctAnswerIndex) {
                                        score++
                                        isAnswerCorrect = true
                                    } else {
                                        isAnswerCorrect = false
                                    }
                                    
                                    // Use the coroutine scope to delay before moving to next question
                                    coroutineScope.launch {
                                        // Show feedback for 1.5 seconds
                                        delay(1500)
                                        
                                        // Move to next question or end quiz
                                        if (currentQuestionIndex < questions.size - 1) {
                                            // Go to next question
                                            currentQuestionIndex++
                                            selectedAnswerIndex = -1
                                            showFeedback = false
                                        } else {
                                            // Quiz is done
                                            quizCompleted = true
                                            
                                            // Just go back instead of trying to navigate to results
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = backgroundColor
                            ),
                            enabled = !showFeedback && !quizCompleted
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    
                    // Show timeout message if no answer was selected
                    if (showFeedback && selectedAnswerIndex == -2) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Time's up! The correct answer was: ${questions[currentQuestionIndex].options[questions[currentQuestionIndex].correctAnswerIndex]}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.semantics { 
                                contentDescription = "Time's up notification. The correct answer was: ${questions[currentQuestionIndex].options[questions[currentQuestionIndex].correctAnswerIndex]}"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
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