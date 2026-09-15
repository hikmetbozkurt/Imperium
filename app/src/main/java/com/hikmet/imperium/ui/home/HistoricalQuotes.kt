package com.hikmet.imperium.ui.home

import com.hikmet.imperium.domain.model.CategoryId
import kotlin.random.Random

data class HistoricalQuote(
    val text: String,
    val author: String,
    val era: String,
    val year: String,
    val categoryId: CategoryId,
)

/** Immutable local content used by the interactive home feature card. */
object HistoricalQuotes {
    val all: List<HistoricalQuote> = listOf(
        HistoricalQuote(
            text = "The unexamined life is not worth living.",
            author = "Socrates",
            era = "Classical Athens",
            year = "399 BCE",
            categoryId = CategoryId.ANCIENT,
        ),
        HistoricalQuote(
            text = "I came, I saw, I conquered.",
            author = "Julius Caesar",
            era = "Roman Republic",
            year = "47 BCE",
            categoryId = CategoryId.ANCIENT,
        ),
        HistoricalQuote(
            text = "To no one will we sell, to no one deny or delay right or justice.",
            author = "Magna Carta",
            era = "Medieval England",
            year = "1215",
            categoryId = CategoryId.MEDIEVAL,
        ),
        HistoricalQuote(
            text = "Life is so short, the craft so long to learn.",
            author = "Geoffrey Chaucer",
            era = "Late Middle Ages",
            year = "c. 1385",
            categoryId = CategoryId.MEDIEVAL,
        ),
        HistoricalQuote(
            text = "To be, or not to be, that is the question.",
            author = "William Shakespeare",
            era = "English Renaissance",
            year = "c. 1600",
            categoryId = CategoryId.RENAISSANCE,
        ),
        HistoricalQuote(
            text = "Knowledge itself is power.",
            author = "Francis Bacon",
            era = "Renaissance",
            year = "1597",
            categoryId = CategoryId.RENAISSANCE,
        ),
        HistoricalQuote(
            text = "Man is born free, and everywhere he is in chains.",
            author = "Jean-Jacques Rousseau",
            era = "Enlightenment",
            year = "1762",
            categoryId = CategoryId.MODERN,
        ),
        HistoricalQuote(
            text = "Workers of the world, unite!",
            author = "Karl Marx and Friedrich Engels",
            era = "Industrial Age",
            year = "1848",
            categoryId = CategoryId.MODERN,
        ),
        HistoricalQuote(
            text = "We shall fight on the beaches.",
            author = "Winston Churchill",
            era = "World War II",
            year = "1940",
            categoryId = CategoryId.WORLD_WARS,
        ),
        HistoricalQuote(
            text = "A date which will live in infamy.",
            author = "Franklin D. Roosevelt",
            era = "World War II",
            year = "1941",
            categoryId = CategoryId.WORLD_WARS,
        ),
    )

    fun quoteAt(index: Int): HistoricalQuote = all[normalize(index)]

    fun randomIndex(random: Random = Random.Default): Int = random.nextInt(all.size)

    fun nextIndex(currentIndex: Int, random: Random = Random.Default): Int {
        if (all.size == 1) return 0
        val current = normalize(currentIndex)
        val candidate = random.nextInt(all.size - 1)
        return if (candidate >= current) candidate + 1 else candidate
    }

    private fun normalize(index: Int): Int = index.takeIf(all.indices::contains) ?: 0
}
