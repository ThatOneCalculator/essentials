package com.sameerasw.essentials

import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val isAccessibilityEnabled = mutableStateOf(false)

    fun check(context: Context) {
        isAccessibilityEnabled.value = isAccessibilityServiceEnabled(context)
    }

    private fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        val serviceName = "${context.packageName}/${ScreenOffAccessibilityService::class.java.name}"
        return enabledServices?.contains(serviceName) == true
    }
}
