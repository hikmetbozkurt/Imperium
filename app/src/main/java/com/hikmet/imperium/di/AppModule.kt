package com.hikmet.imperium.di

import android.content.Context
import com.hikmet.imperium.BuildConfig
import com.hikmet.imperium.data.database.ImperiumDatabase
import com.hikmet.imperium.data.dao.QuizAttemptDao
import com.hikmet.imperium.ui.util.SoundManager
import com.hikmet.imperium.retrofit.BadgeApiService
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

    @Provides
    fun provideQuizAttemptDao(database: ImperiumDatabase): QuizAttemptDao = database.quizAttemptDao()

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
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        
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
    
}
