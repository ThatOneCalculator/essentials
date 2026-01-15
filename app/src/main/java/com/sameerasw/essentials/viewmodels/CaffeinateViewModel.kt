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

    fun check(context: Context) {
        isActive.value = isWakeLockServiceRunning(context)
        postNotificationsGranted.value = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        batteryOptimizationGranted.value = powerManager.isIgnoringBatteryOptimizations(context.packageName)
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