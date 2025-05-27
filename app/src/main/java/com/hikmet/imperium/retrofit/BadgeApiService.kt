package com.hikmet.imperium.retrofit

import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit API service for fetching badge data
 * Connects to the JSON endpoint containing badge information
 */
interface BadgeApiService {
    
    /**
     * Fetch all available badges from the JSON endpoint
     * @return Response containing badge response with badge system
     */
    @GET("b/PPQ1")
    suspend fun getBadges(): Response<BadgeResponse>
}

/**
 * API response wrapper for error handling
 */
sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String) : ApiResult<T>()
    data class Loading<T>(val isLoading: Boolean = true) : ApiResult<T>()
} 