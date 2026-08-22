package com.hikmet.imperium.feature.levels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.repository.CategoryProgress
import com.hikmet.imperium.domain.repository.GameProgressRepository
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class LevelsScreenState(
    val category: HistoryCategory? = null,
    val progress: CategoryProgress? = null,
    val error: String? = null,
)

@HiltViewModel
class LevelsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    contentRepository: HistoryContentRepository,
    progressRepository: GameProgressRepository,
) : ViewModel() {
    private val categoryId = savedStateHandle.get<String>("categoryId")
        ?.let(CategoryId::from)
    private val category = categoryId?.let(contentRepository::category)

    val state: StateFlow<LevelsScreenState> = if (categoryId == null || category == null) {
        flowOf(LevelsScreenState(error = "Category could not be found"))
    } else {
        progressRepository.observeCategory(categoryId).map { progress ->
            LevelsScreenState(category = category, progress = progress)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LevelsScreenState(category = category),
    )
}
