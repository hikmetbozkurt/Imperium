package com.hikmet.imperium.ui.util

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.util.Log
import androidx.annotation.RawRes
import com.hikmet.imperium.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sound Manager for handling all audio effects in the Imperium app
 * Manages MediaPlayer instances and provides easy-to-use sound playing methods
 */
@Singleton
class SoundManager @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val TAG = "SoundManager"
        private const val MAX_VOLUME = 1.0f
        private const val DEFAULT_VOLUME = 0.7f
        private const val PREFS_NAME = "imperium_sound_prefs"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_MUSIC_ENABLED = "music_enabled"
        private const val KEY_SOUND_VOLUME = "sound_volume"
        private const val KEY_MUSIC_VOLUME = "music_volume"
    }
    
    // SharedPreferences for persistent settings
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    // MediaPlayer instances for different sounds
    private var correctAnswerPlayer: MediaPlayer? = null
    private var wrongAnswerPlayer: MediaPlayer? = null
    private var levelCompletePlayer: MediaPlayer? = null
    private var buttonClickPlayer: MediaPlayer? = null
    private var winJinglePlayer: MediaPlayer? = null
    private var backgroundMusicPlayer: MediaPlayer? = null // New background music player
    
    // Volume control - load from SharedPreferences
    private var soundEnabled = prefs.getBoolean(KEY_SOUND_ENABLED, true)
    private var currentVolume = prefs.getFloat(KEY_SOUND_VOLUME, DEFAULT_VOLUME)
    private var musicEnabled = prefs.getBoolean(KEY_MUSIC_ENABLED, true) // Separate control for background music
    private var musicVolume = prefs.getFloat(KEY_MUSIC_VOLUME, 0.3f) // Lower volume for background music
    
    init {
        initializeSounds()
    }
    
    /**
     * Initialize all sound players
     */
    private fun initializeSounds() {
        try {
            // Correct answer sound - "That's right!"
            correctAnswerPlayer = MediaPlayer.create(
                context, 
                R.raw.correct_answer
            )?.apply {
                setVolume(currentVolume, currentVolume)
            }
            
            // Wrong answer sound - "That's wrong!"
            wrongAnswerPlayer = MediaPlayer.create(
                context, 
                R.raw.wrong_answer
            )?.apply {
                setVolume(currentVolume, currentVolume)
            }
            
            // Level complete sound - Hawaii game complete ding
            levelCompletePlayer = MediaPlayer.create(
                context, 
                R.raw.level_complete
            )?.apply {
                setVolume(currentVolume, currentVolume)
            }
            
            // Button click sound - Vending machine beep
            buttonClickPlayer = MediaPlayer.create(
                context, 
                R.raw.button_click
            )?.apply {
                setVolume(currentVolume, currentVolume)
            }
            
            // Win jingle sound - Synth win sound
            winJinglePlayer = MediaPlayer.create(
                context, 
                R.raw.jingle_win_synth
            )?.apply {
                setVolume(currentVolume, currentVolume)
            }
            
            // Background music
            backgroundMusicPlayer = MediaPlayer.create(
                context, 
                R.raw.main_theme
            )?.apply {
                setVolume(musicVolume, musicVolume)
                isLooping = true // Loop the background music
            }
            
            Log.d(TAG, "Sound Manager initialized successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing sounds: ${e.message}")
        }
    }
    
    /**
     * Get resource ID from file name (removes numbers and special characters)
     */
    private fun getResourceId(baseName: String): Int {
        return when {
            baseName.contains("correct_answer") -> R.raw.correct_answer
            baseName.contains("dats_wrong") -> R.raw.wrong_answer
            baseName.contains("hawaii_game_complete") -> R.raw.level_complete
            baseName.contains("vendingmachine") -> R.raw.button_click
            baseName.contains("jingle_win") -> R.raw.jingle_win_synth
            else -> R.raw.correct_answer // Default fallback
        }
    }
    
    /**
     * Play correct answer sound
     */
    fun playCorrectAnswer() {
        if (soundEnabled) {
            try {
                correctAnswerPlayer?.let { player ->
                    if (player.isPlaying) {
                        player.seekTo(0)
                    } else {
                        player.start()
                    }
                }
                Log.d(TAG, "Playing correct answer sound")
            } catch (e: Exception) {
                Log.e(TAG, "Error playing correct answer sound: ${e.message}")
            }
        }
    }
    
    /**
     * Play wrong answer sound
     */
    fun playWrongAnswer() {
        if (soundEnabled) {
            try {
                wrongAnswerPlayer?.let { player ->
                    if (player.isPlaying) {
                        player.seekTo(0)
                    } else {
                        player.start()
                    }
                }
                Log.d(TAG, "Playing wrong answer sound")
            } catch (e: Exception) {
                Log.e(TAG, "Error playing wrong answer sound: ${e.message}")
            }
        }
    }
    
    /**
     * Play level complete sound
     */
    fun playLevelComplete() {
        if (soundEnabled) {
            try {
                levelCompletePlayer?.let { player ->
                    if (player.isPlaying) {
                        player.seekTo(0)
                    } else {
                        player.start()
                    }
                }
                Log.d(TAG, "Playing level complete sound")
            } catch (e: Exception) {
                Log.e(TAG, "Error playing level complete sound: ${e.message}")
            }
        }
    }
    
    /**
     * Play button click sound
     */
    fun playButtonClick() {
        if (soundEnabled) {
            try {
                buttonClickPlayer?.let { player ->
                    if (player.isPlaying) {
                        player.seekTo(0)
                    } else {
                        player.start()
                    }
                }
                Log.d(TAG, "Playing button click sound")
            } catch (e: Exception) {
                Log.e(TAG, "Error playing button click sound: ${e.message}")
            }
        }
    }
    
    /**
     * Play win jingle sound (for high scores or achievements)
     */
    fun playWinJingle() {
        if (soundEnabled) {
            try {
                winJinglePlayer?.let { player ->
                    if (player.isPlaying) {
                        player.seekTo(0)
                    } else {
                        player.start()
                    }
                }
                Log.d(TAG, "Playing win jingle sound")
            } catch (e: Exception) {
                Log.e(TAG, "Error playing win jingle sound: ${e.message}")
            }
        }
    }
    
    /**
     * Start background music (for home screen)
     */
    fun startBackgroundMusic() {
        if (musicEnabled) {
            try {
                backgroundMusicPlayer?.let { player ->
                    if (!player.isPlaying) {
                        player.start()
                        Log.d(TAG, "Background music started")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting background music: ${e.message}")
            }
        }
    }
    
    /**
     * Stop background music
     */
    fun stopBackgroundMusic() {
        try {
            backgroundMusicPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    player.seekTo(0) // Reset to beginning
                    Log.d(TAG, "Background music stopped")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping background music: ${e.message}")
        }
    }
    
    /**
     * Pause background music (without resetting position)
     */
    fun pauseBackgroundMusic() {
        try {
            backgroundMusicPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    Log.d(TAG, "Background music paused")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error pausing background music: ${e.message}")
        }
    }
    
    /**
     * Resume background music
     */
    fun resumeBackgroundMusic() {
        if (musicEnabled) {
            try {
                backgroundMusicPlayer?.let { player ->
                    if (!player.isPlaying) {
                        player.start()
                        Log.d(TAG, "Background music resumed")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error resuming background music: ${e.message}")
            }
        }
    }
    
    /**
     * Enable or disable all sounds
     */
    fun setSoundEnabled(enabled: Boolean) {
        soundEnabled = enabled
        // Save to SharedPreferences
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
        Log.d(TAG, "Sound ${if (enabled) "enabled" else "disabled"}")
    }
    
    /**
     * Enable or disable background music
     */
    fun setMusicEnabled(enabled: Boolean) {
        musicEnabled = enabled
        // Save to SharedPreferences
        prefs.edit().putBoolean(KEY_MUSIC_ENABLED, enabled).apply()
        if (!enabled) {
            stopBackgroundMusic()
        }
        Log.d(TAG, "Music ${if (enabled) "enabled" else "disabled"}")
    }
    
    /**
     * Set volume for all sound effects (0.0 to 1.0)
     */
    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0.0f, MAX_VOLUME)
        // Save to SharedPreferences
        prefs.edit().putFloat(KEY_SOUND_VOLUME, currentVolume).apply()
        
        correctAnswerPlayer?.setVolume(currentVolume, currentVolume)
        wrongAnswerPlayer?.setVolume(currentVolume, currentVolume)
        levelCompletePlayer?.setVolume(currentVolume, currentVolume)
        buttonClickPlayer?.setVolume(currentVolume, currentVolume)
        winJinglePlayer?.setVolume(currentVolume, currentVolume)
        
        Log.d(TAG, "Sound effects volume set to: $currentVolume")
    }
    
    /**
     * Set volume for background music (0.0 to 1.0)
     */
    fun setMusicVolume(volume: Float) {
        musicVolume = volume.coerceIn(0.0f, MAX_VOLUME)
        // Save to SharedPreferences
        prefs.edit().putFloat(KEY_MUSIC_VOLUME, musicVolume).apply()
        backgroundMusicPlayer?.setVolume(musicVolume, musicVolume)
        Log.d(TAG, "Music volume set to: $musicVolume")
    }
    
    /**
     * Check if sounds are enabled
     */
    fun isSoundEnabled(): Boolean = soundEnabled
    
    /**
     * Check if music is enabled
     */
    fun isMusicEnabled(): Boolean = musicEnabled
    
    /**
     * Get current sound effects volume
     */
    fun getCurrentVolume(): Float = currentVolume
    
    /**
     * Get current music volume
     */
    fun getCurrentMusicVolume(): Float = musicVolume
    
    /**
     * Check if background music is currently playing
     */
    fun isBackgroundMusicPlaying(): Boolean {
        return try {
            backgroundMusicPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Error checking background music status: ${e.message}")
            false
        }
    }
    
    /**
     * Release all MediaPlayer resources
     * Call this when the app is being destroyed
     */
    fun release() {
        try {
            correctAnswerPlayer?.release()
            wrongAnswerPlayer?.release()
            levelCompletePlayer?.release()
            buttonClickPlayer?.release()
            winJinglePlayer?.release()
            backgroundMusicPlayer?.release()
            
            correctAnswerPlayer = null
            wrongAnswerPlayer = null
            levelCompletePlayer = null
            buttonClickPlayer = null
            winJinglePlayer = null
            backgroundMusicPlayer = null
            
            Log.d(TAG, "Sound Manager resources released")
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing sound resources: ${e.message}")
        }
    }
    
    /**
     * Reinitialize sounds (useful after release or if sounds fail to load)
     */
    fun reinitialize() {
        release()
        initializeSounds()
    }
} 