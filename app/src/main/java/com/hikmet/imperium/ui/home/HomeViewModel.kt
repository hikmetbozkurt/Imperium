package com.hikmet.imperium.ui.home

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeCategory(
    val content: HistoryCategory,
    val progress: CategoryProgress,
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val categories: List<HomeCategory> = emptyList(),
    val featuredQuote: HistoricalQuote = HistoricalQuotes.quoteAt(0),
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    contentRepository: HistoryContentRepository,
    progressRepository: GameProgressRepository,
    private val savedStateHandle: SavedStateHandle,
    val soundManager: SoundManager,
) : ViewModel() {
    private val categories = contentRepository.categories()
    private val progressFlows = categories.map { progressRepository.observeCategory(it.id) }

    private val categoryState = if (progressFlows.isEmpty()) {
        flowOf(emptyList())
    } else {
        combine(progressFlows) { progress ->
            categories.zip(progress.toList()).map { (content, categoryProgress) ->
                HomeCategory(content, categoryProgress)
            }
        }
    }
    private val quoteIndex = savedStateHandle.getStateFlow(
        CURRENT_QUOTE_INDEX_KEY,
        HistoricalQuotes.randomIndex(),
    )

    val state: StateFlow<HomeUiState> = combine(categoryState, quoteIndex) { categoryItems, index ->
        HomeUiState(
            isLoading = false,
            categories = categoryItems,
            featuredQuote = HistoricalQuotes.quoteAt(index),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(featuredQuote = HistoricalQuotes.quoteAt(quoteIndex.value)),
    )

    fun showNextQuote() {
        savedStateHandle[CURRENT_QUOTE_INDEX_KEY] = HistoricalQuotes.nextIndex(quoteIndex.value)
    }

    private companion object {
        const val CURRENT_QUOTE_INDEX_KEY = "home.currentQuoteIndex"
    }
}
