package com.hikmet.imperium.feature.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.game.GameRules
import com.hikmet.imperium.domain.game.QuizSession
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.model.QuizQuestion
import com.hikmet.imperium.ui.components.categoryIcon
import com.hikmet.imperium.ui.components.visualAssets
import com.hikmet.imperium.ui.theme.ImperiumMotion
import kotlin.math.ceil

internal object ImperiumQuizColors {
    val Background = Color(0xFF161214)
    val SurfaceLowest = Color(0xFF100D0F)
    val SurfaceLow = Color(0xFF1E1B1D)
    val Surface = Color(0xFF221F21)
    val SurfaceHigh = Color(0xFF2D292B)
    val SurfaceHighest = Color(0xFF383436)
    val OnSurface = Color(0xFFE8E0E3)
    val OnSurfaceVariant = Color(0xFFD9C1C3)
    val Muted = Color(0xFFA18C8D)
    val Outline = Color(0xFF534344)
    val Burgundy = Color(0xFF5A1827)
    val BurgundyLight = Color(0xFFD97D8C)
    val Gold = Color(0xFFE9C176)
    val GoldLight = Color(0xFFFFDEA5)
    val GoldDark = Color(0xFF604403)
    val Correct = Color(0xFF2E6F40)
    val CorrectSurface = Color(0xFF173922)
    val Error = Color(0xFFFFB4AB)
    val ErrorSurface = Color(0xFF5C171B)
    val Warning = Color(0xFFD48B36)
}

private object ImperiumQuizTypography {
    val Monument = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.7.sp,
    )
    val Question = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 25.sp,
    )
    val Answer = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    )
    val Label = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.1.sp,
    )
    val Body = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.sp,
    )
}

private val TabletShape = RoundedCornerShape(8.dp)
private val TileShape = RoundedCornerShape(4.dp)

internal enum class QuizAnswerVisualState {
    Default,
    Selected,
    Correct,
    Incorrect,
    Dimmed,
}

internal enum class QuizLayoutDensity { Compact, Comfortable }

internal fun quizLayoutDensity(availableHeightDp: Int): QuizLayoutDensity =
    if (availableHeightDp < 720) QuizLayoutDensity.Compact else QuizLayoutDensity.Comfortable

internal enum class QuizFeedbackKind { Correct, Incorrect, TimeUp }

internal fun quizFeedbackKind(
    selectedIndex: Int?,
    correctIndex: Int,
    timedOut: Boolean,
): QuizFeedbackKind? = when {
    timedOut -> QuizFeedbackKind.TimeUp
    selectedIndex == null -> null
    selectedIndex == correctIndex -> QuizFeedbackKind.Correct
    else -> QuizFeedbackKind.Incorrect
}

internal fun answerVisualState(
    index: Int,
    selectedIndex: Int?,
    correctIndex: Int,
    isRevealed: Boolean,
): QuizAnswerVisualState = when {
    !isRevealed && index == selectedIndex -> QuizAnswerVisualState.Selected
    !isRevealed -> QuizAnswerVisualState.Default
    index == correctIndex -> QuizAnswerVisualState.Correct
    index == selectedIndex -> QuizAnswerVisualState.Incorrect
    else -> QuizAnswerVisualState.Dimmed
}

internal fun romanNumeral(value: Int): String {
    if (value <= 0) return "0"
    var remainder = value
    val result = StringBuilder()
    val numerals = listOf(
        1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
        100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
        10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I",
    )
    numerals.forEach { (number, glyph) ->
        while (remainder >= number) {
            result.append(glyph)
            remainder -= number
        }
    }
    return result.toString()
}

