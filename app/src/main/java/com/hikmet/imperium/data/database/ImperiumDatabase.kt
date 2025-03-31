package com.hikmet.imperium.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
} 