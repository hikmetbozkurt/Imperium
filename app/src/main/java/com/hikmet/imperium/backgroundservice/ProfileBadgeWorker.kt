package com.hikmet.imperium.backgroundservice

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hikmet.imperium.retrofit.BadgeApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

/**
 * Background worker for refreshing badge data using WorkManager and CoroutineWorker
 * Fulfills the Worker requirement for the project
 */
@HiltWorker
class ProfileBadgeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val badgeApiService: BadgeApiService
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
            
            // Simulate background processing delay
            delay(2000)
            
            // Fetch fresh badge data from API
            Log.d(TAG, "📡 Fetching badge data from API...")
            val response = badgeApiService.getBadges()
            
            if (response.isSuccessful && response.body() != null) {
                val badges = response.body()!!.badgeSystem
                val badgeCount = badges.size
                val executionTime = System.currentTimeMillis() - startTime
                
                Log.d(TAG, "✅ Badge refresh completed successfully!")
                Log.d(TAG, "📊 Found $badgeCount badges in ${executionTime}ms")
                
                // Store results for UI update
                val outputData = workDataOf(
                    KEY_BADGE_COUNT to badgeCount,
                    KEY_EXECUTION_TIME to executionTime,
                    KEY_SUCCESS to true
                )
                
                // Cache the fresh badge data
                cacheBadgeData(badges)
                
                Result.success(outputData)
            } else {
                throw Exception("API call failed: ${response.message()}")
            }
            
        } catch (exception: Exception) {
            Log.e(TAG, "❌ Badge refresh failed: ${exception.message}", exception)
            
            val outputData = workDataOf(
                KEY_SUCCESS to false,
                KEY_EXECUTION_TIME to 0L
            )
            
            // Retry on failure
            if (runAttemptCount < 3) {
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
    private suspend fun cacheBadgeData(badges: List<com.hikmet.imperium.retrofit.Badge>) {
        try {
            Log.d(TAG, "💾 Caching ${badges.size} badges locally...")
            
            // Store in shared preferences or database for quick access
            val sharedPrefs = applicationContext.getSharedPreferences("badge_cache", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            
            // Cache badge count and last update time
            editor.putInt("cached_badge_count", badges.size)
            editor.putLong("last_cache_update", System.currentTimeMillis())
            editor.putBoolean("cache_valid", true)
            
            // Cache first few badge titles for quick preview
            badges.take(5).forEachIndexed { index, badge ->
                editor.putString("badge_title_$index", badge.title)
            }
            
            editor.apply()
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