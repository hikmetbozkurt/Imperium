package com.hikmet.imperium.data.repository

import com.hikmet.imperium.domain.model.CategoryId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalHistoryContentRepositoryTest {
    private val repository = LocalHistoryContentRepository()

    @Test
    fun `catalog exposes only playable levels`() {
        val categories = repository.categories()

        assertEquals(5, categories.size)
        assertEquals(8, repository.category(CategoryId.ANCIENT)?.levels?.size)
        assertEquals(20, repository.category(CategoryId.MEDIEVAL)?.levels?.size)
        assertEquals(20, repository.category(CategoryId.RENAISSANCE)?.levels?.size)
        assertEquals(20, repository.category(CategoryId.MODERN)?.levels?.size)
        assertEquals(20, repository.category(CategoryId.WORLD_WARS)?.levels?.size)
        categories.forEach { category ->
            category.levels.forEach { level ->
                assertTrue(repository.questions(category.id, level.number).isNotEmpty())
            }
        }
    }

    @Test
    fun `question ids are stable and globally unique`() {
        val questions = repository.categories().flatMap { category ->
            category.levels.flatMap { repository.questions(category.id, it.number) }
        }

        assertEquals(questions.size, questions.map { it.id }.distinct().size)
        assertTrue(questions.all { it.correctAnswerIndex in it.options.indices })
    }
}
