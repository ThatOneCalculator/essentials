package com.sameerasw.essentials.viewmodels

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.sameerasw.essentials.services.CaffeinateWakeLockService

class CaffeinateViewModel : ViewModel() {
    val isActive = mutableStateOf(false)
    val postNotificationsGranted = mutableStateOf(false)
    val batteryOptimizationGranted = mutableStateOf(false)
    val abortWithScreenOff = mutableStateOf(true)

    fun check(context: Context) {
        isActive.value = isWakeLockServiceRunning(context)
        postNotificationsGranted.value = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        batteryOptimizationGranted.value = powerManager.isIgnoringBatteryOptimizations(context.packageName)

        val prefs = context.getSharedPreferences("caffeinate_prefs", Context.MODE_PRIVATE)
        abortWithScreenOff.value = prefs.getBoolean("abort_screen_off", true)
    }

    fun setAbortWithScreenOff(value: Boolean, context: Context) {
        val prefs = context.getSharedPreferences("caffeinate_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("abort_screen_off", value).apply()
        abortWithScreenOff.value = value
        
        // Notify service if it's running
        if (isActive.value) {
            val intent = Intent(context, CaffeinateWakeLockService::class.java).apply {
                action = "UPDATE_PREFS"
            }
            context.startService(intent)
        }
    }

    fun toggle(context: Context) {
        if (isActive.value) {
            // Stop service
            context.stopService(Intent(context, CaffeinateWakeLockService::class.java))
            isActive.value = false
        } else {
            // Start service
            ContextCompat.startForegroundService(context, Intent(context, CaffeinateWakeLockService::class.java))
            isActive.value = true
        }
    }

    private fun isWakeLockServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (CaffeinateWakeLockService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
}