package com.hikmet.imperium.feature.levels

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hikmet.imperium.domain.model.HistoryLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelsScreen(
    onBack: () -> Unit,
    onLevelSelected: (Int) -> Unit,
    viewModel: LevelsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val category = state.category

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category?.title ?: "Levels") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when {
            state.error != null -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error)
            }
            category == null -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(category.gradientStartColor), Color(category.gradientEndColor)),
                                ),
                                shape = RoundedCornerShape(24.dp),
                            )
                            .padding(20.dp),
                    ) {
                        Text(category.description, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                            Text(
                                text = " ${state.progress?.totalStars ?: 0} stars earned",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
                items(category.levels, key = HistoryLevel::number) { level ->
                    val unlocked = level.number <= (state.progress?.unlockedLevels ?: 1)
                    LevelCard(
                        level = level,
                        stars = state.progress?.levelStars?.get(level.number) ?: 0,
                        unlocked = unlocked,
                        onClick = { if (unlocked) onLevelSelected(level.number) },
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelCard(
    level: HistoryLevel,
    stars: Int,
    unlocked: Boolean,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        enabled = unlocked,
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(level.number.toString(), fontWeight = FontWeight.Bold)
            }
            Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(level.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(level.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                if (stars > 0) {
                    Text("★".repeat(stars), color = Color(0xFFFFA000), style = MaterialTheme.typography.titleMedium)
                }
            }
            Icon(
                imageVector = if (unlocked) Icons.Default.PlayArrow else Icons.Default.Lock,
                contentDescription = null,
                tint = if (unlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            )
        }
    }
}
