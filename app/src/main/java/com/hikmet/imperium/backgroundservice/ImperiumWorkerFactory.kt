package com.hikmet.imperium.backgroundservice

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.hikmet.imperium.retrofit.BadgeApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Custom WorkerFactory for Hilt dependency injection
 * Enables Workers to receive dependencies through Hilt
 */
@Singleton
class ImperiumWorkerFactory @Inject constructor(
    private val badgeApiService: BadgeApiService
) : WorkerFactory() {
    
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        
        return when (workerClassName) {
            ProfileBadgeWorker::class.java.name -> {
                ProfileBadgeWorker(appContext, workerParameters, badgeApiService)
            }
            else -> null
        }
    }
} 