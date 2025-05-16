package com.hikmet.imperium.data.database

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hikmet.imperium.data.RenaissanceQuizData
import com.hikmet.imperium.data.dao.AnswerDao
import com.hikmet.imperium.data.dao.CategoryDao
import com.hikmet.imperium.data.dao.LevelDao
import com.hikmet.imperium.data.dao.LevelProgressDao
import com.hikmet.imperium.data.dao.QuestionDao
import com.hikmet.imperium.data.dao.UserProgressDao
import com.hikmet.imperium.data.entities.AnswerEntity
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.QuestionEntity
import com.hikmet.imperium.data.entities.UserProgressEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log

// Helper extension function (can be moved to a common utils file if preferred)
fun String.toColorIntHelper(): Int {
    return Color.parseColor(this)
}

/**
 * Main Room database class for the Imperium app
 * Contains all DAOs and entities
 */
@Database(
    entities = [
        CategoryEntity::class,
        UserProgressEntity::class,
        LevelEntity::class,
        LevelProgressEntity::class,
        QuestionEntity::class,
        AnswerEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ImperiumDatabase : RoomDatabase() {
    
    // DAOs
    abstract fun categoryDao(): CategoryDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun levelDao(): LevelDao
    abstract fun levelProgressDao(): LevelProgressDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    
    companion object {
        // Singleton to prevent multiple instances
        @Volatile
        private var INSTANCE: ImperiumDatabase? = null
        
        /**
         * Get database instance
         */
        fun getDatabase(context: Context): ImperiumDatabase {
            // If instance doesn't exist, create it
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImperiumDatabase::class.java,
                    "imperium_database"
                )
                .fallbackToDestructiveMigration() // Recreate database if version changes
                .addCallback(DatabaseCallback(context.applicationContext))
                .build()
                
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        prepopulateDatabase(it, context)
                    }
                }
            }
        }

        suspend fun prepopulateDatabase(database: ImperiumDatabase, context: Context) {
            // Insert categories
            val categories = listOf(
                CategoryEntity(
                    id = "ancient",
                    title = "Ancient Civilizations",
                    description = "Explore the ancient world from Egypt to China",
                    longDescription = "Discover the rich history of the ancient world, from the pyramids of Egypt to the first emperor of China. Learn about the great civilizations that laid the foundations for our modern world.",
                    totalLevels = 20,
                    gradientStartColor = "#227C70".toColorIntHelper(),
                    gradientEndColor = "#1E5631".toColorIntHelper(),
                    iconResourceName = "ic_ancient"
                ),
                CategoryEntity(
                    id = "medieval",
                    title = "Medieval Period",
                    description = "Discover the Middle Ages from Europe to Asia",
                    longDescription = "Journey through the medieval period, a time of knights, castles, and great empires spanning from Europe to the far reaches of Asia.",
                    totalLevels = 20,
                    gradientStartColor = "#4B0082".toColorIntHelper(),
                    gradientEndColor = "#26428B".toColorIntHelper(),
                    iconResourceName = "ic_medieval"
                ),
                CategoryEntity(
                    id = "renaissance",
                    title = "Renaissance",
                    description = "Experience the rebirth of art, culture and learning",
                    longDescription = "Immerse yourself in the European Renaissance, a period of great cultural change and achievement that bridged the gap between the Middle Ages and modern history.",
                    totalLevels = 15,
                    gradientStartColor = "#8B0000".toColorIntHelper(),
                    gradientEndColor = "#FF4500".toColorIntHelper(),
                    iconResourceName = "ic_renaissance"
                )
            )
            database.categoryDao().insertCategories(categories)

            // Set up default user progress
            val userProgressEntities = listOf(
                UserProgressEntity(
                    categoryId = "ancient",
                    unlockedLevels = 1, totalStarsEarned = 0, highestLevelCompleted = 0, lastPlayedTimestamp = System.currentTimeMillis()
                ),
                UserProgressEntity(
                    categoryId = "medieval",
                    unlockedLevels = 1, totalStarsEarned = 0, highestLevelCompleted = 0, lastPlayedTimestamp = System.currentTimeMillis()
                ),
                 UserProgressEntity(
                    categoryId = "renaissance",
                    unlockedLevels = 1, totalStarsEarned = 0, highestLevelCompleted = 0, lastPlayedTimestamp = System.currentTimeMillis()
                )
            )
            userProgressEntities.forEach { database.userProgressDao().insertOrUpdateProgress(it) }

            // Create levels for Ancient Civilizations
            val ancientLevels = (1..20).map { levelNumber ->
                LevelEntity(
                    categoryId = "ancient", levelNumber = levelNumber, title = "Level $levelNumber", description = "Ancient Civilizations Level $levelNumber",
                    difficulty = when (levelNumber) {
                        in 1..5 -> 1; in 6..10 -> 2; in 11..15 -> 3; else -> 4
                    },
                    requiredStarsToUnlock = when (levelNumber) {
                        1 -> 0; in 2..5 -> (levelNumber - 1) * 2; in 6..10 -> 10 + (levelNumber - 5) * 3; in 11..15 -> 25 + (levelNumber - 10) * 4; else -> 45 + (levelNumber - 15) * 5
                    }
                )
            }
            database.levelDao().insertLevels(ancientLevels)
            insertAncientCivilizationsQuestions(database)

            // Create levels for Renaissance
            val renaissanceLevels = (1..15).map { levelNumber ->
                LevelEntity(
                    categoryId = "renaissance", 
                    levelNumber = levelNumber,
                    title = when (levelNumber) {
                        1 -> "Early Renaissance"; 2 -> "Renaissance Art"; 3 -> "Humanism"; 
                        4 -> "The Medici Family"; 5 -> "Renaissance Architecture"; 
                        6 -> "Science & Invention"; 7 -> "The Printing Press"; 
                        8 -> "Renaissance Literature"; 9 -> "Renaissance Music"; 
                        10 -> "The Catholic Church"; 11 -> "Northern Renaissance"; 
                        12 -> "Women in the Renaissance"; 13 -> "The Age of Exploration"; 
                        14 -> "High Renaissance"; 15 -> "Politics & Diplomacy"; 
                        else -> "Level $levelNumber"
                    },
                    description = "Renaissance Level $levelNumber",
                    difficulty = when (levelNumber) { in 1..5 -> 1; in 6..10 -> 2; else -> 3 },
                    requiredStarsToUnlock = when (levelNumber) {
                        1 -> 0; in 2..5 -> (levelNumber - 1) * 2; 
                        in 6..10 -> 10 + (levelNumber - 5) * 3; 
                        else -> 25 + (levelNumber - 10) * 4
                    }
                )
            }
            database.levelDao().insertLevels(renaissanceLevels)
            
            // Insert Renaissance questions
            insertRenaissanceQuestions(database)
        }

        private suspend fun insertAncientCivilizationsQuestions(database: ImperiumDatabase) {
            val egyptQuestions = listOf(
                QuestionEntity(id = "ancient_1_1", categoryId = "ancient", levelNumber = 1, text = "Which ancient Egyptian structure is the oldest of the Seven Wonders of the Ancient World?", difficulty = 1),
                QuestionEntity(id = "ancient_1_2", categoryId = "ancient", levelNumber = 1, text = "Which pharaoh's tomb was discovered nearly intact by Howard Carter in 1922?", difficulty = 1),
                QuestionEntity(id = "ancient_1_3", categoryId = "ancient", levelNumber = 1, text = "What is the name of the ancient Egyptian writing system?", difficulty = 1),
                QuestionEntity(id = "ancient_1_4", categoryId = "ancient", levelNumber = 1, text = "Which river was central to ancient Egyptian civilization?", difficulty = 1),
                QuestionEntity(id = "ancient_2_1", categoryId = "ancient", levelNumber = 2, text = "Who was the Egyptian queen who aligned herself with Julius Caesar and Mark Antony?", difficulty = 1),
                QuestionEntity(id = "ancient_2_2", categoryId = "ancient", levelNumber = 2, text = "What was the main purpose of the Egyptian pyramids?", difficulty = 1),
                QuestionEntity(id = "ancient_2_3", categoryId = "ancient", levelNumber = 2, text = "What material did ancient Egyptians use to write on?", difficulty = 1),
                QuestionEntity(id = "ancient_2_4", categoryId = "ancient", levelNumber = 2, text = "Which pharaoh is known for establishing the first monotheistic religion in Egypt?", difficulty = 1),
                QuestionEntity(id = "ancient_3_1", categoryId = "ancient", levelNumber = 3, text = "What is the name of the legendary half-lion, half-human statue near the Pyramids of Giza?", difficulty = 1),
                QuestionEntity(id = "ancient_3_2", categoryId = "ancient", levelNumber = 3, text = "Which ancient Egyptian god is associated with the afterlife and resurrection?", difficulty = 2),
                QuestionEntity(id = "ancient_3_3", categoryId = "ancient", levelNumber = 3, text = "What was the name of the process used by ancient Egyptians to preserve bodies?", difficulty = 1),
                QuestionEntity(id = "ancient_3_4", categoryId = "ancient", levelNumber = 3, text = "Which female pharaoh ruled during Egypt's 18th dynasty and established trading networks?", difficulty = 2),
                QuestionEntity(id = "ancient_4_1", categoryId = "ancient", levelNumber = 4, text = "What stone, found in 1799, helped scholars decipher Egyptian hieroglyphics?", difficulty = 2),
                QuestionEntity(id = "ancient_4_2", categoryId = "ancient", levelNumber = 4, text = "What was the ancient Egyptian name for their country?", difficulty = 2),
                QuestionEntity(id = "ancient_4_3", categoryId = "ancient", levelNumber = 4, text = "Which pharaoh commissioned the construction of the temple at Abu Simbel?", difficulty = 2),
                QuestionEntity(id = "ancient_4_4", categoryId = "ancient", levelNumber = 4, text = "In ancient Egyptian religion, what was the feather of Ma'at used for?", difficulty = 2),
                QuestionEntity(id = "ancient_5_1", categoryId = "ancient", levelNumber = 5, text = "Which ancient civilization built the city of Machu Picchu?", difficulty = 2),
                QuestionEntity(id = "ancient_5_2", categoryId = "ancient", levelNumber = 5, text = "What was the main language of ancient Rome?", difficulty = 2),
                QuestionEntity(id = "ancient_5_3", categoryId = "ancient", levelNumber = 5, text = "Which ancient Greek philosopher was the teacher of Alexander the Great?", difficulty = 2),
                QuestionEntity(id = "ancient_5_4", categoryId = "ancient", levelNumber = 5, text = "What ancient civilization is credited with inventing the concept of zero?", difficulty = 2)
            )
            database.questionDao().insertQuestions(egyptQuestions)
            val egyptAnswers = listOf(
                AnswerEntity(questionId = "ancient_1_1", text = "Great Pyramid of Giza", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_1_1", text = "Hanging Gardens of Babylon", isCorrect = false, sortOrder = 1),
                // ... (rest of the ancient answers, truncated for brevity in this plan) ...
                 AnswerEntity(questionId = "ancient_5_4", text = "Greeks", isCorrect = false, sortOrder = 3)
            )
             // Manually create the full list of answers as it was in ImperiumApplication.kt
            val fullEgyptAnswers = listOf(
                AnswerEntity(questionId = "ancient_1_1", text = "Great Pyramid of Giza", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_1_1", text = "Hanging Gardens of Babylon", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_1_1", text = "Lighthouse of Alexandria", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_1_1", text = "Colossus of Rhodes", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_1_2", text = "Tutankhamun", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_1_2", text = "Ramses II", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_1_2", text = "Cleopatra", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_1_2", text = "Khufu", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_1_3", text = "Hieroglyphics", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_1_3", text = "Cuneiform", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_1_3", text = "Sanskrit", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_1_3", text = "Aramaic", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_1_4", text = "Nile", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_1_4", text = "Tigris", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_1_4", text = "Euphrates", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_1_4", text = "Jordan", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_2_1", text = "Cleopatra VII", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_2_1", text = "Nefertiti", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_2_1", text = "Hatshepsut", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_2_1", text = "Nefertari", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_2_2", text = "Tombs for pharaohs", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_2_2", text = "Temples for worship", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_2_2", text = "Astronomical observatories", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_2_2", text = "Granaries for food storage", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_2_3", text = "Papyrus", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_2_3", text = "Parchment", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_2_3", text = "Cotton", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_2_3", text = "Silk", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_2_4", text = "Akhenaten", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_2_4", text = "Ramses II", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_2_4", text = "Tutankhamun", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_2_4", text = "Thutmose III", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_3_1", text = "Great Sphinx", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_3_1", text = "Anubis", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_3_1", text = "Sekhmet", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_3_1", text = "Horus", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_3_2", text = "Osiris", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_3_2", text = "Ra", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_3_2", text = "Horus", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_3_2", text = "Anubis", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_3_3", text = "Mummification", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_3_3", text = "Cremation", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_3_3", text = "Embalming", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_3_3", text = "Entombment", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_3_4", text = "Hatshepsut", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_3_4", text = "Cleopatra", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_3_4", text = "Nefertiti", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_3_4", text = "Tiye", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_4_1", text = "Rosetta Stone", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_4_1", text = "Turin Papyrus", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_4_1", text = "Narmer Palette", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_4_1", text = "Rhind Papyrus", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_4_2", text = "Kemet", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_4_2", text = "Misr", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_4_2", text = "Aegyptus", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_4_2", text = "Nubia", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_4_3", text = "Ramses II", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_4_3", text = "Seti I", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_4_3", text = "Amenhotep III", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_4_3", text = "Tuthmosis IV", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_4_4", text = "Weighing the heart of the deceased", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_4_4", text = "Crowning new pharaohs", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_4_4", text = "Decorating royal headdresses", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_4_4", text = "Writing messages to the gods", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_5_1", text = "Inca", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_5_1", text = "Maya", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_5_1", text = "Aztec", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_5_1", text = "Olmec", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_5_2", text = "Latin", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_5_2", text = "Greek", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_5_2", text = "Etruscan", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_5_2", text = "Aramaic", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_5_3", text = "Aristotle", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_5_3", text = "Socrates", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_5_3", text = "Plato", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_5_3", text = "Pythagoras", isCorrect = false, sortOrder = 3),
                AnswerEntity(questionId = "ancient_5_4", text = "Mayans", isCorrect = true, sortOrder = 0),
                AnswerEntity(questionId = "ancient_5_4", text = "Egyptians", isCorrect = false, sortOrder = 1),
                AnswerEntity(questionId = "ancient_5_4", text = "Chinese", isCorrect = false, sortOrder = 2),
                AnswerEntity(questionId = "ancient_5_4", text = "Greeks", isCorrect = false, sortOrder = 3)
            )
            database.answerDao().insertAnswers(fullEgyptAnswers)
        }

        private suspend fun insertRenaissanceQuestions(database: ImperiumDatabase) {
            try {
                val renaissanceQuizDataObject = RenaissanceQuizData
                val questions = mutableListOf<QuestionEntity>()
                val answers = mutableListOf<AnswerEntity>()
                
                for (level in 1..15) {
                    val levelQuestions = renaissanceQuizDataObject.getQuestionsByLevel(level.toString())
                    Log.d("DB_PREPOPULATE", "Inserting ${levelQuestions.size} questions for Renaissance level $level")
                    
                    levelQuestions.forEachIndexed { questionIndex, questionData ->
                        val questionId = "renaissance_${level}_${questionIndex + 1}"
                        questions.add(
                            QuestionEntity(
                                id = questionId, 
                                categoryId = "renaissance", 
                                levelNumber = level, 
                                text = questionData.text,
                                difficulty = when (level) { in 1..5 -> 1; in 6..10 -> 2; else -> 3 }
                            )
                        )
                        
                        questionData.options.forEachIndexed { answerIndex, optionText ->
                            answers.add(
                                AnswerEntity(
                                    questionId = questionId, 
                                    text = optionText, 
                                    isCorrect = answerIndex == questionData.correctAnswerIndex, 
                                    sortOrder = answerIndex
                                )
                            )
                        }
                    }
                }
                
                database.questionDao().insertQuestions(questions)
                database.answerDao().insertAnswers(answers)
                Log.d("DB_PREPOPULATE", "Successfully inserted ${questions.size} Renaissance questions and ${answers.size} answers")
            } catch (e: Exception) {
                Log.e("DB_PREPOPULATE", "Failed to insert Renaissance questions: ${e.message}", e)
                throw e
            }
        }
    }
} 