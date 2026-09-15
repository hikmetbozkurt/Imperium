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
        CategoryId.entries.forEach { categoryId ->
            assertEquals(20, repository.category(categoryId)?.levels?.size)
        }
        categories.forEach { category ->
            category.levels.forEach { level ->
                assertEquals(8, repository.questions(category.id, level.number).size)
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
        CategoryId.entries.forEach { categoryId ->
            assertTrue(repository.questions(categoryId, 0).isEmpty())
            assertTrue(repository.questions(categoryId, 21).isEmpty())
        }
    }

    @Test
    fun `content pack contains eight hundred valid questions in increasing difficulty bands`() {
        val categories = repository.categories()
        val questions = categories.flatMap { category ->
            category.levels.flatMap { level ->
                val expectedDifficulty = ((level.number - 1) / 4) + 1
                repository.questions(category.id, level.number).onEach { question ->
                    assertEquals(4, question.options.size)
                    assertEquals(4, question.options.distinct().size)
                    assertEquals(expectedDifficulty, question.difficulty)
                    assertTrue(question.text, question.text.endsWith("?"))
                    assertTrue(question.options.all(String::isNotBlank))
                }
            }
        }

        assertEquals(800, questions.size)
        val duplicateTexts = questions
            .groupBy { it.text.trim().lowercase() }
            .filterValues { it.size > 1 }
            .keys
        assertTrue(duplicateTexts.joinToString(separator = "\n"), duplicateTexts.isEmpty())
    }
}
