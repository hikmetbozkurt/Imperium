package com.hikmet.imperium.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Data class to hold quote information
 */
data class HistoricalQuote(
    val text: String,
    val author: String,
    val era: String,
    val year: String,
    val category: HistoricalEra,
    val gradientColors: Pair<Color, Color>
)

/**
 * Enum class for historical eras
 */
enum class HistoricalEra {
    ANCIENT,
    MEDIEVAL,
    RENAISSANCE,
    MODERN,
    WORLD_WARS
}

/**
 * Database of historical quotes organized by era
 */
object QuotesDatabase {
    val ancientQuotes = listOf(
        HistoricalQuote(
            "The die is cast.",
            "Julius Caesar",
            "Ancient Rome",
            "49 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "Know thyself.",
            "Temple of Apollo at Delphi",
            "Ancient Greece",
            "c. 600 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "I came, I saw, I conquered.",
            "Julius Caesar",
            "Ancient Rome",
            "47 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "Man is the measure of all things.",
            "Protagoras",
            "Ancient Greece",
            "c. 485 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "The unexamined life is not worth living.",
            "Socrates",
            "Ancient Greece",
            "399 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "Excellence is not an act, but a habit.",
            "Aristotle",
            "Ancient Greece",
            "c. 350 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "Death does not concern us, because as long as we exist, death is not here.",
            "Epicurus",
            "Ancient Greece",
            "c. 300 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "Man is by nature a political animal.",
            "Aristotle",
            "Ancient Greece",
            "c. 350 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "In all things of nature there is something of the marvelous.",
            "Aristotle",
            "Ancient Greece",
            "c. 350 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        ),
        HistoricalQuote(
            "Everything flows.",
            "Heraclitus",
            "Ancient Greece",
            "c. 500 BCE",
            HistoricalEra.ANCIENT,
            Pair(AncientGradientStart, AncientGradientEnd)
        )
    )

    val medievalQuotes = listOf(
        HistoricalQuote(
            "The truth will set you free.",
            "Saint Augustine",
            "Early Medieval",
            "c. 400",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "God wills it!",
            "Pope Urban II",
            "First Crusade",
            "1095",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "I think, therefore I am.",
            "René Descartes",
            "Late Medieval",
            "1637",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "The end justifies the means.",
            "Niccolò Machiavelli",
            "Late Medieval",
            "1532",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "To one who has faith, no explanation is necessary.",
            "Thomas Aquinas",
            "High Medieval",
            "c. 1270",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "Beauty will save the world.",
            "Fyodor Dostoevsky",
            "Medieval Russia",
            "1869",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "The only true wisdom is in knowing you know nothing.",
            "Socrates",
            "Medieval Philosophy",
            "c. 470 BCE",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "Knowledge is power.",
            "Francis Bacon",
            "Late Medieval",
            "1597",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "Time is money.",
            "Benjamin Franklin",
            "Colonial Era",
            "1748",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        ),
        HistoricalQuote(
            "The pen is mightier than the sword.",
            "Edward Bulwer-Lytton",
            "Victorian Era",
            "1839",
            HistoricalEra.MEDIEVAL,
            Pair(MedievalGradientStart, MedievalGradientEnd)
        )
    )

    val renaissanceQuotes = listOf(
        HistoricalQuote(
            "Knowledge is power.",
            "Francis Bacon",
            "Renaissance",
            "1597",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "Simplicity is the ultimate sophistication.",
            "Leonardo da Vinci",
            "Italian Renaissance",
            "c. 1490",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "Art is never finished, only abandoned.",
            "Leonardo da Vinci",
            "Italian Renaissance",
            "c. 1500",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "The measure of intelligence is the ability to change.",
            "Albert Einstein",
            "Modern Era",
            "1879-1955",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "To be, or not to be, that is the question.",
            "William Shakespeare",
            "English Renaissance",
            "1603",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "All the world's a stage.",
            "William Shakespeare",
            "English Renaissance",
            "1599",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "Man is the architect of his own fortune.",
            "Francis Bacon",
            "Renaissance",
            "1625",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "Fortune favors the bold.",
            "Erasmus",
            "Northern Renaissance",
            "1511",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "In praise of folly.",
            "Erasmus",
            "Northern Renaissance",
            "1509",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        ),
        HistoricalQuote(
            "The ends justify the means.",
            "Niccolò Machiavelli",
            "Italian Renaissance",
            "1532",
            HistoricalEra.RENAISSANCE,
            Pair(RenaissanceGradientStart, RenaissanceGradientEnd)
        )
    )

    val modernQuotes = listOf(
        HistoricalQuote(
            "Let them eat cake.",
            "Marie Antoinette",
            "French Revolution",
            "1789",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "I have nothing to offer but blood, toil, tears, and sweat.",
            "Winston Churchill",
            "World War II",
            "1940",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "Ask not what your country can do for you.",
            "John F. Kennedy",
            "Cold War Era",
            "1961",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "I have a dream.",
            "Martin Luther King Jr.",
            "Civil Rights Movement",
            "1963",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "That's one small step for man, one giant leap for mankind.",
            "Neil Armstrong",
            "Space Age",
            "1969",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "Mr. Gorbachev, tear down this wall!",
            "Ronald Reagan",
            "Cold War Era",
            "1987",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "The only thing we have to fear is fear itself.",
            "Franklin D. Roosevelt",
            "Great Depression",
            "1933",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "Never in the field of human conflict was so much owed by so many to so few.",
            "Winston Churchill",
            "World War II",
            "1940",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "The buck stops here.",
            "Harry S. Truman",
            "Post-WWII Era",
            "1945",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        ),
        HistoricalQuote(
            "Ich bin ein Berliner.",
            "John F. Kennedy",
            "Cold War Era",
            "1963",
            HistoricalEra.MODERN,
            Pair(ModernGradientStart, ModernGradientEnd)
        )
    )

    val worldWarsQuotes = listOf(
        HistoricalQuote(
            "We shall fight on the beaches.",
            "Winston Churchill",
            "World War II",
            "1940",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "Never surrender.",
            "Winston Churchill",
            "World War II",
            "1940",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "Lafayette, we are here!",
            "Charles E. Stanton",
            "World War I",
            "1917",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "The war to end all wars.",
            "H. G. Wells",
            "World War I",
            "1914",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "Today we are all Americans.",
            "Le Monde newspaper",
            "September 11",
            "2001",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "I have nothing to offer but blood, toil, tears and sweat.",
            "Winston Churchill",
            "World War II",
            "1940",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "Yesterday, December 7, 1941—a date which will live in infamy.",
            "Franklin D. Roosevelt",
            "World War II",
            "1941",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "The only thing we have to fear is fear itself.",
            "Franklin D. Roosevelt",
            "World War II",
            "1933",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "Never in the field of human conflict was so much owed by so many to so few.",
            "Winston Churchill",
            "World War II",
            "1940",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        ),
        HistoricalQuote(
            "Give me liberty, or give me death!",
            "Patrick Henry",
            "American Revolution",
            "1775",
            HistoricalEra.WORLD_WARS,
            Pair(WorldWarsGradientStart, WorldWarsGradientEnd)
        )
    )

    // Get all quotes
    val allQuotes = ancientQuotes + medievalQuotes + renaissanceQuotes + modernQuotes + worldWarsQuotes

    // Get random quote from specific era
    fun getRandomQuote(era: HistoricalEra? = null): HistoricalQuote {
        return when (era) {
            HistoricalEra.ANCIENT -> ancientQuotes.random()
            HistoricalEra.MEDIEVAL -> medievalQuotes.random()
            HistoricalEra.RENAISSANCE -> renaissanceQuotes.random()
            HistoricalEra.MODERN -> modernQuotes.random()
            HistoricalEra.WORLD_WARS -> worldWarsQuotes.random()
            null -> allQuotes.random()
        }
    }

    // Get quotes by era
    fun getQuotesByEra(era: HistoricalEra): List<HistoricalQuote> {
        return when (era) {
            HistoricalEra.ANCIENT -> ancientQuotes
            HistoricalEra.MEDIEVAL -> medievalQuotes
            HistoricalEra.RENAISSANCE -> renaissanceQuotes
            HistoricalEra.MODERN -> modernQuotes
            HistoricalEra.WORLD_WARS -> worldWarsQuotes
        }
    }
} 