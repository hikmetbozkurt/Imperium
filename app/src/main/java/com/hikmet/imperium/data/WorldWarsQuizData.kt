package com.hikmet.imperium.data

import com.hikmet.imperium.data.Question

/**
 * Database of World Wars quiz questions
 */
object WorldWarsQuizData {
    // Level 1 questions
    private val level1Questions = listOf(
        Question(
            text = "What event directly triggered World War I?",
            options = listOf(
                "Assassination of Archduke Ferdinand",
                "Sinking of the Lusitania",
                "Zimmermann Telegram",
                "Invasion of Belgium"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which alliance was formed in opposition to the Triple Alliance before WWI?",
            options = listOf(
                "Triple Entente",
                "League of Nations",
                "Axis Powers",
                "United Nations"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which battle marked the first major trench warfare?",
            options = listOf(
                "Battle of the Marne",
                "Battle of Verdun",
                "Battle of the Somme",
                "Battle of Tannenberg"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "World War I officially ended with which treaty?",
            options = listOf(
                "Treaty of Versailles",
                "Treaty of Tordesillas",
                "Treaty of Ghent",
                "Treaty of Paris"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 2 questions
    private val level2Questions = listOf(
        Question(
            text = "What was the primary front of warfare on the European continent?",
            options = listOf(
                "Western Front",
                "Eastern Front",
                "Italian Front",
                "Balkan Front"
            ),
            correctAnswerIndex = 0
        ),
        // TODO: Add 3 more unique questions for level 2
    )

    // Stubs for World Wars levels 3 through 15 questions
    private val level3Questions: List<Question> = emptyList()
    private val level4Questions: List<Question> = emptyList()
    private val level5Questions: List<Question> = emptyList()
    private val level6Questions: List<Question> = emptyList()
    private val level7Questions: List<Question> = emptyList()
    private val level8Questions: List<Question> = emptyList()
    private val level9Questions: List<Question> = emptyList()
    private val level10Questions: List<Question> = emptyList()
    private val level11Questions: List<Question> = emptyList()
    private val level12Questions: List<Question> = emptyList()
    private val level13Questions: List<Question> = emptyList()
    private val level14Questions: List<Question> = emptyList()
    private val level15Questions: List<Question> = emptyList()
    private val level16Questions: List<Question> = emptyList()
    private val level17Questions: List<Question> = emptyList()
    private val level18Questions: List<Question> = emptyList()
    private val level19Questions: List<Question> = emptyList()
    private val level20Questions: List<Question> = emptyList()

    // Map all level questions
    private val questionsByLevel: Map<String, List<Question>> = mapOf(
        "1" to level1Questions,
        "2" to level2Questions,
        "3" to level3Questions,
        "4" to level4Questions,
        "5" to level5Questions,
        "6" to level6Questions,
        "7" to level7Questions,
        "8" to level8Questions,
        "9" to level9Questions,
        "10" to level10Questions,
        "11" to level11Questions,
        "12" to level12Questions,
        "13" to level13Questions,
        "14" to level14Questions,
        "15" to level15Questions,
        "16" to level16Questions,
        "17" to level17Questions,
        "18" to level18Questions,
        "19" to level19Questions,
        "20" to level20Questions
    )

    /**
     * Get questions for a specific World Wars level
     */
    fun getQuestionsByLevel(levelId: String): List<Question> {
        return questionsByLevel[levelId] ?: emptyList()
    }

    /**
     * Fallback random questions across all levels
     */
    fun getRandomQuestions(count: Int = 4): List<Question> {
        val all = questionsByLevel.values.flatten()
        return if (all.size <= count) all else all.shuffled().take(count)
    }
} 