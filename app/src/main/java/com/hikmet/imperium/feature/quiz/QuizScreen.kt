package com.hikmet.imperium.feature.quiz

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.game.QuizSession
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.ui.theme.ImperiumMotion

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
        containerColor = ImperiumQuizColors.Background,
        contentColor = ImperiumQuizColors.OnSurface,
        topBar = {
            QuizTopBar(
                categoryTitle = state.category?.title,
                onBack = onBack,
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
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.error != null -> Text(
                    text = state.error.orEmpty(),
                    color = ImperiumQuizColors.Error,
                    modifier = Modifier.padding(24.dp),
                )

                state.session == null || state.category == null -> CircularProgressIndicator(
                    color = ImperiumQuizColors.Gold,
                )

                else -> QuizContent(
                    category = requireNotNull(state.category),
                    session = requireNotNull(state.session),
                    onAnswer = viewModel::selectAnswer,
                )
            }
        }
    }
}

@Composable
private fun QuizContent(
    category: HistoryCategory,
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

    val levelTitle = category.levels
        .firstOrNull { it.number == session.levelNumber }
        ?.title
        ?: stringResource(R.string.quiz_level_fallback, session.levelNumber)

    LazyColumn(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "quiz-progress") {
            QuizProgressHeader(
                currentQuestion = session.currentQuestionIndex + 1,
                totalQuestions = session.questions.size,
                levelTitle = levelTitle,
                remainingTimeMs = session.remainingTimeMs,
                isTimerFrozen = session.isAnswerRevealed,
            )
        }

        item(key = "combo-progress") {
            ComboProgressCard(session)
        }

        item(key = "question-card") {
            AnimatedContent(
                targetState = session.currentQuestion,
                transitionSpec = {
                    (slideInHorizontally(
                        animationSpec = tween(ImperiumMotion.Standard),
                        initialOffsetX = { it / 5 },
                    ) + fadeIn()) togetherWith
                        (slideOutHorizontally(
                            animationSpec = tween(ImperiumMotion.Quick),
                            targetOffsetX = { -it / 6 },
                        ) + fadeOut())
                },
                label = "question",
            ) { question ->
                HistoricalQuestionCard(
                    category = category,
                    levelTitle = levelTitle,
                    question = question,
                )
            }
        }

        itemsIndexed(
            items = session.currentQuestion.options,
            key = { index, _ -> "${session.currentQuestion.id}-$index" },
        ) { index, option ->
            AnswerOptionTile(
                text = option,
                index = index,
                visualState = answerVisualState(
                    index = index,
                    selectedIndex = session.selectedAnswerIndex,
                    correctIndex = session.currentQuestion.correctAnswerIndex,
                    isRevealed = session.isAnswerRevealed,
                ),
                enabled = !session.isAnswerRevealed,
                onClick = { onAnswer(index) },
            )
        }

        if (session.isAnswerRevealed) {
            item(key = "feedback-${session.currentQuestion.id}") {
                QuizFeedbackCard(
                    explanation = session.currentQuestion.explanation,
                    correctAnswer = session.currentQuestion.options[session.currentQuestion.correctAnswerIndex],
                )
            }
        }

        item(key = "lifelines") {
            QuizLifelineBar()
        }

        item(key = "bottom-space") {
            Spacer(Modifier.height(4.dp))
        }
    }
}