@Composable
internal fun QuizTopBar(
    categoryTitle: String?,
    onBack: () -> Unit,
) {
    Surface(
        color = ImperiumQuizColors.SurfaceLowest.copy(alpha = 0.98f),
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.quiz_leave_content_description),
                    tint = ImperiumQuizColors.Gold,
                )
            }
            Surface(
                modifier = Modifier.size(34.dp),
                shape = CircleShape,
                color = ImperiumQuizColors.GoldDark,
                border = BorderStroke(1.dp, ImperiumQuizColors.Gold.copy(alpha = 0.65f)),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = ImperiumQuizColors.GoldLight,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
            ) {
                Text(
                    text = stringResource(R.string.quiz_trial_title),
                    color = ImperiumQuizColors.OnSurface,
                    style = ImperiumQuizTypography.Monument,
                    maxLines = 1,
                )
                if (categoryTitle != null) {
                    Text(
                        text = categoryTitle.uppercase(),
                        color = ImperiumQuizColors.Muted,
                        style = ImperiumQuizTypography.Label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Surface(
                modifier = Modifier.size(34.dp),
                shape = CircleShape,
                color = ImperiumQuizColors.Burgundy,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = ImperiumQuizColors.BurgundyLight,
                        modifier = Modifier.size(19.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun QuizProgressHeader(
    currentQuestion: Int,
    totalQuestions: Int,
    levelTitle: String,
    remainingTimeMs: Long,
    isTimerFrozen: Boolean,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val timerColor = when {
        remainingTimeMs <= GameRules.TIMER_CRITICAL_THRESHOLD_MS -> ImperiumQuizColors.Error
        remainingTimeMs <= GameRules.TIMER_WARNING_THRESHOLD_MS -> ImperiumQuizColors.Warning
        else -> ImperiumQuizColors.Gold
    }
    val animatedTimerColor by animateColorAsState(
        targetValue = timerColor,
        animationSpec = tween(ImperiumMotion.Standard),
        label = "timer-color",
    )
    val progress = (remainingTimeMs.toFloat() / GameRules.QUESTION_DURATION_MS).coerceIn(0f, 1f)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(if (compact) 34.dp else 40.dp),
                shape = CircleShape,
                color = ImperiumQuizColors.SurfaceHigh,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = ImperiumQuizColors.Gold,
                        modifier = Modifier.size(if (compact) 17.dp else 19.dp),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(
                        R.string.quiz_questio_progress,
                        romanNumeral(currentQuestion),
                        romanNumeral(totalQuestions),
                    ),
                    color = ImperiumQuizColors.Gold,
                    style = ImperiumQuizTypography.Monument,
                    maxLines = 1,
                )
                Text(
                    text = levelTitle.uppercase(),
                    color = ImperiumQuizColors.OnSurfaceVariant,
                    style = ImperiumQuizTypography.Label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            RomanCircularTimer(
                remainingTimeMs = remainingTimeMs,
                progress = progress,
                color = animatedTimerColor,
                isFrozen = isTimerFrozen,
                compact = compact,
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(CircleShape),
            color = animatedTimerColor,
            trackColor = ImperiumQuizColors.SurfaceHighest,
        )
    }
}

@Composable
private fun RomanCircularTimer(
    remainingTimeMs: Long,
    progress: Float,
    color: Color,
    isFrozen: Boolean,
    compact: Boolean,
) {
    val seconds = ceil(remainingTimeMs / 1000.0).toInt()
    val timerDescription = stringResource(R.string.quiz_timer_accessibility, seconds)
    Box(
        modifier = Modifier
            .size(if (compact) 42.dp else 48.dp)
            .semantics {
                contentDescription = timerDescription
                if (isFrozen) stateDescription = "Paused"
            },
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = ImperiumQuizColors.SurfaceHighest,
            strokeWidth = 3.dp,
        )
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = color,
            strokeWidth = 3.dp,
        )
        Text(
            text = romanNumeral(seconds),
            color = color,
            style = ImperiumQuizTypography.Label.copy(letterSpacing = 0.sp),
        )
    }
}

@Composable
internal fun ComboProgressCard(
    session: QuizSession,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val streak = session.responses.asReversed().takeWhile { it.isCorrect }.size
    Surface(
        modifier = modifier,
        color = ImperiumQuizColors.SurfaceLow,
        shape = TabletShape,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = if (compact) 6.dp else 9.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = ImperiumQuizColors.Gold,
                modifier = Modifier.size(if (compact) 18.dp else 21.dp),
            )
            Column(Modifier.padding(start = 6.dp)) {
                Text(
                    text = if (streak > 0) {
                        stringResource(R.string.quiz_combo, romanNumeral(streak))
                    } else {
                        stringResource(R.string.quiz_progress_label)
                    },
                    color = ImperiumQuizColors.Gold,
                    style = ImperiumQuizTypography.Label,
                )
                Text(
                    text = stringResource(
                        R.string.quiz_score_progress,
                        session.correctAnswers,
                        session.questions.size,
                    ),
                    color = ImperiumQuizColors.BurgundyLight,
                    style = ImperiumQuizTypography.Label.copy(fontSize = 9.sp),
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(session.questions.size) { index ->
                    val dotColor = when {
                        index < session.currentQuestionIndex -> ImperiumQuizColors.Gold
                        index == session.currentQuestionIndex -> ImperiumQuizColors.BurgundyLight
                        else -> ImperiumQuizColors.SurfaceHighest
                    }
                    Box(
                        Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (index == session.currentQuestionIndex) 12.dp else 8.dp)
                            .background(dotColor, CircleShape),
                    )
                }
            }
            Icon(
                Icons.Default.Shield,
                contentDescription = null,
                tint = ImperiumQuizColors.OnSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = "${session.currentQuestionIndex + 1}/${session.questions.size}",
                color = ImperiumQuizColors.OnSurfaceVariant,
                style = ImperiumQuizTypography.Label.copy(letterSpacing = 0.sp),
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Composable
internal fun HistoricalQuestionCard(
    category: HistoryCategory,
    levelTitle: String,
    question: QuizQuestion,
    compact: Boolean,
) {
    val illustration = category.visualAssets().illustrationResId
    Surface(
        color = ImperiumQuizColors.Surface,
        shape = TabletShape,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
        modifier = Modifier
            .fillMaxWidth()
            .goldCornerBrackets(),
    ) {
        Column(
            modifier = Modifier.padding(if (compact) 10.dp else 16.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 10.dp),
        ) {
            Surface(
                color = ImperiumQuizColors.Burgundy,
                shape = TileShape,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = category.id.categoryIcon(),
                        contentDescription = null,
                        tint = ImperiumQuizColors.BurgundyLight,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = category.title.uppercase(),
                        color = ImperiumQuizColors.BurgundyLight,
                        style = ImperiumQuizTypography.Label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (compact) 58.dp else 126.dp)
                    .clip(TabletShape)
                    .background(ImperiumQuizColors.SurfaceLow),
            ) {
                if (illustration != null) {
                    val grayscale = remember {
                        ColorMatrix().apply { setToSaturation(0f) }
                    }
                    Image(
                        painter = painterResource(illustration),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(grayscale),
                        alpha = 0.72f,
                    )
                } else {
                    CategoryArtworkFallback(category.id.categoryIcon())
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    ImperiumQuizColors.Surface.copy(alpha = 0.30f),
                                    ImperiumQuizColors.Surface,
                                ),
                            ),
                        ),
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Default.HistoryEdu,
                        contentDescription = null,
                        tint = ImperiumQuizColors.Gold,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = levelTitle.uppercase(),
                        color = ImperiumQuizColors.Gold,
                        style = ImperiumQuizTypography.Label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 5.dp),
                    )
                }
            }
            Text(
                text = question.text,
                color = ImperiumQuizColors.OnSurface,
                style = if (compact) {
                    ImperiumQuizTypography.Question.copy(fontSize = 16.sp, lineHeight = 21.sp)
                } else {
                    ImperiumQuizTypography.Question
                },
            )
        }
    }
}

