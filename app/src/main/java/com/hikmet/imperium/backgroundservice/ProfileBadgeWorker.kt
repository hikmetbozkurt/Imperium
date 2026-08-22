package com.hikmet.imperium.backgroundservice

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hikmet.imperium.retrofit.BadgeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

/**
 * Background worker for refreshing badge data using WorkManager and CoroutineWorker
 * Fulfills the Worker requirement for the project
 */
@HiltWorker
class ProfileBadgeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val badgeRepository: BadgeRepository,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val TAG = "ProfileBadgeWorker"
        const val WORK_NAME = "profile_badge_refresh"
        const val KEY_BADGE_COUNT = "badge_count"
        const val KEY_EXECUTION_TIME = "execution_time"
        const val KEY_SUCCESS = "success"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "🔄 Starting background badge refresh...")
        
        return try {
            val startTime = System.currentTimeMillis()
            
            Log.d(TAG, "📡 Fetching badge data from API...")
            val badges = badgeRepository.fetchBadges()
            val executionTime = System.currentTimeMillis() - startTime
            cacheBadgeData(badges)
            Result.success(
                workDataOf(
                    KEY_BADGE_COUNT to badges.size,
                    KEY_EXECUTION_TIME to executionTime,
                    KEY_SUCCESS to true,
                ),
            )
            
        } catch (exception: Exception) {
            Log.e(TAG, "❌ Badge refresh failed: ${exception.message}", exception)
            
            val outputData = workDataOf(
                KEY_SUCCESS to false,
                KEY_EXECUTION_TIME to 0L
            )
            
            val isTransient = exception is IOException ||
                (exception is HttpException && exception.code() >= 500)
            if (isTransient && runAttemptCount < 3) {
                Log.d(TAG, "🔄 Retrying badge refresh (attempt ${runAttemptCount + 1}/3)")
                Result.retry()
            } else {
                Log.e(TAG, "💥 Badge refresh failed after 3 attempts")
                Result.failure(outputData)
            }
        }
    }
    
    /**
     * Cache badge data locally for faster UI updates
     */
    private fun cacheBadgeData(badges: List<com.hikmet.imperium.retrofit.Badge>) {
        try {
            Log.d(TAG, "💾 Caching ${badges.size} badges locally...")
            
            // Store in shared preferences or database for quick access
            val sharedPrefs = applicationContext.getSharedPreferences("badge_cache", Context.MODE_PRIVATE)
            sharedPrefs.edit {
                putInt("cached_badge_count", badges.size)
                putLong("last_cache_update", System.currentTimeMillis())
                putBoolean("cache_valid", true)
                badges.take(5).forEachIndexed { index, badge ->
                    putString("badge_title_$index", badge.title)
                }
            }
            Log.d(TAG, "✅ Badge data cached successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to cache badge data: ${e.message}", e)
        }
    }
    
    /**
     * Get cached badge information
     */
    fun getCachedBadgeInfo(): CachedBadgeInfo {
        val sharedPrefs = applicationContext.getSharedPreferences("badge_cache", Context.MODE_PRIVATE)
        
        return CachedBadgeInfo(
            badgeCount = sharedPrefs.getInt("cached_badge_count", 0),
            lastUpdateTime = sharedPrefs.getLong("last_cache_update", 0),
            isValid = sharedPrefs.getBoolean("cache_valid", false)
        )
    }
}

/**
 * Data class for cached badge information
 */
data class CachedBadgeInfo(
    val badgeCount: Int,
    val lastUpdateTime: Long,
    val isValid: Boolean
)
