package com.hikmet.imperium.feature.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.model.QuizResult
import com.hikmet.imperium.domain.repository.GameProgressRepository
import com.hikmet.imperium.domain.repository.HistoryContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ResultScreenState(
    val attemptId: Long? = null,
    val result: QuizResult? = null,
    val category: HistoryCategory? = null,
    val isNewBest: Boolean = false,
    val addedStars: Int = 0,
    val newlyUnlockedLevel: Int? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    progressRepository: GameProgressRepository,
    contentRepository: HistoryContentRepository,
) : ViewModel() {
    private val attemptId = savedStateHandle.get<Long>("attemptId")

    val state: StateFlow<ResultScreenState> = if (attemptId == null) {
        flowOf(ResultScreenState(isLoading = false, error = "Result could not be found"))
    } else {
        progressRepository.observeRecordedAttempt(attemptId).map { recorded ->
            if (recorded == null) {
                ResultScreenState(isLoading = false, error = "Result could not be found")
            } else {
                ResultScreenState(
                    attemptId = recorded.id,
                    result = recorded.result,
                    category = contentRepository.category(recorded.result.categoryId),
                    isNewBest = recorded.isNewBest,
                    addedStars = recorded.addedStars,
                    newlyUnlockedLevel = recorded.newlyUnlockedLevel,
                    isLoading = false,
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResultScreenState(),
    )
}
