package com.hikmet.imperium.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hikmet.imperium.data.dao.AnswerDao
import com.hikmet.imperium.data.dao.CategoryDao
import com.hikmet.imperium.data.dao.LevelDao
import com.hikmet.imperium.data.dao.LevelProgressDao
import com.hikmet.imperium.data.dao.QuestionDao
import com.hikmet.imperium.data.dao.QuizAttemptDao
import com.hikmet.imperium.data.dao.UserProgressDao
import com.hikmet.imperium.data.entities.AnswerEntity
import com.hikmet.imperium.data.entities.CategoryEntity
import com.hikmet.imperium.data.entities.LevelEntity
import com.hikmet.imperium.data.entities.LevelProgressEntity
import com.hikmet.imperium.data.entities.QuestionEntity
import com.hikmet.imperium.data.entities.QuizAttemptEntity
import com.hikmet.imperium.data.entities.UserProgressEntity

@Database(
    entities = [
        CategoryEntity::class,
        UserProgressEntity::class,
        LevelEntity::class,
        LevelProgressEntity::class,
        QuestionEntity::class,
        AnswerEntity::class,
        QuizAttemptEntity::class,
    ],
    version = 4,
    exportSchema = true,
)
@TypeConverters(MapIntIntConverter::class)
abstract class ImperiumDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun levelDao(): LevelDao
    abstract fun levelProgressDao(): LevelProgressDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    abstract fun quizAttemptDao(): QuizAttemptDao

    companion object {
        @Volatile
        private var instance: ImperiumDatabase? = null

        fun getDatabase(context: Context): ImperiumDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                ImperiumDatabase::class.java,
                "imperium_database",
            )
                .addMigrations(DatabaseMigrations.MIGRATION_3_4)
                .build()
                .also { instance = it }
        }
    }
}
