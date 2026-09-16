package com.hikmet.imperium.ui.home

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.repository.CategoryProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomePresentationTest {
    @Test
    fun `current expedition uses category with most unlocked levels`() {
        val ancient = homeCategory(CategoryId.ANCIENT, unlockedLevels = 3, totalStars = 8)
        val medieval = homeCategory(CategoryId.MEDIEVAL, unlockedLevels = 7, totalStars = 11)

        assertEquals(medieval, selectCurrentExpedition(listOf(ancient, medieval)))
    }

    @Test
    fun `current expedition keeps repository order when progress is tied`() {
        val ancient = homeCategory(CategoryId.ANCIENT, unlockedLevels = 4, totalStars = 9)
        val medieval = homeCategory(CategoryId.MEDIEVAL, unlockedLevels = 4, totalStars = 14)

        assertEquals(ancient, selectCurrentExpedition(listOf(ancient, medieval)))
    }

    @Test
    fun `current expedition is absent when no category exists`() {
        assertNull(selectCurrentExpedition(emptyList()))
    }

    private fun homeCategory(
        id: CategoryId,
        unlockedLevels: Int,
        totalStars: Int,
    ) = HomeCategory(
        content = HistoryCategory(
            id = id,
            title = id.value,
            description = "description",
            longDescription = "long description",
            iconResourceName = "",
            gradientStartColor = 0,
            gradientEndColor = 0,
            levels = emptyList(),
        ),
        progress = CategoryProgress(
            categoryId = id,
            unlockedLevels = unlockedLevels,
            totalStars = totalStars,
            levelStars = emptyMap(),
        ),
    )
}
