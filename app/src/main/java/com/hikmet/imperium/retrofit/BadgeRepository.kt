package com.hikmet.imperium.retrofit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing badge data
 * Handles API calls and data processing
 */
@Singleton
class BadgeRepository @Inject constructor(
    private val badgeApiService: BadgeApiService
) {
    
    /**
     * Fetch badges from API and return as Flow
     */
    fun getBadges(): Flow<ApiResult<List<Badge>>> = flow {
        emit(ApiResult.Loading())
        
        try {
            val response = badgeApiService.getBadges()
            if (response.isSuccessful && response.body() != null) {
                val badges = response.body()!!.badgeSystem
                emit(ApiResult.Success(badges))
            } else {
                emit(ApiResult.Error("Failed to load badges: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get badges that user has earned based on their star count
     */
    fun getEarnedBadges(userStars: Int): Flow<ApiResult<List<Badge>>> = flow {
        emit(ApiResult.Loading())
        
        try {
            val response = badgeApiService.getBadges()
            if (response.isSuccessful && response.body() != null) {
                val badges = response.body()!!.badgeSystem
                val earnedBadges = badges.filter { it.isEarned(userStars) }
                emit(ApiResult.Success(earnedBadges))
            } else {
                emit(ApiResult.Error("Failed to load badges: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get next badge user can earn
     */
    fun getNextBadge(userStars: Int): Flow<ApiResult<Badge?>> = flow {
        emit(ApiResult.Loading())
        
        try {
            val response = badgeApiService.getBadges()
            if (response.isSuccessful && response.body() != null) {
                val badges = response.body()!!.badgeSystem
                val nextBadge = badges
                    .filter { !it.isEarned(userStars) }
                    .minByOrNull { it.minStars }
                emit(ApiResult.Success(nextBadge))
            } else {
                emit(ApiResult.Error("Failed to load badges: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
} 