package com.hikmet.imperium.ui.results

import androidx.lifecycle.ViewModel
import com.hikmet.imperium.ui.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModernHistoryResultsViewModel @Inject constructor(
    val soundManager: SoundManager
) : ViewModel() {
    
    override fun onCleared() {
        super.onCleared()
        // Don't release the sound manager here as it's a singleton
        // and might be used by other screens
    }
} 