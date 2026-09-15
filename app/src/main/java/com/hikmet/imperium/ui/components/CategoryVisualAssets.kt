package com.hikmet.imperium.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Palette
import androidx.compose.ui.graphics.vector.ImageVector
import com.hikmet.imperium.R
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory

/** Optional editorial artwork for a framework-independent category model. */
data class CategoryVisualAssets(
    @param:DrawableRes val illustrationResId: Int? = null,
)

fun HistoryCategory.visualAssets(): CategoryVisualAssets {
    val illustration = when (id) {
        CategoryId.ANCIENT -> R.drawable.ancient_page_image
        CategoryId.MEDIEVAL -> R.drawable.medieval_image
        CategoryId.RENAISSANCE,
        CategoryId.MODERN,
        CategoryId.WORLD_WARS,
        -> null
    }

    return CategoryVisualAssets(illustrationResId = illustration)
}

/** One coherent Material icon family for every era-facing surface. */
fun CategoryId.categoryIcon(): ImageVector = when (this) {
    CategoryId.ANCIENT -> Icons.Default.AccountBalance
    CategoryId.MEDIEVAL -> Icons.Default.Castle
    CategoryId.RENAISSANCE -> Icons.Default.Palette
    CategoryId.MODERN -> Icons.Default.Factory
    CategoryId.WORLD_WARS -> Icons.Default.MilitaryTech
}
