package com.hikmet.imperium.retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor(
    private val badgeRepository: BadgeRepository,
) : ViewModel() {
    private val _badgeState = MutableStateFlow<ApiResult<List<Badge>>>(ApiResult.Loading())
    val badgeState: StateFlow<ApiResult<List<Badge>>> = _badgeState.asStateFlow()
    private var refreshJob: Job? = null

    fun refreshBadges(@Suppress("UNUSED_PARAMETER") userStars: Int) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _badgeState.value = ApiResult.Loading()
            _badgeState.value = runCatching { badgeRepository.fetchBadges() }
                .fold(
                    onSuccess = { ApiResult.Success(it) },
                    onFailure = { ApiResult.Error(it.message ?: "Badges could not be loaded") },
                )
        }
    }
}
