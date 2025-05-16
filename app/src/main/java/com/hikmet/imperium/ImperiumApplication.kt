package com.hikmet.imperium

import android.app.Application
// import android.graphics.Color // No longer needed here
// import androidx.core.graphics.toColorInt // No longer needed here
import com.hikmet.imperium.data.database.ImperiumDatabase
// import com.hikmet.imperium.data.entities.AnswerEntity // No longer needed here
// import com.hikmet.imperium.data.entities.CategoryEntity // No longer needed here
// import com.hikmet.imperium.data.entities.LevelEntity // No longer needed here
// import com.hikmet.imperium.data.entities.QuestionEntity // No longer needed here
// import com.hikmet.imperium.data.entities.UserProgressEntity // No longer needed here
import com.hikmet.imperium.data.repository.QuizRepository
// import com.hikmet.imperium.data.RenaissanceQuizData // No longer needed here
// import kotlinx.coroutines.CoroutineScope // No longer needed here
// import kotlinx.coroutines.Dispatchers // No longer needed here
// import kotlinx.coroutines.launch // No longer needed here

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
        // Database pre-population is now handled by RoomDatabase.Callback in ImperiumDatabase.kt
        // Triggering database access here ensures it gets created if not already.
        database.isOpen // Accessing a property to ensure initialization
    }
    
    // All pre-population methods previously here (prepopulateDatabaseIfNeeded, 
    // insertAncientCivilizationsQuestions, insertRenaissanceQuestions, toColorInt)
    // have been moved or their logic incorporated into ImperiumDatabase.kt's companion object and callback.
} 