package com.hikmet.imperium.di

import android.content.Context
import androidx.room.RoomDatabase // For RoomDatabase.Callback if used directly in AppModule
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.data.dao.CategoryDao
import com.hikmet.imperium.data.dao.LevelDao
import com.hikmet.imperium.data.dao.QuestionDao
import com.hikmet.imperium.data.dao.AnswerDao
import com.hikmet.imperium.data.dao.UserProgressDao
import com.hikmet.imperium.data.dao.LevelProgressDao
import com.hikmet.imperium.data.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideImperiumDatabase(
        @ApplicationContext context: Context
    ): ImperiumDatabase {
        return ImperiumDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideCategoryDao(database: ImperiumDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Singleton
    @Provides
    fun provideLevelDao(database: ImperiumDatabase): LevelDao {
        return database.levelDao()
    }

    @Singleton
    @Provides
    fun provideQuestionDao(database: ImperiumDatabase): QuestionDao {
        return database.questionDao()
    }

    @Singleton
    @Provides
    fun provideAnswerDao(database: ImperiumDatabase): AnswerDao {
        return database.answerDao()
    }

    @Singleton
    @Provides
    fun provideUserProgressDao(database: ImperiumDatabase): UserProgressDao {
        return database.userProgressDao()
    }

    @Singleton
    @Provides
    fun provideLevelProgressDao(database: ImperiumDatabase): LevelProgressDao {
        return database.levelProgressDao()
    }

    // QuizRepository is already annotated with @Singleton and @Inject constructor,
    // so Hilt can provide it automatically if all its dependencies (ImperiumDatabase) are provided.
    // If we weren't using @Inject constructor on QuizRepository, we would provide it here:
    /*
    @Singleton
    @Provides
    fun provideQuizRepository(database: ImperiumDatabase): QuizRepository {
        return QuizRepository(database)
    }
    */
} 