package com.hikmet.imperium.retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing badge data in Profile screen
 */
@HiltViewModel
class BadgeViewModel @Inject constructor(
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _badgeState = MutableStateFlow<ApiResult<List<Badge>>>(ApiResult.Loading())
    val badgeState: StateFlow<ApiResult<List<Badge>>> = _badgeState.asStateFlow()

    private val _earnedBadgesState = MutableStateFlow<ApiResult<List<Badge>>>(ApiResult.Loading())
    val earnedBadgesState: StateFlow<ApiResult<List<Badge>>> = _earnedBadgesState.asStateFlow()

    private val _nextBadgeState = MutableStateFlow<ApiResult<Badge?>>(ApiResult.Loading())
    val nextBadgeState: StateFlow<ApiResult<Badge?>> = _nextBadgeState.asStateFlow()

    /**
     * Load all badges
     */
    fun loadBadges() {
        viewModelScope.launch {
            badgeRepository.getBadges().collect { result ->
                _badgeState.value = result
            }
        }
    }

    /**
     * Load badges earned by user based on their star count
     */
    fun loadEarnedBadges(userStars: Int) {
        viewModelScope.launch {
            badgeRepository.getEarnedBadges(userStars).collect { result ->
                _earnedBadgesState.value = result
            }
        }
    }

    /**
     * Load next badge user can earn
     */
    fun loadNextBadge(userStars: Int) {
        viewModelScope.launch {
            badgeRepository.getNextBadge(userStars).collect { result ->
                _nextBadgeState.value = result
            }
        }
    }

    /**
     * Refresh all badge data
     */
    fun refreshBadges(userStars: Int) {
        loadBadges()
        loadEarnedBadges(userStars)
        loadNextBadge(userStars)
    }
} 