@Composable
private fun CategoryArtworkFallback(icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        ImperiumQuizColors.SurfaceHigh,
                        ImperiumQuizColors.Burgundy.copy(alpha = 0.52f),
                        ImperiumQuizColors.SurfaceLowest,
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val lineColor = ImperiumQuizColors.Gold.copy(alpha = 0.08f)
            val step = size.width / 7f
            repeat(9) { index ->
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(index * step, 0f),
                    end = androidx.compose.ui.geometry.Offset((index - 3) * step, size.height),
                    strokeWidth = 1.dp.toPx(),
                )
            }
        }
        Icon(
            icon,
            contentDescription = null,
            tint = ImperiumQuizColors.Gold.copy(alpha = 0.34f),
            modifier = Modifier.size(62.dp),
        )
    }
}

@Composable
internal fun AnswerOptionTile(
    text: String,
    index: Int,
    visualState: QuizAnswerVisualState,
    enabled: Boolean,
    compact: Boolean,
    onClick: () -> Unit,
) {
    val containerTarget = when (visualState) {
        QuizAnswerVisualState.Correct -> ImperiumQuizColors.CorrectSurface
        QuizAnswerVisualState.Incorrect -> ImperiumQuizColors.ErrorSurface
        QuizAnswerVisualState.Selected -> ImperiumQuizColors.SurfaceHighest
        QuizAnswerVisualState.Default,
        QuizAnswerVisualState.Dimmed,
        -> ImperiumQuizColors.SurfaceHigh
    }
    val borderTarget = when (visualState) {
        QuizAnswerVisualState.Correct -> ImperiumQuizColors.Correct
        QuizAnswerVisualState.Incorrect -> ImperiumQuizColors.Error
        QuizAnswerVisualState.Selected -> ImperiumQuizColors.Gold
        QuizAnswerVisualState.Default,
        QuizAnswerVisualState.Dimmed,
        -> ImperiumQuizColors.Outline
    }
    val container by animateColorAsState(containerTarget, tween(ImperiumMotion.Standard), label = "answer-bg")
    val border by animateColorAsState(borderTarget, tween(ImperiumMotion.Standard), label = "answer-border")
    val opacity by animateFloatAsState(
        targetValue = if (visualState == QuizAnswerVisualState.Dimmed) 0.48f else 1f,
        animationSpec = tween(ImperiumMotion.Standard),
        label = "answer-opacity",
    )
    val shake = remember { Animatable(0f) }
    LaunchedEffect(visualState) {
        if (visualState == QuizAnswerVisualState.Incorrect) {
            shake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = ImperiumMotion.Emphasis
                    -10f at 70
                    8f at 150
                    -5f at 230
                    3f at 310
                    0f at ImperiumMotion.Emphasis
                },
            )
        }
    }
    val optionStateDescription = when (visualState) {
        QuizAnswerVisualState.Correct -> stringResource(R.string.quiz_option_correct)
        QuizAnswerVisualState.Incorrect -> stringResource(R.string.quiz_option_incorrect)
        QuizAnswerVisualState.Selected -> stringResource(R.string.quiz_option_selected)
        else -> null
    }

    Surface(
        color = container,
        shape = TileShape,
        shadowElevation = if (visualState == QuizAnswerVisualState.Selected) 1.dp else 4.dp,
        border = BorderStroke(1.dp, border),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationX = shake.value
                alpha = opacity
            }
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics {
                optionStateDescription?.let { stateDescription = it }
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = if (compact) 8.dp else 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(if (compact) 28.dp else 32.dp),
                shape = TileShape,
                color = when (visualState) {
                    QuizAnswerVisualState.Correct -> ImperiumQuizColors.Correct
                    QuizAnswerVisualState.Incorrect -> ImperiumQuizColors.Error
                    QuizAnswerVisualState.Selected -> ImperiumQuizColors.GoldDark
                    else -> ImperiumQuizColors.SurfaceHighest
                },
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = ('A'.code + index).toChar().toString(),
                        color = when (visualState) {
                            QuizAnswerVisualState.Incorrect -> Color(0xFF690005)
                            QuizAnswerVisualState.Correct -> Color.White
                            QuizAnswerVisualState.Selected -> ImperiumQuizColors.GoldLight
                            else -> ImperiumQuizColors.OnSurfaceVariant
                        },
                        style = if (compact) {
                            ImperiumQuizTypography.Answer.copy(fontSize = 14.sp)
                        } else {
                            ImperiumQuizTypography.Answer
                        },
                    )
                }
            }
            Text(
                text = text,
                color = when (visualState) {
                    QuizAnswerVisualState.Correct -> Color(0xFFD6F5DD)
                    QuizAnswerVisualState.Incorrect -> Color(0xFFFFDAD6)
                    else -> ImperiumQuizColors.OnSurface
                },
                style = if (compact) {
                    ImperiumQuizTypography.Answer.copy(fontSize = 15.sp, lineHeight = 19.sp)
                } else {
                    ImperiumQuizTypography.Answer
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            )
            when (visualState) {
                QuizAnswerVisualState.Correct -> Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF8DDBA0),
                )
                QuizAnswerVisualState.Incorrect -> Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = ImperiumQuizColors.Error,
                )
                else -> Unit
            }
        }
    }
}

