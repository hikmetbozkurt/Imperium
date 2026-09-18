package com.hikmet.imperium.ui.home

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.repository.CategoryProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomePresentationTest {
    @Test
    fun `current expedition uses the most recently played category`() {
        val ancient = homeCategory(
            CategoryId.ANCIENT,
            unlockedLevels = 9,
            totalStars = 20,
            lastPlayedTimestamp = 100,
        )
        val medieval = homeCategory(
            CategoryId.MEDIEVAL,
            unlockedLevels = 2,
            totalStars = 3,
            lastPlayedTimestamp = 200,
        )

        assertEquals(medieval, selectCurrentExpedition(listOf(ancient, medieval)))
    }

    @Test
    fun `current expedition keeps repository order for a new player`() {
        val ancient = homeCategory(CategoryId.ANCIENT, unlockedLevels = 1, totalStars = 0)
        val medieval = homeCategory(CategoryId.MEDIEVAL, unlockedLevels = 1, totalStars = 0)

        assertEquals(ancient, selectCurrentExpedition(listOf(ancient, medieval)))
    }

    @Test
    fun `current expedition is absent when no category exists`() {
        assertNull(selectCurrentExpedition(emptyList()))
    }

    @Test
    fun `expedition summary is derived from real category content and progress`() {
        val ancient = homeCategory(
            CategoryId.ANCIENT,
            unlockedLevels = 3,
            totalStars = 8,
            levelCount = 20,
        )
        val medieval = homeCategory(
            CategoryId.MEDIEVAL,
            unlockedLevels = 1,
            totalStars = 2,
            levelCount = 20,
        )

        assertEquals(
            ExpeditionSummary(categoryCount = 2, levelCount = 40, totalStars = 10),
            summarizeExpeditions(listOf(ancient, medieval)),
        )
    }

    private fun homeCategory(
        id: CategoryId,
        unlockedLevels: Int,
        totalStars: Int,
        levelCount: Int = 0,
        lastPlayedTimestamp: Long = 0,
    ) = HomeCategory(
        content = HistoryCategory(
            id = id,
            title = id.value,
            description = "description",
            longDescription = "long description",
            iconResourceName = "",
            gradientStartColor = 0,
            gradientEndColor = 0,
            levels = List(levelCount) { index ->
                com.hikmet.imperium.domain.model.HistoryLevel(
                    number = index + 1,
                    title = "Level ${index + 1}",
                    description = "description",
                    iconResId = 0,
                    requiredStars = 0,
                )
            },
        ),
        progress = CategoryProgress(
            categoryId = id,
            unlockedLevels = unlockedLevels,
            totalStars = totalStars,
            levelStars = emptyMap(),
            lastPlayedTimestamp = lastPlayedTimestamp,
        ),
    )
}
