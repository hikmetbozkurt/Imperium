package com.hikmet.imperium.feature.results

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ResultScreen(
    onRetry: (categoryId: String, levelNumber: Int) -> Unit,
    onLevels: (categoryId: String) -> Unit,
    onHome: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val result = state.result

    Scaffold { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null || result == null -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.error ?: "Result could not be found", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onHome) { Text("Home") }
                }
                else -> Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val start = Color(state.category?.gradientStartColor ?: 0xFF8C4A00)
                    val end = Color(state.category?.gradientEndColor ?: 0xFF5F4C24)
                    Box(
                        modifier = Modifier
                            .size(132.dp)
                            .background(Brush.linearGradient(listOf(start, end)), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "${result.score}%",
                            color = Color.White,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = if (result.stars > 0) "Level complete" else "Keep learning",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = state.category?.title.orEmpty() + " · Level ${result.levelNumber}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(Modifier.padding(vertical = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(3) { index ->
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < result.stars) Color(0xFFFFA000) else MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Metric("Correct", "${result.correctAnswers}/${result.totalQuestions}")
                            Metric("Time", "${result.durationMs / 1_000}s")
                            Metric("Stars", result.stars.toString())
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { onLevels(result.categoryId.value) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        Text("Continue")
                    }
                    OutlinedButton(
                        onClick = { onRetry(result.categoryId.value, result.levelNumber) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Text("  Try again")
                    }
                    OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Home, contentDescription = null)
                        Text("  Home")
                    }
                }
            }
        }
    }
}

@Composable
private fun Metric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}