@Composable
internal fun QuizLifelineBar(compact: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { disabled() }
            .alpha(0.56f),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        LifelineItem(
            icon = Icons.Default.HourglassTop,
            label = stringResource(R.string.quiz_lifeline_mora),
            compact = compact,
        )
        LifelineItem(
            icon = Icons.Default.FilterAlt,
            label = stringResource(R.string.quiz_lifeline_fifty),
            compact = compact,
        )
        LifelineItem(
            icon = Icons.AutoMirrored.Filled.MenuBook,
            label = stringResource(R.string.quiz_lifeline_oraculum),
            compact = compact,
        )
    }
}

@Composable
private fun LifelineItem(
    icon: ImageVector,
    label: String,
    compact: Boolean,
) {
    Column(
        modifier = Modifier.widthIn(min = 88.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Surface(
            modifier = Modifier.size(if (compact) 34.dp else 40.dp),
            shape = CircleShape,
            color = ImperiumQuizColors.Surface,
            border = BorderStroke(1.dp, ImperiumQuizColors.Outline),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = ImperiumQuizColors.OnSurfaceVariant,
                    modifier = Modifier.size(if (compact) 16.dp else 19.dp),
                )
            }
        }
        Text(
            text = label,
            color = ImperiumQuizColors.OnSurfaceVariant,
            style = ImperiumQuizTypography.Label.copy(fontSize = 9.sp),
            maxLines = 1,
        )
    }
}

