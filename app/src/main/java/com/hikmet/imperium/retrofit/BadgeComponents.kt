package com.hikmet.imperium.retrofit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialPanelShape
import com.hikmet.imperium.ui.components.ImperialTileShape
import com.hikmet.imperium.ui.components.ImperialTypography

@Composable
fun BadgeSection(
    badgeState: ApiResult<List<Badge>>,
    userStars: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.profile_honors).uppercase(),
            color = ImperialColors.Gold,
            style = ImperialTypography.Label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        when (badgeState) {
            is ApiResult.Loading -> BadgeLoadingState()
            is ApiResult.Success -> BadgeList(badgeState.data, userStars)
            is ApiResult.Error -> BadgeErrorState(badgeState.message)
        }
    }
}

@Composable
private fun BadgeLoadingState() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
        border = BorderStroke(1.dp, ImperialColors.Outline),
    ) {
        Box(Modifier.fillMaxWidth().padding(28.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = ImperialColors.Gold, modifier = Modifier.size(34.dp))
        }
    }
}

@Composable
private fun BadgeErrorState(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = ImperialPanelShape,
        colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
        border = BorderStroke(1.dp, ImperialColors.Error.copy(alpha = 0.45f)),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(R.string.profile_badges_failed),
                color = ImperialColors.Error,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                message,
                color = ImperialColors.Muted,
                style = ImperialTypography.Body,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun BadgeList(badges: List<Badge>, userStars: Int) {
    if (badges.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = ImperialPanelShape,
            colors = CardDefaults.cardColors(containerColor = ImperialColors.SurfaceHigh),
            border = BorderStroke(1.dp, ImperialColors.Outline),
        ) {
            Text(
                stringResource(R.string.profile_no_honors),
                color = ImperialColors.OnSurfaceVariant,
                style = ImperialTypography.Body,
                modifier = Modifier.padding(18.dp),
            )
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(badges, key = { "${it.title}:${it.minStars}" }) { badge ->
            BadgeItem(badge, badge.isEarned(userStars), userStars)
        }
    }
}

@Composable
private fun BadgeItem(badge: Badge, isEarned: Boolean, userStars: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().alpha(if (isEarned) 1f else 0.70f),
        shape = ImperialTileShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned) ImperialColors.Burgundy else ImperialColors.SurfaceHigh,
        ),
        border = BorderStroke(
            1.dp,
            if (isEarned) ImperialColors.Gold.copy(alpha = 0.6f) else ImperialColors.Outline,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BadgeIcon(isEarned, if (isEarned) 1f else badge.getProgress(userStars))
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    badge.title,
                    color = if (isEarned) ImperialColors.GoldLight else ImperialColors.OnSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    badge.description,
                    color = ImperialColors.OnSurfaceVariant,
                    style = ImperialTypography.Body,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!isEarned) {
                    val remaining = (badge.minStars - userStars).coerceAtLeast(0)
                    Text(
                        pluralStringResource(R.plurals.profile_more_stars_needed, remaining, remaining),
                        color = ImperialColors.Muted,
                        style = ImperialTypography.Label,
                        modifier = Modifier.padding(top = 5.dp),
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = ImperialColors.Gold, modifier = Modifier.size(15.dp))
                Text(
                    badge.minStars.toString(),
                    color = ImperialColors.Gold,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 3.dp),
                )
            }
        }
    }
}

@Composable
private fun BadgeIcon(isEarned: Boolean, progress: Float) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(52.dp)) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = if (isEarned) ImperialColors.GoldDark else ImperialColors.SurfaceLowest,
            border = BorderStroke(1.dp, ImperialColors.Outline),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    if (isEarned) Icons.Default.Star else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isEarned) ImperialColors.GoldLight else ImperialColors.Muted,
                    modifier = Modifier.size(if (isEarned) 25.dp else 20.dp),
                )
            }
        }
        if (!isEarned && progress > 0f) {
            Box(
                Modifier.size(52.dp).border(
                    2.dp,
                    ImperialColors.Gold.copy(alpha = progress.coerceIn(0.15f, 1f)),
                    CircleShape,
                ),
            )
        }
    }
}
