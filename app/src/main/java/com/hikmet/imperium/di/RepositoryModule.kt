package com.hikmet.imperium.di

import com.hikmet.imperium.data.repository.LocalHistoryContentRepository
import com.hikmet.imperium.data.repository.RoomGameProgressRepository
import com.hikmet.imperium.data.repository.ProgressRepository
import com.hikmet.imperium.domain.repository.GameProgressRepository
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import com.hikmet.imperium.domain.repository.ProgressAnalyticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindHistoryContentRepository(
        implementation: LocalHistoryContentRepository,
    ): HistoryContentRepository

    @Binds
    @Singleton
    abstract fun bindGameProgressRepository(
        implementation: RoomGameProgressRepository,
    ): GameProgressRepository

    @Binds
    @Singleton
    abstract fun bindProgressAnalyticsRepository(
        implementation: ProgressRepository,
    ): ProgressAnalyticsRepository
}
