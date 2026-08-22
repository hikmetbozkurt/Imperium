package com.hikmet.imperium.feature.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.domain.game.QuizSession
import com.hikmet.imperium.ui.theme.CorrectAnswer
import com.hikmet.imperium.ui.theme.IncorrectAnswer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onBack: () -> Unit,
    onResult: (Long) -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is QuizEvent.OpenResult -> onResult(event.attemptId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.category?.title ?: "Quiz") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Leave quiz")
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when {
                state.error != null -> Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error)
                state.session == null -> CircularProgressIndicator()
                else -> QuizContent(
                    session = requireNotNull(state.session),
                    isSaving = state.isSaving,
                    onAnswer = viewModel::selectAnswer,
                    onNext = viewModel::nextQuestion,
                )
            }
        }
    }
}

@Composable
private fun QuizContent(
    session: QuizSession,
    isSaving: Boolean,
    onAnswer: (Int) -> Unit,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Question ${session.currentQuestionIndex + 1} of ${session.questions.size}",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
            )
            Icon(Icons.Default.AccessTime, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("${(session.remainingTimeMs + 999) / 1000}s", fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(
            progress = { session.remainingTimeMs.toFloat() / GameRules.DEFAULT_QUIZ_DURATION_MS },
            modifier = Modifier.fillMaxWidth().height(8.dp),
        )
        AnimatedContent(targetState = session.currentQuestion, label = "question") { question ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = question.text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(22.dp),
                )
            }
        }
        session.currentQuestion.options.forEachIndexed { index, option ->
            AnswerCard(
                text = option,
                index = index,
                selectedIndex = session.selectedAnswerIndex,
                correctIndex = session.currentQuestion.correctAnswerIndex,
                onClick = { onAnswer(index) },
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onNext,
            enabled = session.selectedAnswerIndex != null && !isSaving,
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.height(22.dp), strokeWidth = 2.dp)
            } else {
                Text(if (session.isLastQuestion) "Finish" else "Next question")
            }
        }
    }
}

@Composable
private fun AnswerCard(
    text: String,
    index: Int,
    selectedIndex: Int?,
    correctIndex: Int,
    onClick: () -> Unit,
) {
    val isRevealed = selectedIndex != null
    val isCorrect = index == correctIndex
    val isSelected = index == selectedIndex
    val container = when {
        isRevealed && isCorrect -> CorrectAnswer
        isRevealed && isSelected -> IncorrectAnswer
        else -> MaterialTheme.colorScheme.surfaceContainer
    }
    val border = when {
        isRevealed && isCorrect -> MaterialTheme.colorScheme.primary
        isSelected -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        onClick = onClick,
        enabled = selectedIndex == null,
        colors = CardDefaults.cardColors(containerColor = container),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isRevealed && isCorrect) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isRevealed && isCorrect) Color(0xFF2E7D32) else MaterialTheme.colorScheme.outline,
            )
            Spacer(Modifier.width(12.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
