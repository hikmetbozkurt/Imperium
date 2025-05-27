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
import com.hikmet.imperium.ui.util.SoundManager
import com.hikmet.imperium.retrofit.BadgeApiService
import com.hikmet.imperium.backgroundservice.BadgeWorkManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideSoundManager(
        @ApplicationContext context: Context
    ): SoundManager {
        return SoundManager(context)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.jsonkeeper.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideBadgeApiService(retrofit: Retrofit): BadgeApiService {
        return retrofit.create(BadgeApiService::class.java)
    }
    
    @Singleton
    @Provides
    fun provideBadgeWorkManager(
        @ApplicationContext context: Context
    ): BadgeWorkManager {
        return BadgeWorkManager(context)
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