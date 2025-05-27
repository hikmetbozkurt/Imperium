package com.hikmet.imperium.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hikmet.imperium.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {
    
    private val _currentTimeView = MutableStateFlow(TimeView.WEEKLY)
    val currentTimeView: StateFlow<TimeView> = _currentTimeView.asStateFlow()
    
    private val _chartData = MutableStateFlow<List<ProgressEntry>>(emptyList())
    val chartData: StateFlow<List<ProgressEntry>> = _chartData.asStateFlow()
    
    private val _detailedProgressData = MutableStateFlow<DetailedProgressData?>(null)
    val detailedProgressData: StateFlow<DetailedProgressData?> = _detailedProgressData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadProgressData()
    }
    
    fun onTimeViewChanged(newTimeView: TimeView) {
        _currentTimeView.value = newTimeView
        loadProgressData()
    }
    
    private fun loadProgressData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                val timeView = _currentTimeView.value
                
                // Load chart data
                val chartEntries = progressRepository.getProgressEntries(timeView)
                _chartData.value = chartEntries
                
                // Load detailed progress data
                val detailedData = progressRepository.getDetailedProgressData(timeView)
                _detailedProgressData.value = detailedData
                
            } catch (e: Exception) {
                // Handle error - show empty data for now
                _chartData.value = emptyList()
                _detailedProgressData.value = createEmptyProgressData()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun createEmptyProgressData(): DetailedProgressData {
        return DetailedProgressData(
            summaryMetrics = emptyList(),
            categoryBreakdown = emptyList(),
            averageScore = 0,
            bestScore = 0,
            improvement = 0,
            insights = listOf("Start playing quizzes to see your progress!")
        )
    }
} 