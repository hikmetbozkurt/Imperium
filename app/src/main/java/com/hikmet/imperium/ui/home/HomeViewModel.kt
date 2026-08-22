package com.hikmet.imperium.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.repository.CategoryProgress
import com.hikmet.imperium.domain.repository.GameProgressRepository
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import com.hikmet.imperium.ui.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeCategory(
    val content: HistoryCategory,
    val progress: CategoryProgress,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    contentRepository: HistoryContentRepository,
    progressRepository: GameProgressRepository,
    val soundManager: SoundManager,
) : ViewModel() {
    private val categories = contentRepository.categories()
    private val progressFlows = categories.map { progressRepository.observeCategory(it.id) }

    val categoriesState: StateFlow<List<HomeCategory>> = combine(progressFlows) { progress ->
        categories.zip(progress.toList()).map { (content, categoryProgress) ->
            HomeCategory(content, categoryProgress)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )
}
