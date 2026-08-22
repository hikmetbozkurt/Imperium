package com.hikmet.imperium.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.domain.repository.ProgressAnalyticsRepository
import com.hikmet.imperium.domain.repository.ProgressOverview
import com.hikmet.imperium.ui.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    repository: ProgressAnalyticsRepository,
    val soundManager: SoundManager,
) : ViewModel() {
    val progress: StateFlow<ProgressOverview?> = repository.observeProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
