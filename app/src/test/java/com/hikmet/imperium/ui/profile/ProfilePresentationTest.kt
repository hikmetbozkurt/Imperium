package com.hikmet.imperium.ui.profile

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfilePresentationTest {
    @Test
    fun `rank advances only from earned stars`() {
        assertEquals(ProfileRank.NoviceChronicler, profileRank(0))
        assertEquals(ProfileRank.Archivist, profileRank(20))
        assertEquals(ProfileRank.Historian, profileRank(60))
        assertEquals(ProfileRank.ImperialScholar, profileRank(120))
    }

    @Test
    fun `mastery is bounded for incomplete empty and complete categories`() {
        assertEquals(0f, masteryProgress(completedLevels = 0, totalLevels = 0))
        assertEquals(0.25f, masteryProgress(completedLevels = 5, totalLevels = 20))
        assertEquals(1f, masteryProgress(completedLevels = 24, totalLevels = 20))
    }
}
