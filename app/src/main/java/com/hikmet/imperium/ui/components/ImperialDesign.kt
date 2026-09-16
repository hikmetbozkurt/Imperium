package com.hikmet.imperium.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hikmet.imperium.R

internal object ImperialColors {
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
    val Error = Color(0xFFFFB4AB)
}

internal object ImperialTypography {
    val Monument = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.6.sp,
    )
    val Section = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.3.sp,
    )
    val Label = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.2.sp,
    )
    val Body = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )
}

internal val ImperialPanelShape = RoundedCornerShape(8.dp)
internal val ImperialTileShape = RoundedCornerShape(4.dp)

internal enum class ImperialDestination { Home, Codex, Profile }

@Composable
internal fun ImperialScreenBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        ImperialColors.Background,
                        ImperialColors.SurfaceLowest,
                    ),
                ),
            )
            .drawBehind {
                val color = ImperialColors.Gold.copy(alpha = 0.025f)
                val step = 36.dp.toPx()
                var x = 0f
                while (x < size.width) {
                    drawLine(color, Offset(x, 0f), Offset(x, size.height), 1f)
                    x += step
                }
            },
    ) { content() }
}

@Composable
internal fun ImperialCoinEmblem(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(42.dp),
        shape = CircleShape,
        color = ImperialColors.SurfaceLowest,
        border = BorderStroke(1.dp, ImperialColors.Gold.copy(alpha = 0.82f)),
        shadowElevation = 5.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize().padding(5.dp)) {
                drawCircle(ImperialColors.Gold.copy(alpha = 0.14f))
                drawCircle(
                    color = ImperialColors.Gold.copy(alpha = 0.65f),
                    radius = size.minDimension * 0.39f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()),
                )
            }
            Icon(
                Icons.Default.AccountBalance,
                contentDescription = null,
                tint = ImperialColors.Gold,
                modifier = Modifier.size(21.dp),
            )
        }
    }
}

@Composable
internal fun ImperialBrandHeader(
    title: String,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ImperialCoinEmblem()
        Column(Modifier.weight(1f).padding(start = 11.dp)) {
            Text(
                text = title.uppercase(),
                color = ImperialColors.GoldLight,
                style = ImperialTypography.Monument,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            subtitle?.let {
                Text(
                    text = it.uppercase(),
                    color = ImperialColors.Muted,
                    style = ImperialTypography.Label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
internal fun ImperialTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = ImperialColors.OnSurface,
                style = ImperialTypography.Section,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.common_back),
                    tint = ImperialColors.Gold,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = ImperialColors.SurfaceLowest),
    )
}

@Composable
internal fun ImperialBottomBar(
    selected: ImperialDestination,
    onHome: () -> Unit,
    onCodex: () -> Unit,
    onProfile: () -> Unit,
) {
    NavigationBar(
        containerColor = ImperialColors.SurfaceLowest,
        tonalElevation = 0.dp,
    ) {
        ImperialNavigationItem(
            label = stringResource(R.string.nav_home),
            icon = Icons.Default.Home,
            selected = selected == ImperialDestination.Home,
            onClick = onHome,
        )
        ImperialNavigationItem(
            label = stringResource(R.string.nav_codex),
            icon = Icons.AutoMirrored.Filled.MenuBook,
            selected = selected == ImperialDestination.Codex,
            onClick = onCodex,
        )
        ImperialNavigationItem(
            label = stringResource(R.string.nav_profile),
            icon = Icons.Default.Person,
            selected = selected == ImperialDestination.Profile,
            onClick = onProfile,
        )
    }
}

@Composable
private fun RowScope.ImperialNavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label, style = ImperialTypography.Label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = ImperialColors.GoldLight,
            selectedTextColor = ImperialColors.GoldLight,
            indicatorColor = ImperialColors.Burgundy,
            unselectedIconColor = ImperialColors.Muted,
            unselectedTextColor = ImperialColors.Muted,
        ),
    )
}

@Composable
internal fun ImperialSectionTitle(
    eyebrow: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(eyebrow.uppercase(), color = ImperialColors.Gold, style = ImperialTypography.Label)
        Text(title, color = ImperialColors.OnSurface, style = ImperialTypography.Section)
    }
}
