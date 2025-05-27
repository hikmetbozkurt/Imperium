package com.hikmet.imperium.backgroundservice

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WorkManager controller for scheduling and managing badge refresh workers
 * Handles periodic and one-time badge data updates
 */
@Singleton
class BadgeWorkManager @Inject constructor(
    private val context: Context
) {
    
    companion object {
        const val TAG = "BadgeWorkManager"
        const val PERIODIC_WORK_NAME = "periodic_badge_refresh"
        const val ONE_TIME_WORK_NAME = "one_time_badge_refresh"
    }
    
    private val workManager = WorkManager.getInstance(context)
    
    /**
     * Schedule periodic badge refresh (every 2 hours)
     */
    fun schedulePeriodicBadgeRefresh() {
        Log.d(TAG, "📅 Scheduling periodic badge refresh...")
        
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        
        val periodicWorkRequest = PeriodicWorkRequestBuilder<ProfileBadgeWorker>(
            2, TimeUnit.HOURS,  // Repeat every 2 hours
            30, TimeUnit.MINUTES // Flex interval
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("badge_refresh")
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
        
        Log.d(TAG, "✅ Periodic badge refresh scheduled")
    }
    
    /**
     * Trigger immediate badge refresh (for Profile screen entry)
     */
    fun triggerImmediateBadgeRefresh(): LiveData<WorkInfo> {
        Log.d(TAG, "⚡ Triggering immediate badge refresh...")
        
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<ProfileBadgeWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("immediate_badge_refresh")
            .build()
        
        workManager.enqueueUniqueWork(
            ONE_TIME_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
        
        return workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
    }
    
    /**
     * Get status of badge refresh work
     */
    fun getBadgeRefreshStatus(): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosByTagLiveData("badge_refresh")
    }
    
    /**
     * Cancel all badge refresh work
     */
    fun cancelAllBadgeWork() {
        Log.d(TAG, "🛑 Cancelling all badge refresh work...")
        workManager.cancelAllWorkByTag("badge_refresh")
        workManager.cancelAllWorkByTag("immediate_badge_refresh")
        Log.d(TAG, "✅ All badge work cancelled")
    }
    
    /**
     * Get work info for immediate refresh
     */
    fun getImmediateRefreshStatus(): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosByTagLiveData("immediate_badge_refresh")
    }
    
    /**
     * Check if periodic work is scheduled
     */
    fun isPeriodicWorkScheduled(): Boolean {
        val workInfos = workManager.getWorkInfosForUniqueWork(PERIODIC_WORK_NAME)
        return try {
            val workInfo = workInfos.get().firstOrNull()
            workInfo?.state == WorkInfo.State.ENQUEUED || workInfo?.state == WorkInfo.State.RUNNING
        } catch (e: Exception) {
            Log.e(TAG, "Error checking periodic work status: ${e.message}")
            false
        }
    }
    
    /**
     * Initialize badge work on app start
     */
    fun initializeBadgeWork() {
        Log.d(TAG, "🚀 Initializing badge work system...")
        
        // Schedule periodic refresh if not already scheduled
        if (!isPeriodicWorkScheduled()) {
            schedulePeriodicBadgeRefresh()
        } else {
            Log.d(TAG, "⏰ Periodic badge refresh already scheduled")
        }
        
        // Trigger immediate refresh for fresh data
        triggerImmediateBadgeRefresh()
        
        Log.d(TAG, "✅ Badge work system initialized")
    }
    
    /**
     * Get work statistics for debugging
     */
    fun getWorkStatistics(): WorkStatistics {
        val periodicWork = workManager.getWorkInfosForUniqueWork(PERIODIC_WORK_NAME)
        val immediateWork = workManager.getWorkInfosForUniqueWork(ONE_TIME_WORK_NAME)
        
        return WorkStatistics(
            periodicWorkScheduled = isPeriodicWorkScheduled(),
            periodicWorkState = try { periodicWork.get().firstOrNull()?.state?.name ?: "UNKNOWN" } catch (e: Exception) { "ERROR" },
            immediateWorkState = try { immediateWork.get().firstOrNull()?.state?.name ?: "UNKNOWN" } catch (e: Exception) { "ERROR" },
            lastRefreshTime = getLastRefreshTime()
        )
    }
    
    private fun getLastRefreshTime(): Long {
        val sharedPrefs = context.getSharedPreferences("badge_cache", Context.MODE_PRIVATE)
        return sharedPrefs.getLong("last_cache_update", 0)
    }
}

/**
 * Data class for work statistics
 */
data class WorkStatistics(
    val periodicWorkScheduled: Boolean,
    val periodicWorkState: String,
    val immediateWorkState: String,
    val lastRefreshTime: Long
) 