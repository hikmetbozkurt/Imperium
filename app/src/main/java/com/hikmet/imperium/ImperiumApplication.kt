package com.hikmet.imperium

import android.app.Application
import android.graphics.Color
import androidx.core.graphics.toColorInt
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.data.entities.AnswerEntity
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.QuestionEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import com.hikmet.imperium.data.repository.QuizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Application class for Imperium that initializes the database and repository
 */
class ImperiumApplication : Application() {
    // Lazy initialized database instance
    val database by lazy { ImperiumDatabase.getDatabase(this) }
    
    // Lazy initialized repository instance
    val repository by lazy { QuizRepository(database) }
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize database with sample data if needed
        CoroutineScope(Dispatchers.IO).launch {
            prepopulateDatabaseIfNeeded()
        }
    }
    
    /**
     * Prepopulate the database with initial data if needed
     */
    private suspend fun prepopulateDatabaseIfNeeded() {
        // Check if categories exist
        val categoryCount = database.categoryDao().getCategoryCount()
        
        if (categoryCount == 0) {
            // Insert categories
            val categories = listOf(
                CategoryEntity(
                    id = "ancient",
                    title = "Ancient Civilizations",
                    description = "Explore the ancient world from Egypt to China",
                    longDescription = "Discover the rich history of the ancient world, from the pyramids of Egypt to the first emperor of China. Learn about the great civilizations that laid the foundations for our modern world.",
                    totalLevels = 20,
                    gradientStartColor = "#227C70".toColorInt(),
                    gradientEndColor = "#1E5631".toColorInt(),
                    iconResourceName = "ic_ancient"
                ),
                CategoryEntity(
                    id = "medieval",
                    title = "Medieval Period",
                    description = "Discover the Middle Ages from Europe to Asia",
                    longDescription = "Journey through the medieval period, a time of knights, castles, and great empires spanning from Europe to the far reaches of Asia.",
                    totalLevels = 20,
                    gradientStartColor = "#4B0082".toColorInt(),
                    gradientEndColor = "#26428B".toColorInt(),
                    iconResourceName = "ic_medieval"
                ),
                CategoryEntity(
                    id = "renaissance",
                    title = "Renaissance",
                    description = "Experience the rebirth of art, science, and culture in Europe",
                    longDescription = "The Renaissance was a period of great cultural, artistic, and scientific advancement in Europe, marking the transition from the Middle Ages to modernity. Explore the works of da Vinci, Michelangelo, and the rise of humanism.",
                    totalLevels = 10,
                    gradientStartColor = "#DAA520".toColorInt(),
                    gradientEndColor = "#B8860B".toColorInt(),
                    iconResourceName = "ic_renaissance"
                ),
                CategoryEntity(
                    id = "modern",
                    title = "Modern History",
                    description = "From the Industrial Revolution to the present day",
                    longDescription = "Modern History covers the dramatic changes in society, technology, and politics from the 18th century to today. Explore revolutions, world wars, and the rise of the digital age.",
                    totalLevels = 10,
                    gradientStartColor = "#4169E1".toColorInt(),
                    gradientEndColor = "#1E90FF".toColorInt(),
                    iconResourceName = "ic_modern"
                ),
                CategoryEntity(
                    id = "world_wars",
                    title = "World Wars",
                    description = "The two major global conflicts of the 20th century",
                    longDescription = "Examine the causes, events, and consequences of World War I and World War II, and their impact on the modern world.",
                    totalLevels = 10,
                    gradientStartColor = "#8B0000".toColorInt(),
                    gradientEndColor = "#DC143C".toColorInt(),
                    iconResourceName = "ic_wars"
                )
                // More categories can be added here
            )
            
            database.categoryDao().insertCategories(categories)
            
            // Set up default user progress for Ancient Civilizations
            database.userProgressDao().insertOrUpdateProgress(
                UserProgressEntity(
                    categoryId = "ancient",
                    unlockedLevels = 1,  // First level unlocked by default
                    totalStarsEarned = 0,
                    highestLevelCompleted = 0,
                    lastPlayedTimestamp = System.currentTimeMillis()
                )
            )
            
            // Create levels for Ancient Civilizations
            val ancientLevels = (1..20).map { levelNumber ->
                LevelEntity(
                    categoryId = "ancient",
                    levelNumber = levelNumber,
                    title = "Level $levelNumber",
                    description = "Ancient Civilizations Level $levelNumber",
                    difficulty = when (levelNumber) {
                        in 1..5 -> 1    // Easy
                        in 6..10 -> 2   // Medium
                        in 11..15 -> 3  // Hard
                        else -> 4       // Expert
                    },
                    requiredStarsToUnlock = when (levelNumber) {
                        1 -> 0          // First level is free
                        in 2..5 -> (levelNumber - 1) * 2  // 2, 4, 6, 8 stars
                        in 6..10 -> 10 + (levelNumber - 5) * 3  // 13, 16, 19, 22, 25 stars
                        in 11..15 -> 25 + (levelNumber - 10) * 4  // 29, 33, 37, 41, 45 stars
                        else -> 45 + (levelNumber - 15) * 5  // 50, 55, 60, 65, 70 stars
                    }
                )
            }
            
            database.levelDao().insertLevels(ancientLevels)
            
            // Insert sample questions and answers for Ancient Civilizations
            insertAncientCivilizationsQuestions()

            // Set up default user progress for Renaissance
            database.userProgressDao().insertOrUpdateProgress(
                UserProgressEntity(
                    categoryId = "renaissance",
                    unlockedLevels = 1,  // First level unlocked by default
                    totalStarsEarned = 0,
                    highestLevelCompleted = 0,
                    lastPlayedTimestamp = System.currentTimeMillis()
                )
            )

            // Create levels for Renaissance
            val renaissanceLevels = (1..20).map { levelNumber ->
                LevelEntity(
                    categoryId = "renaissance",
                    levelNumber = levelNumber,
                    title = com.hikmet.imperium.data.RenaissanceLevels.levels[levelNumber - 1].title,
                    description = com.hikmet.imperium.data.RenaissanceLevels.levels[levelNumber - 1].description,
                    difficulty = when (levelNumber) {
                        in 1..5 -> 1
                        in 6..10 -> 2
                        in 11..15 -> 3
                        else -> 4
                    },
                    requiredStarsToUnlock = com.hikmet.imperium.data.RenaissanceLevels.levels[levelNumber - 1].requiredStars
                )
            }
            database.levelDao().insertLevels(renaissanceLevels)

            // Set up default user progress for Modern History
            database.userProgressDao().insertOrUpdateProgress(
                UserProgressEntity(
                    categoryId = "modern",
                    unlockedLevels = 1,  // First level unlocked by default
                    totalStarsEarned = 0,
                    highestLevelCompleted = 0,
                    lastPlayedTimestamp = System.currentTimeMillis()
                )
            )

            // Create levels for Modern History
            val modernLevels = (1..20).map { levelNumber ->
                LevelEntity(
                    categoryId = "modern",
                    levelNumber = levelNumber,
                    title = com.hikmet.imperium.data.ModernHistoryLevels.levels[levelNumber - 1].title,
                    description = com.hikmet.imperium.data.ModernHistoryLevels.levels[levelNumber - 1].description,
                    difficulty = when (levelNumber) {
                        in 1..5 -> 1
                        in 6..10 -> 2
                        in 11..15 -> 3
                        else -> 4
                    },
                    requiredStarsToUnlock = com.hikmet.imperium.data.ModernHistoryLevels.levels[levelNumber - 1].requiredStars
                )
            }
            database.levelDao().insertLevels(modernLevels)

            // Set up default user progress for World Wars
            database.userProgressDao().insertOrUpdateProgress(
                UserProgressEntity(
                    categoryId = "world_wars",
                    unlockedLevels = 1,  // First level unlocked by default
                    totalStarsEarned = 0,
                    highestLevelCompleted = 0,
                    lastPlayedTimestamp = System.currentTimeMillis()
                )
            )

            // Create levels for World Wars
            val worldWarsLevels = (1..20).map { levelNumber ->
                LevelEntity(
                    categoryId = "world_wars",
                    levelNumber = levelNumber,
                    title = com.hikmet.imperium.data.WorldWarsLevels.levels[levelNumber - 1].title,
                    description = com.hikmet.imperium.data.WorldWarsLevels.levels[levelNumber - 1].description,
                    difficulty = when (levelNumber) {
                        in 1..5 -> 1
                        in 6..10 -> 2
                        in 11..15 -> 3
                        else -> 4
                    },
                    requiredStarsToUnlock = com.hikmet.imperium.data.WorldWarsLevels.levels[levelNumber - 1].requiredStars
                )
            }
            database.levelDao().insertLevels(worldWarsLevels)
        }
    }
    
    /**
     * Insert questions and answers for Ancient Civilizations
     */
    private suspend fun insertAncientCivilizationsQuestions() {
        // Create questions for Ancient Egypt (Level 1-4)
        val egyptQuestions = listOf(
            // Level 1
            QuestionEntity(
                id = "ancient_1_1",
                categoryId = "ancient",
                levelNumber = 1,
                text = "Which ancient Egyptian structure is the oldest of the Seven Wonders of the Ancient World?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_1_2",
                categoryId = "ancient",
                levelNumber = 1,
                text = "Which pharaoh's tomb was discovered nearly intact by Howard Carter in 1922?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_1_3",
                categoryId = "ancient",
                levelNumber = 1,
                text = "What is the name of the ancient Egyptian writing system?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_1_4",
                categoryId = "ancient",
                levelNumber = 1,
                text = "Which river was central to ancient Egyptian civilization?",
                difficulty = 1
            ),
            
            // Level 2
            QuestionEntity(
                id = "ancient_2_1",
                categoryId = "ancient",
                levelNumber = 2,
                text = "Who was the Egyptian queen who aligned herself with Julius Caesar and Mark Antony?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_2_2",
                categoryId = "ancient",
                levelNumber = 2,
                text = "What was the main purpose of the Egyptian pyramids?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_2_3",
                categoryId = "ancient",
                levelNumber = 2,
                text = "What material did ancient Egyptians use to write on?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_2_4",
                categoryId = "ancient",
                levelNumber = 2,
                text = "Which pharaoh is known for establishing the first monotheistic religion in Egypt?",
                difficulty = 1
            ),
            
            // Level 3
            QuestionEntity(
                id = "ancient_3_1",
                categoryId = "ancient",
                levelNumber = 3,
                text = "What is the name of the legendary half-lion, half-human statue near the Pyramids of Giza?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_3_2",
                categoryId = "ancient",
                levelNumber = 3,
                text = "Which ancient Egyptian god is associated with the afterlife and resurrection?",
                difficulty = 2
            ),
            QuestionEntity(
                id = "ancient_3_3",
                categoryId = "ancient",
                levelNumber = 3,
                text = "What was the name of the process used by ancient Egyptians to preserve bodies?",
                difficulty = 1
            ),
            QuestionEntity(
                id = "ancient_3_4",
                categoryId = "ancient",
                levelNumber = 3,
                text = "Which female pharaoh ruled during Egypt's 18th dynasty and established trading networks?",
                difficulty = 2
            ),
            
            // Level 4
            QuestionEntity(
                id = "ancient_4_1",
                categoryId = "ancient",
                levelNumber = 4,
                text = "What stone, found in 1799, helped scholars decipher Egyptian hieroglyphics?",
                difficulty = 2
            ),
            QuestionEntity(
                id = "ancient_4_2",
                categoryId = "ancient",
                levelNumber = 4,
                text = "What was the ancient Egyptian name for their country?",
                difficulty = 2
            ),
            QuestionEntity(
                id = "ancient_4_3",
                categoryId = "ancient",
                levelNumber = 4,
                text = "Which pharaoh commissioned the construction of the temple at Abu Simbel?",
                difficulty = 2
            ),
            QuestionEntity(
                id = "ancient_4_4",
                categoryId = "ancient",
                levelNumber = 4,
                text = "In ancient Egyptian religion, what was the feather of Ma'at used for?",
                difficulty = 2
            ),
            
            // Level 5
            QuestionEntity(
                id = "ancient_5_1",
                categoryId = "ancient",
                levelNumber = 5,
                text = "Which ancient civilization built the city of Machu Picchu?",
                difficulty = 2
            ),
            QuestionEntity(
                id = "ancient_5_2",
                categoryId = "ancient",
                levelNumber = 5,
                text = "What was the main language of ancient Rome?",
                difficulty = 2
            ),
            QuestionEntity(
                id = "ancient_5_3",
                categoryId = "ancient",
                levelNumber = 5,
                text = "Which ancient Greek philosopher was the teacher of Alexander the Great?",
                difficulty = 2
            ),
            QuestionEntity(
                id = "ancient_5_4",
                categoryId = "ancient",
                levelNumber = 5,
                text = "What ancient civilization is credited with inventing the concept of zero?",
                difficulty = 2
            )
        )
        
        // Insert questions
        database.questionDao().insertQuestions(egyptQuestions)
        
        // Create answers for these questions
        val egyptAnswers = listOf(
            // Level 1, Question 1
            AnswerEntity(questionId = "ancient_1_1", text = "Great Pyramid of Giza", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_1_1", text = "Hanging Gardens of Babylon", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_1_1", text = "Lighthouse of Alexandria", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_1_1", text = "Colossus of Rhodes", isCorrect = false, sortOrder = 3),
            
            // Level 1, Question 2
            AnswerEntity(questionId = "ancient_1_2", text = "Tutankhamun", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_1_2", text = "Ramses II", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_1_2", text = "Cleopatra", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_1_2", text = "Khufu", isCorrect = false, sortOrder = 3),
            
            // Level 1, Question 3
            AnswerEntity(questionId = "ancient_1_3", text = "Hieroglyphics", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_1_3", text = "Cuneiform", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_1_3", text = "Sanskrit", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_1_3", text = "Aramaic", isCorrect = false, sortOrder = 3),
            
            // Level 1, Question 4
            AnswerEntity(questionId = "ancient_1_4", text = "Nile", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_1_4", text = "Tigris", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_1_4", text = "Euphrates", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_1_4", text = "Jordan", isCorrect = false, sortOrder = 3),
            
            // Level 2, Question 1
            AnswerEntity(questionId = "ancient_2_1", text = "Cleopatra VII", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_2_1", text = "Nefertiti", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_2_1", text = "Hatshepsut", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_2_1", text = "Nefertari", isCorrect = false, sortOrder = 3),
            
            // Level 2, Question 2
            AnswerEntity(questionId = "ancient_2_2", text = "Tombs for pharaohs", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_2_2", text = "Temples for worship", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_2_2", text = "Astronomical observatories", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_2_2", text = "Granaries for food storage", isCorrect = false, sortOrder = 3),
            
            // Level 2, Question 3
            AnswerEntity(questionId = "ancient_2_3", text = "Papyrus", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_2_3", text = "Parchment", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_2_3", text = "Cotton", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_2_3", text = "Silk", isCorrect = false, sortOrder = 3),
            
            // Level 2, Question 4
            AnswerEntity(questionId = "ancient_2_4", text = "Akhenaten", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_2_4", text = "Ramses II", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_2_4", text = "Tutankhamun", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_2_4", text = "Thutmose III", isCorrect = false, sortOrder = 3),
            
            // Level 3, Question 1
            AnswerEntity(questionId = "ancient_3_1", text = "Great Sphinx", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_3_1", text = "Anubis", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_3_1", text = "Sekhmet", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_3_1", text = "Horus", isCorrect = false, sortOrder = 3),
            
            // Level 3, Question 2
            AnswerEntity(questionId = "ancient_3_2", text = "Osiris", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_3_2", text = "Ra", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_3_2", text = "Horus", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_3_2", text = "Anubis", isCorrect = false, sortOrder = 3),
            
            // Level 3, Question 3
            AnswerEntity(questionId = "ancient_3_3", text = "Mummification", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_3_3", text = "Cremation", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_3_3", text = "Embalming", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_3_3", text = "Entombment", isCorrect = false, sortOrder = 3),
            
            // Level 3, Question 4
            AnswerEntity(questionId = "ancient_3_4", text = "Hatshepsut", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_3_4", text = "Cleopatra", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_3_4", text = "Nefertiti", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_3_4", text = "Tiye", isCorrect = false, sortOrder = 3),
            
            // Level 4, Question 1
            AnswerEntity(questionId = "ancient_4_1", text = "Rosetta Stone", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_4_1", text = "Turin Papyrus", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_4_1", text = "Narmer Palette", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_4_1", text = "Rhind Papyrus", isCorrect = false, sortOrder = 3),
            
            // Level 4, Question 2
            AnswerEntity(questionId = "ancient_4_2", text = "Kemet", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_4_2", text = "Misr", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_4_2", text = "Aegyptus", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_4_2", text = "Nubia", isCorrect = false, sortOrder = 3),
            
            // Level 4, Question 3
            AnswerEntity(questionId = "ancient_4_3", text = "Ramses II", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_4_3", text = "Seti I", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_4_3", text = "Amenhotep III", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_4_3", text = "Tuthmosis IV", isCorrect = false, sortOrder = 3),
            
            // Level 4, Question 4
            AnswerEntity(questionId = "ancient_4_4", text = "Weighing the heart of the deceased", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_4_4", text = "Crowning new pharaohs", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_4_4", text = "Decorating royal headdresses", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_4_4", text = "Writing messages to the gods", isCorrect = false, sortOrder = 3),
            
            // Level 5, Question 1
            AnswerEntity(questionId = "ancient_5_1", text = "Inca", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_5_1", text = "Maya", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_5_1", text = "Aztec", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_5_1", text = "Olmec", isCorrect = false, sortOrder = 3),
            
            // Level 5, Question 2
            AnswerEntity(questionId = "ancient_5_2", text = "Latin", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_5_2", text = "Greek", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_5_2", text = "Etruscan", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_5_2", text = "Aramaic", isCorrect = false, sortOrder = 3),
            
            // Level 5, Question 3
            AnswerEntity(questionId = "ancient_5_3", text = "Aristotle", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_5_3", text = "Socrates", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_5_3", text = "Plato", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_5_3", text = "Pythagoras", isCorrect = false, sortOrder = 3),
            
            // Level 5, Question 4
            AnswerEntity(questionId = "ancient_5_4", text = "Mayans", isCorrect = true, sortOrder = 0),
            AnswerEntity(questionId = "ancient_5_4", text = "Egyptians", isCorrect = false, sortOrder = 1),
            AnswerEntity(questionId = "ancient_5_4", text = "Chinese", isCorrect = false, sortOrder = 2),
            AnswerEntity(questionId = "ancient_5_4", text = "Greeks", isCorrect = false, sortOrder = 3)
        )
        
        // Insert answers
        database.answerDao().insertAnswers(egyptAnswers)
        
        // Further levels would be added here with similar patterns
        // This is just a starting point for the database population
    }
    
    /**
     * Helper function to convert hex string to ColorInt
     */
    private fun String.toColorInt(): Int {
        return Color.parseColor(this)
    }
} 