@Composable
internal fun QuizActionBar(
    session: QuizSession,
    isSaving: Boolean,
    onNext: () -> Unit,
) {
    val feedback = quizFeedbackKind(
        selectedIndex = session.selectedAnswerIndex,
        correctIndex = session.currentQuestion.correctAnswerIndex,
        timedOut = session.isCurrentQuestionTimedOut,
    )
    Surface(
        color = ImperiumQuizColors.SurfaceLowest.copy(alpha = 0.98f),
        shadowElevation = 12.dp,
        border = BorderStroke(1.dp, ImperiumQuizColors.Outline.copy(alpha = 0.55f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (feedback != null) {
                val correctAnswer = session.currentQuestion.options[session.currentQuestion.correctAnswerIndex]
                val feedbackColor = when (feedback) {
                    QuizFeedbackKind.Correct -> Color(0xFF8DDBA0)
                    QuizFeedbackKind.Incorrect,
                    QuizFeedbackKind.TimeUp,
                    -> ImperiumQuizColors.Error
                }
                Row(
                    modifier = Modifier
                        .widthIn(max = 568.dp)
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = when (feedback) {
                            QuizFeedbackKind.Correct -> Icons.Default.Check
                            QuizFeedbackKind.Incorrect -> Icons.Default.Close
                            QuizFeedbackKind.TimeUp -> Icons.Default.AccessTime
                        },
                        contentDescription = null,
                        tint = feedbackColor,
                        modifier = Modifier.size(17.dp),
                    )
                    Text(
                        text = when (feedback) {
                            QuizFeedbackKind.Correct -> stringResource(R.string.quiz_answer_correct)
                            QuizFeedbackKind.Incorrect -> stringResource(
                                R.string.quiz_feedback_wrong_answer,
                                correctAnswer,
                            )
                            QuizFeedbackKind.TimeUp -> stringResource(
                                R.string.quiz_feedback_time_up,
                                correctAnswer,
                            )
                        }.uppercase(),
                        color = feedbackColor,
                        style = ImperiumQuizTypography.Label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
            Button(
                onClick = onNext,
                enabled = session.isAnswerRevealed && !isSaving,
                modifier = Modifier
                    .widthIn(max = 568.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = TileShape,
                border = BorderStroke(1.dp, ImperiumQuizColors.Gold.copy(alpha = 0.55f)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImperiumQuizColors.Burgundy,
                    contentColor = ImperiumQuizColors.GoldLight,
                    disabledContainerColor = ImperiumQuizColors.SurfaceHigh,
                    disabledContentColor = ImperiumQuizColors.Muted,
                ),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = ImperiumQuizColors.Gold,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = stringResource(
                            if (session.isLastQuestion) R.string.quiz_finish else R.string.quiz_next_question,
                        ).uppercase(),
                        style = ImperiumQuizTypography.Label.copy(fontSize = 13.sp),
                    )
                }
            }
        }
    }
}

private fun Modifier.goldCornerBrackets(): Modifier = drawBehind {
    val color = ImperiumQuizColors.Gold.copy(alpha = 0.30f)
    val inset = 9.dp.toPx()
    val arm = 11.dp.toPx()
    val stroke = 1.dp.toPx()
    drawLine(color, androidx.compose.ui.geometry.Offset(inset, inset), androidx.compose.ui.geometry.Offset(inset + arm, inset), stroke)
    drawLine(color, androidx.compose.ui.geometry.Offset(inset, inset), androidx.compose.ui.geometry.Offset(inset, inset + arm), stroke)
    drawLine(color, androidx.compose.ui.geometry.Offset(size.width - inset, inset), androidx.compose.ui.geometry.Offset(size.width - inset - arm, inset), stroke)
    drawLine(color, androidx.compose.ui.geometry.Offset(size.width - inset, inset), androidx.compose.ui.geometry.Offset(size.width - inset, inset + arm), stroke)
    drawLine(color, androidx.compose.ui.geometry.Offset(inset, size.height - inset), androidx.compose.ui.geometry.Offset(inset + arm, size.height - inset), stroke)
    drawLine(color, androidx.compose.ui.geometry.Offset(inset, size.height - inset), androidx.compose.ui.geometry.Offset(inset, size.height - inset - arm), stroke)
    drawLine(color, androidx.compose.ui.geometry.Offset(size.width - inset, size.height - inset), androidx.compose.ui.geometry.Offset(size.width - inset - arm, size.height - inset), stroke)
    drawLine(color, androidx.compose.ui.geometry.Offset(size.width - inset, size.height - inset), androidx.compose.ui.geometry.Offset(size.width - inset, size.height - inset - arm), stroke)
}
