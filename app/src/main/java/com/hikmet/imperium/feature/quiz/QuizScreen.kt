package com.hikmet.imperium.feature.quiz

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hikmet.imperium.R
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.domain.game.QuizSession
import com.hikmet.imperium.ui.theme.CorrectAnswer
import com.hikmet.imperium.ui.theme.IncorrectAnswer
import com.hikmet.imperium.ui.theme.ImperiumMotion
import com.hikmet.imperium.ui.components.ImperiumBackdrop

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

    ImperiumBackdrop(Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(state.category?.title ?: stringResource(R.string.quiz_title)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.quiz_leave_content_description),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
            bottomBar = {
                state.session?.let { session ->
                    if (state.error == null) {
                        QuizActionBar(
                            session = session,
                            isSaving = state.isSaving,
                            onNext = viewModel::nextQuestion,
                        )
                    }
                }
            },
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                when {
                    state.error != null -> Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error)
                    state.session == null -> CircularProgressIndicator()
                    else -> QuizContent(
                        session = requireNotNull(state.session),
                        onAnswer = viewModel::selectAnswer,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizContent(
    session: QuizSession,
    onAnswer: (Int) -> Unit,
) {
    val view = LocalView.current
    var handledResponseCount by remember(session.sessionSeed) {
        mutableIntStateOf(session.responses.size)
    }
    LaunchedEffect(session.responses.size) {
        if (session.responses.size > handledResponseCount) {
            val response = session.responses.last()
            view.performHapticFeedback(
                if (response.isCorrect) HapticFeedbackConstants.CONFIRM else HapticFeedbackConstants.REJECT,
            )
            handledResponseCount = session.responses.size
        }
    }
    val timerColor by animateColorAsState(
        targetValue = when {
            session.remainingTimeMs <= GameRules.TIMER_CRITICAL_THRESHOLD_MS -> MaterialTheme.colorScheme.error
            session.remainingTimeMs <= GameRules.TIMER_WARNING_THRESHOLD_MS -> Color(0xFFF59E0B)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(ImperiumMotion.Standard),
        label = "question-timer-color",
    )

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 720.dp)
                .fillMaxSize()
                .align(Alignment.TopCenter),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 20.dp,
                vertical = 12.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
        item(key = "quiz-status") {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(
                        R.string.quiz_question_progress,
                        session.currentQuestionIndex + 1,
                        session.questions.size,
                    ),
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                )
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = timerColor)
                Spacer(Modifier.width(6.dp))
                Text(
                    stringResource(R.string.quiz_seconds, (session.remainingTimeMs + 999) / 1000),
                    fontWeight = FontWeight.Bold,
                    color = timerColor,
                )
            }
        }
        item(key = "quiz-timer") {
            LinearProgressIndicator(
                progress = {
                    session.remainingTimeMs.toFloat() /
                        GameRules.QUESTION_DURATION_MS.toFloat()
                },
                color = timerColor,
                modifier = Modifier.fillMaxWidth().height(8.dp),
            )
        }
        item(key = "question-card") {
            AnimatedContent(
                targetState = session.currentQuestion,
                transitionSpec = {
                    (slideInHorizontally(
                        animationSpec = tween(ImperiumMotion.Standard),
                        initialOffsetX = { it / 5 },
                    ) + fadeIn(tween(ImperiumMotion.Standard))) togetherWith
                        (slideOutHorizontally(
                            animationSpec = tween(ImperiumMotion.Quick),
                            targetOffsetX = { -it / 6 },
                        ) + fadeOut(tween(ImperiumMotion.Quick)))
                },
                label = "question",
            ) { question ->
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
        }
        itemsIndexed(
            items = session.currentQuestion.options,
            key = { index, _ -> "${session.currentQuestion.id}-$index" },
        ) { index, option ->
            AnswerCard(
                text = option,
                index = index,
                selectedIndex = session.selectedAnswerIndex,
                correctIndex = session.currentQuestion.correctAnswerIndex,
                isRevealed = session.isAnswerRevealed,
                onClick = { onAnswer(index) },
            )
        }

        }

    }
}

@Composable
private fun QuizActionBar(
    session: QuizSession,
    isSaving: Boolean,
    onNext: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Button(
                onClick = onNext,
                enabled = session.isAnswerRevealed && !isSaving,
                modifier = Modifier.widthIn(max = 680.dp).fillMaxWidth().height(52.dp),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.height(22.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        stringResource(
                            if (session.isLastQuestion) R.string.quiz_finish else R.string.quiz_next_question,
                        ),
                    )
                }
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
    isRevealed: Boolean,
    onClick: () -> Unit,
) {
    val isCorrect = index == correctIndex
    val isSelected = index == selectedIndex
    val targetContainer = when {
        isRevealed && isCorrect -> CorrectAnswer
        isRevealed && isSelected -> IncorrectAnswer
        else -> MaterialTheme.colorScheme.surfaceContainer
    }
    val targetBorder = when {
        isRevealed && isCorrect -> MaterialTheme.colorScheme.primary
        isSelected -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val container by animateColorAsState(
        targetValue = targetContainer,
        animationSpec = tween(ImperiumMotion.Standard),
        label = "answer-container",
    )
    val border by animateColorAsState(
        targetValue = targetBorder,
        animationSpec = tween(ImperiumMotion.Standard),
        label = "answer-border",
    )
    val shake = remember { Animatable(0f) }
    val answerState = when {
        isRevealed && isCorrect -> stringResource(R.string.quiz_option_correct)
        isRevealed && isSelected -> stringResource(R.string.quiz_option_incorrect)
        isSelected -> stringResource(R.string.quiz_option_selected)
        else -> null
    }
    LaunchedEffect(isRevealed, isSelected, isCorrect) {
        if (isRevealed && isSelected && !isCorrect) {
            shake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = ImperiumMotion.Emphasis
                    -12f at 70
                    10f at 150
                    -7f at 230
                    5f at 310
                    0f at ImperiumMotion.Emphasis
                },
            )
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = container),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { translationX = shake.value }
            .clickable(enabled = !isRevealed, role = Role.Button, onClick = onClick)
            .semantics {
                answerState?.let { stateDescription = it }
            },
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = when {
                    isRevealed && isCorrect -> Icons.Default.CheckCircle
                    isRevealed && isSelected -> Icons.Default.Cancel
                    else -> Icons.Default.RadioButtonUnchecked
                },
                contentDescription = null,
                tint = when {
                    isRevealed && isCorrect -> Color(0xFF2E7D32)
                    isRevealed && isSelected -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.outline
                },
            )
            Spacer(Modifier.width(12.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
