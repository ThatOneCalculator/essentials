package com.sameerasw.essentials.utils

import android.content.Context
import android.view.View
import androidx.compose.runtime.mutableStateOf

/**
 * Centralized haptic feedback utility that can be toggled on/off app-wide.
 * Controls in-app UI haptics (not widget haptics).
 */
object HapticUtil {
    // Mutable state to track if in-app haptics are enabled
    val isAppHapticsEnabled = mutableStateOf(true)

    /**
     * Perform a UI interaction haptic feedback (light tick)
     * Only performs if app haptics are enabled
     */
    fun performUIHaptic(view: View) {
        if (!isAppHapticsEnabled.value) return
        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
    }


    fun performSliderHaptic(view: View) {
        if (!isAppHapticsEnabled.value) return
        view.performHapticFeedback(android.view.HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
    }

    /**
     * Perform a virtual key haptic feedback (stronger)
     * Only performs if app haptics are enabled
     */
    fun performVirtualKeyHaptic(view: View) {
        if (!isAppHapticsEnabled.value) return
        view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
    }

    /**
     * Load app haptic preference from SharedPreferences
     */
    fun loadAppHapticsEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("app_haptics_enabled", true) // Default: enabled
    }

    /**
     * Save app haptic preference to SharedPreferences
     */
    fun saveAppHapticsEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("app_haptics_enabled", enabled).apply()
        isAppHapticsEnabled.value = enabled
    }

    /**
     * Initialize haptic setting from SharedPreferences
     */
    fun initialize(context: Context) {
        isAppHapticsEnabled.value = loadAppHapticsEnabled(context)
    }
}

