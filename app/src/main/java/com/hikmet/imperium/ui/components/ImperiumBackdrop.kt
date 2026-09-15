package com.hikmet.imperium.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** A lightweight, asset-free archive surface shared by the game's core screens. */
@Composable
fun ImperiumBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val ink = colors.onBackground
    val accent = colors.primary
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        colors.background,
                        colors.surfaceVariant.copy(alpha = 0.56f),
                        colors.background,
                    ),
                ),
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(accent.copy(alpha = 0.08f), colors.background.copy(alpha = 0f)),
                    center = Offset(1_000f, 0f),
                    radius = 1_200f,
                ),
            ),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val thinStroke = Stroke(width = 1.dp.toPx())
            drawCircle(
                color = accent.copy(alpha = 0.035f),
                radius = size.minDimension * 0.62f,
                center = Offset(size.width * 0.96f, size.height * 0.12f),
                style = thinStroke,
            )
            drawCircle(
                color = accent.copy(alpha = 0.025f),
                radius = size.minDimension * 0.43f,
                center = Offset(size.width * 0.96f, size.height * 0.12f),
                style = thinStroke,
            )
            val lineGap = 88.dp.toPx()
            var y = lineGap
            while (y < size.height) {
                drawLine(
                    color = ink.copy(alpha = 0.018f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx(),
                )
                y += lineGap
            }
        }
        content()
    }
}
