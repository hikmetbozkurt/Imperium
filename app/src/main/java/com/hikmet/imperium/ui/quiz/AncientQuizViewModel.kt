package com.hikmet.imperium.ui.quiz

import androidx.lifecycle.ViewModel
import com.hikmet.imperium.ui.util.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AncientQuizViewModel @Inject constructor(
    val soundManager: SoundManager
) : ViewModel() {
    
    fun playCorrectAnswer() {
        soundManager.playCorrectAnswer()
    }
    
    fun playWrongAnswer() {
        soundManager.playWrongAnswer() 
    }
    
    fun playLevelComplete() {
        soundManager.playLevelComplete()
    }
} 