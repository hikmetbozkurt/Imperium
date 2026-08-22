package com.hikmet.imperium.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.domain.repository.ProgressAnalyticsRepository
import com.hikmet.imperium.domain.repository.ProgressOverview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ProgressScreenState(
    val overview: ProgressOverview? = null,
    val isLoading: Boolean = true,
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    repository: ProgressAnalyticsRepository,
) : ViewModel() {
    val state: StateFlow<ProgressScreenState> = repository.observeProgress()
        .map { ProgressScreenState(overview = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProgressScreenState(),
        )
}
