package com.hikmet.imperium.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds

/**
 * Utility class to manage quiz timer countdown
 */
class QuizTimer(
    private val initialTimeMs: Long,
    private val onTick: (Long) -> Unit,
    private val onFinish: () -> Unit
) {
    private var remainingTime = initialTimeMs
    private var isRunning = false
    
    /**
     * Start the timer
     */
    fun start() {
        isRunning = true
    }
    
    /**
     * Pause the timer
     */
    fun pause() {
        isRunning = false
    }
    
    /**
     * Reset the timer
     */
    fun reset() {
        isRunning = false
        remainingTime = initialTimeMs
    }
    
    /**
     * Check if the timer is running
     */
    fun isRunning(): Boolean = isRunning
    
    /**
     * Get the remaining time
     */
    fun getRemainingTimeMs(): Long = remainingTime
    
    /**
     * Tick function to update time
     */
    fun tick(deltaMs: Long) {
        if (!isRunning) return
        
        remainingTime -= deltaMs
        
        if (remainingTime <= 0) {
            remainingTime = 0
            isRunning = false
            onFinish()
        } else {
            onTick(remainingTime)
        }
    }
}

/**
 * Composable function to use the quiz timer
 */
@Composable
fun rememberQuizTimer(
    initialTimeMs: Long,
    isStarted: Boolean,
    onTick: (Long) -> Unit,
    onFinish: () -> Unit
): State<Long> {
    // Create timer
    val timer = remember {
        QuizTimer(
            initialTimeMs = initialTimeMs,
            onTick = onTick,
            onFinish = onFinish
        )
    }
    
    // State for the timer
    val timerState = remember { mutableLongStateOf(initialTimeMs) }
    
    // Update timer state based on isStarted
    LaunchedEffect(isStarted) {
        if (isStarted) {
            timer.start()
        } else {
            timer.pause()
        }
    }
    
    // Timer effect
    LaunchedEffect(timer, isStarted) {
        if (isStarted) {
            while (isActive && timer.isRunning()) {
                val before = System.currentTimeMillis()
                delay(100.milliseconds) // Update every 100ms
                val after = System.currentTimeMillis()
                
                // Calculate real elapsed time
                val delta = after - before
                timer.tick(delta)
                
                // Update state
                timerState.longValue = timer.getRemainingTimeMs()
            }
        }
    }
    
    // Reset timer when disposed
    DisposableEffect(Unit) {
        onDispose {
            timer.reset()
        }
    }
    
    return timerState
}

/**
 * Format time in milliseconds to MM:SS
 */
fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = (totalSeconds / 60).toInt()
    val seconds = (totalSeconds % 60).toInt()
    
    return String.format("%02d:%02d", minutes, seconds)
} 