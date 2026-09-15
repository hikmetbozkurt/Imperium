package com.hikmet.imperium.retrofit

import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BadgeRepository @Inject constructor(
    private val badgeApiService: BadgeApiService,
) {
    suspend fun fetchBadges(): List<Badge> {
        val response = badgeApiService.getBadges()
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.badgeSystem
            ?: error("Badge service returned an empty response")
    }
}
