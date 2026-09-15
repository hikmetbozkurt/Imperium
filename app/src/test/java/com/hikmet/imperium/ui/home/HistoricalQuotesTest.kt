package com.hikmet.imperium.ui.home

import com.hikmet.imperium.domain.model.CategoryId
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HistoricalQuotesTest {
    @Test
    fun `every category has featured quote content`() {
        assertEquals(CategoryId.entries.toSet(), HistoricalQuotes.all.map { it.categoryId }.toSet())
        assertTrue(
            HistoricalQuotes.all.all { quote ->
                quote.text.isNotBlank() && quote.author.isNotBlank() &&
                    quote.era.isNotBlank() && quote.year.isNotBlank()
            },
        )
    }

    @Test
    fun `next quote never immediately repeats current quote`() {
        val random = Random(42)

        HistoricalQuotes.all.indices.forEach { current ->
            repeat(25) {
                val next = HistoricalQuotes.nextIndex(current, random)
                assertTrue(next in HistoricalQuotes.all.indices)
                assertNotEquals(current, next)
            }
        }
    }
}
