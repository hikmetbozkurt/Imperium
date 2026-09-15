package com.hikmet.imperium.data.repository

import com.hikmet.imperium.data.AncientLevels
import com.hikmet.imperium.data.AncientQuizData
import com.hikmet.imperium.data.MedievalLevels
import com.hikmet.imperium.data.MedievalQuizData
import com.hikmet.imperium.data.ModernHistoryLevels
import com.hikmet.imperium.data.ModernHistoryQuizData
import com.hikmet.imperium.data.Question
import com.hikmet.imperium.data.RenaissanceLevels
import com.hikmet.imperium.data.RenaissanceQuizData
import com.hikmet.imperium.data.WorldWarsLevels
import com.hikmet.imperium.data.WorldWarsQuizData
import com.hikmet.imperium.data.content.AncientSupplementalQuestions
import com.hikmet.imperium.data.content.MedievalSupplementalQuestions
import com.hikmet.imperium.data.content.ModernSupplementalQuestions
import com.hikmet.imperium.data.content.RenaissanceSupplementalQuestions
import com.hikmet.imperium.data.content.WorldWarsSupplementalQuestions
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.model.HistoryLevel
import com.hikmet.imperium.domain.model.QuizQuestion
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import java.nio.charset.StandardCharsets
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/** Single source of truth for immutable game content. */
@Singleton
class LocalHistoryContentRepository @Inject constructor() : HistoryContentRepository {
    private val catalog: Map<CategoryId, HistoryCategory> by lazy {
        listOf(
            category(
                id = CategoryId.ANCIENT,
                title = "Ancient Civilizations",
                description = "Explore the civilizations that shaped the ancient world.",
                longDescription = "Travel from Egypt and Greece to Rome and discover the foundations of the modern world.",
                icon = "ic_ancient",
                colors = 0xFF227C70 to 0xFF1E5631,
                levels = AncientLevels.levels.map { HistoryLevel(it.id.toInt(), it.title, it.description, it.imageResId, it.requiredStars) },
            ),
            category(
                id = CategoryId.MEDIEVAL,
                title = "Medieval Period",
                description = "Discover the Middle Ages across Europe and Asia.",
                longDescription = "Explore kingdoms, belief, trade, art and conflict throughout the medieval world.",
                icon = "ic_medieval",
                colors = 0xFF4B0082 to 0xFF26428B,
                levels = MedievalLevels.levels.map { HistoryLevel(it.id.toInt(), it.title, it.description, it.imageResId, it.requiredStars) },
            ),
            category(
                id = CategoryId.RENAISSANCE,
                title = "Renaissance",
                description = "Experience a rebirth of art, culture and learning.",
                longDescription = "Meet the artists, scientists and thinkers who transformed Europe and beyond.",
                icon = "ic_renaissance",
                colors = 0xFF8B0000 to 0xFFFF4500,
                levels = RenaissanceLevels.levels.map { HistoryLevel(it.id.toInt(), it.title, it.description, it.imageResId, it.requiredStars) },
            ),
            category(
                id = CategoryId.MODERN,
                title = "Modern History",
                description = "Follow the industrial age into the modern world.",
                longDescription = "Understand the revolutions, inventions and social changes behind today's world.",
                icon = "ic_modern",
                colors = 0xFF4682B4 to 0xFF87CEEB,
                levels = ModernHistoryLevels.levels.map { HistoryLevel(it.id.toInt(), it.title, it.description, it.imageResId, it.requiredStars) },
            ),
            category(
                id = CategoryId.WORLD_WARS,
                title = "World Wars",
                description = "Study the defining conflicts of the twentieth century.",
                longDescription = "Explore the causes, events and global consequences of the First and Second World Wars.",
                icon = "ic_wars",
                colors = 0xFF8C3D3D to 0xFF5E2828,
                levels = WorldWarsLevels.levels.map { HistoryLevel(it.id.toInt(), it.title, it.description, it.imageResId, it.requiredStars) },
            ),
        ).associateBy(HistoryCategory::id)
    }

    override fun categories(): List<HistoryCategory> = CategoryId.entries.mapNotNull(catalog::get)

    override fun category(id: CategoryId): HistoryCategory? = catalog[id]

    override fun questions(categoryId: CategoryId, levelNumber: Int): List<QuizQuestion> {
        if (catalog[categoryId]?.levels?.none { it.number == levelNumber } != false) return emptyList()
        return sourceQuestions(categoryId, levelNumber).map { question ->
            question.toDomain(categoryId, levelNumber)
        }
    }

    private fun sourceQuestions(categoryId: CategoryId, levelNumber: Int): List<Question> {
        val original = when (categoryId) {
            CategoryId.ANCIENT -> AncientQuizData.getQuestionsByLevel(levelNumber.toString())
            CategoryId.MEDIEVAL -> MedievalQuizData.getQuestionsByLevel(levelNumber.toString())
            CategoryId.RENAISSANCE -> RenaissanceQuizData.getQuestionsByLevel(levelNumber.toString())
            CategoryId.MODERN -> ModernHistoryQuizData.getQuestionsByLevel(levelNumber.toString())
            CategoryId.WORLD_WARS -> WorldWarsQuizData.getQuestionsByLevel(levelNumber.toString())
        }
        val supplemental = when (categoryId) {
            CategoryId.ANCIENT -> AncientSupplementalQuestions.byLevel[levelNumber]
            CategoryId.MEDIEVAL -> MedievalSupplementalQuestions.byLevel[levelNumber]
            CategoryId.RENAISSANCE -> RenaissanceSupplementalQuestions.byLevel[levelNumber]
            CategoryId.MODERN -> ModernSupplementalQuestions.byLevel[levelNumber]
            CategoryId.WORLD_WARS -> WorldWarsSupplementalQuestions.byLevel[levelNumber]
        }.orEmpty()
        return original + supplemental
    }

    private fun category(
        id: CategoryId,
        title: String,
        description: String,
        longDescription: String,
        icon: String,
        colors: Pair<Long, Long>,
        levels: List<HistoryLevel>,
    ) = HistoryCategory(
        id = id,
        title = title,
        description = description,
        longDescription = longDescription,
        iconResourceName = icon,
        gradientStartColor = colors.first,
        gradientEndColor = colors.second,
        levels = levels.filter { sourceQuestions(id, it.number).isNotEmpty() },
    )

    private fun Question.toDomain(categoryId: CategoryId, levelNumber: Int): QuizQuestion {
        val stableId = id ?: UUID.nameUUIDFromBytes(
            "${categoryId.value}|$levelNumber|$text|${options.joinToString("|")}".toByteArray(StandardCharsets.UTF_8),
        ).toString()
        return QuizQuestion(
            id = stableId,
            text = text,
            options = options,
            correctAnswerIndex = correctAnswerIndex,
            conceptId = conceptId ?: stableId,
            difficulty = difficultyForLevel(levelNumber),
            explanation = explanation,
        )
    }

    /** Five internal difficulty bands: levels 1-4 through levels 17-20. */
    private fun difficultyForLevel(levelNumber: Int): Int = ((levelNumber - 1) / 4 + 1).coerceIn(1, 5)
}
