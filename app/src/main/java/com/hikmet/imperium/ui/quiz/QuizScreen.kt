package com.hikmet.imperium.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.navigation.NavDestinations
import com.hikmet.imperium.ui.theme.CorrectAnswer
import com.hikmet.imperium.ui.theme.ImperiumTheme
import com.hikmet.imperium.ui.theme.IncorrectAnswer
import com.hikmet.imperium.ui.theme.SelectedAnswer
import kotlinx.coroutines.delay

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
 * Sample questions for preview
 */
val sampleQuestions = listOf(
    Question(
        id = 1,
        text = "Which emperor built the Colosseum in Rome?",
        imageResId = R.drawable.ic_launcher_foreground,
        options = listOf("Augustus", "Vespasian", "Nero", "Constantine"),
        correctAnswerIndex = 1
    ),
    Question(
        id = 2,
        text = "In which year did World War II end?",
        imageResId = R.drawable.ic_launcher_foreground,
        options = listOf("1943", "1944", "1945", "1946"),
        correctAnswerIndex = 2
    ),
    Question(
        id = 3,
        text = "Who painted the ceiling of the Sistine Chapel?",
        imageResId = R.drawable.ic_launcher_foreground,
        options = listOf("Leonardo da Vinci", "Raphael", "Michelangelo", "Donatello"),
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
    categoryId: String?,
    levelId: String?,
    quizType: String? = "STANDARD"
) {
    val questions = sampleQuestions
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    
    val quizTypeEnum = remember {
        when (quizType) {
            "TIME_ATTACK" -> QuizType.TIME_ATTACK
            "IMAGE_BASED" -> QuizType.IMAGE_BASED
            else -> QuizType.STANDARD
        }
    }
    
    // Timer for Time Attack mode
    var timeRemaining by remember { mutableFloatStateOf(1f) }
    val isTimeAttack = quizTypeEnum == QuizType.TIME_ATTACK
    
    LaunchedEffect(currentQuestionIndex, isTimeAttack) {
        if (isTimeAttack) {
            timeRemaining = 1f
            while (timeRemaining > 0) {
                delay(100)
                timeRemaining -= 0.01f
                if (timeRemaining <= 0) {
                    // Time's up, move to next question
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                        selectedAnswerIndex = -1
                    } else {
                        // End of quiz
                        navController.navigate(
                            "results/$categoryId/$levelId/$score"
                        ) {
                            popUpTo("quiz/$categoryId/$levelId/$quizType") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
    
    // Handle answer checking
    LaunchedEffect(showResult) {
        if (showResult) {
            delay(1500) // Show result for 1.5 seconds
            showResult = false
            
            // Update score if correct
            if (selectedAnswerIndex == questions[currentQuestionIndex].correctAnswerIndex) {
                score++
            }
            
            // Move to next question or end of quiz
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                selectedAnswerIndex = -1
            } else {
                // End of quiz
                navController.navigate(
                    "results/$categoryId/$levelId/$score"
                ) {
                    popUpTo("quiz/$categoryId/$levelId/$quizType") { inclusive = true }
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Quiz",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    if (isTimeAttack) {
                        Text(
                            text = "${(timeRemaining * 30).toInt()}s",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress indicator
                LinearProgressIndicator(
                    progress = { (currentQuestionIndex.toFloat() + 1) / questions.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isTimeAttack) {
                    // Time Attack progress bar
                    LinearProgressIndicator(
                        progress = { timeRemaining },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Question image
                Image(
                    painter = painterResource(
                        id = questions[currentQuestionIndex].imageResId
                    ),
                    contentDescription = "Question Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Question text
                Text(
                    text = questions[currentQuestionIndex].text,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Answer options
                AnswerGrid(
                    options = questions[currentQuestionIndex].options,
                    selectedAnswerIndex = selectedAnswerIndex,
                    correctAnswerIndex = if (showResult) 
                        questions[currentQuestionIndex].correctAnswerIndex 
                    else -1,
                    onOptionSelected = { index ->
                        if (selectedAnswerIndex == -1 && !showResult) {
                            selectedAnswerIndex = index
                            showResult = true
                        }
                    }
                )
            }
        }
    )
}

/**
 * Grid of answer options in 2x2 layout
 */
@Composable
fun AnswerGrid(
    options: List<String>,
    selectedAnswerIndex: Int,
    correctAnswerIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnswerOption(
                text = options[0],
                isSelected = selectedAnswerIndex == 0,
                isCorrect = correctAnswerIndex == 0,
                isIncorrect = selectedAnswerIndex == 0 && correctAnswerIndex != 0 && correctAnswerIndex != -1,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(0) }
            )
            AnswerOption(
                text = options[1],
                isSelected = selectedAnswerIndex == 1,
                isCorrect = correctAnswerIndex == 1,
                isIncorrect = selectedAnswerIndex == 1 && correctAnswerIndex != 1 && correctAnswerIndex != -1,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(1) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnswerOption(
                text = options[2],
                isSelected = selectedAnswerIndex == 2,
                isCorrect = correctAnswerIndex == 2,
                isIncorrect = selectedAnswerIndex == 2 && correctAnswerIndex != 2 && correctAnswerIndex != -1,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(2) }
            )
            AnswerOption(
                text = options[3],
                isSelected = selectedAnswerIndex == 3,
                isCorrect = correctAnswerIndex == 3,
                isIncorrect = selectedAnswerIndex == 3 && correctAnswerIndex != 3 && correctAnswerIndex != -1,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(3) }
            )
        }
    }
}

/**
 * Individual answer option
 */
@Composable
fun AnswerOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isIncorrect: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isCorrect -> CorrectAnswer
        isIncorrect -> IncorrectAnswer
        isSelected -> SelectedAnswer
        else -> MaterialTheme.colorScheme.surface
    }
    
    val borderColor = when {
        isCorrect -> MaterialTheme.colorScheme.primary
        isIncorrect -> MaterialTheme.colorScheme.error
        isSelected -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    
    val pulsateAnimation = rememberInfiniteTransition(label = "correct_answer_animation")
    val scale = if (isCorrect) {
        pulsateAnimation.animateFloat(
            initialValue = 1f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale_animation"
        ).value
    } else 1f
    
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(enabled = !isSelected && !isCorrect && !isIncorrect) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
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

@Preview(showBackground = true)
@Composable
fun AnswerGridPreview() {
    ImperiumTheme {
        AnswerGrid(
            options = listOf("Option A", "Option B", "Option C", "Option D"),
            selectedAnswerIndex = 0,
            correctAnswerIndex = 2,
            onOptionSelected = {}
        )
    }
} 