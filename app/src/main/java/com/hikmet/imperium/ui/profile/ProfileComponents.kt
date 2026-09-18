package com.hikmet.imperium.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hikmet.imperium.R
import com.hikmet.imperium.ui.components.ImperialColors
import com.hikmet.imperium.ui.components.ImperialTypography

@Composable
internal fun ProfileSubpageHeader(title: String, subtitle: String, onBack: () -> Unit) {
    Surface(
        color = ImperialColors.SurfaceLowest,
        border = BorderStroke(0.5.dp, ImperialColors.Outline),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.common_back),
                    tint = ImperialColors.Gold,
                    modifier = Modifier.size(24.dp),
                )
            }
            androidx.compose.foundation.layout.Column(Modifier.padding(start = 6.dp)) {
                Text(title, color = ImperialColors.OnSurface, style = ImperialTypography.Section)
                Text(subtitle.uppercase(), color = ImperialColors.Gold, style = ImperialTypography.Label)
            }
        }
    }
}
