package com.sameerasw.essentials

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.sameerasw.essentials.ui.composables.NetworkType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StatusBarIconViewModel : ViewModel() {
    val isWriteSecureSettingsEnabled = mutableStateOf(false)
    val isMobileDataVisible = mutableStateOf(true)
    val isWiFiVisible = mutableStateOf(false)
    val isSmartWiFiEnabled = mutableStateOf(false)
    val isSmartDataEnabled = mutableStateOf(false)
    val selectedNetworkTypes = mutableStateOf(setOf(NetworkType.NETWORK_4G, NetworkType.NETWORK_5G))

    private var updateJob: Job? = null
    private var smartWifiJob: Job? = null
    private var smartDataJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    companion object {
        const val ICON_BLACKLIST_SETTING = "icon_blacklist"
        const val BASE_BLACKLIST = "rotate,vowifi,ims,nfc,vpn,volte,alarm_clock,headset,hotspot,bluetooth,ims_volte,vpn"
        const val PREF_SMART_WIFI_ENABLED = "smart_wifi_enabled"
        const val PREF_SMART_DATA_ENABLED = "smart_data_enabled"
        const val PREF_SELECTED_NETWORK_TYPES = "selected_network_types"
    }

    fun check(context: Context) {
        isWriteSecureSettingsEnabled.value = canWriteSecureSettings(context)
        loadIconVisibilityState(context)
        loadSmartWiFiPref(context)
        loadSmartDataPref(context)
        loadSelectedNetworkTypes(context)

        if (isSmartWiFiEnabled.value && isWriteSecureSettingsEnabled.value) {
            startSmartWiFiUpdates(context)
        }

        if (isSmartDataEnabled.value && isWriteSecureSettingsEnabled.value) {
            startSmartDataUpdates(context)
        }
    }

    fun setMobileDataVisible(visible: Boolean, context: Context) {
        isMobileDataVisible.value = visible
        context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE).edit {
            putBoolean("icon_mobile_visible", visible)
        }
        updateIconBlacklist(context)
    }

    fun setWiFiVisible(visible: Boolean, context: Context) {
        isWiFiVisible.value = visible
        context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE).edit {
            putBoolean("icon_wifi_visible", visible)
        }
        updateIconBlacklist(context)
    }

    fun setSmartWiFiEnabled(enabled: Boolean, context: Context) {
        isSmartWiFiEnabled.value = enabled
        context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE).edit {
            putBoolean(PREF_SMART_WIFI_ENABLED, enabled)
        }

        if (enabled && isWriteSecureSettingsEnabled.value) {
            startSmartWiFiUpdates(context)
        } else {
            smartWifiJob?.cancel()
            // When disabling smart WiFi, restore manual settings
            updateIconBlacklist(context)
        }
    }

    fun setSmartDataEnabled(enabled: Boolean, context: Context) {
        isSmartDataEnabled.value = enabled
        context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE).edit {
            putBoolean(PREF_SMART_DATA_ENABLED, enabled)
        }

        if (enabled && isWriteSecureSettingsEnabled.value) {
            startSmartDataUpdates(context)
        } else {
            smartDataJob?.cancel()
            updateIconBlacklist(context)
        }

        // Update the network types based on Smart Data setting
        updateSelectedNetworkTypes(context, enabled)
    }

    fun updateSelectedNetworkTypes(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        val currentTypes = prefs.getStringSet(PREF_SELECTED_NETWORK_TYPES, setOf(NetworkType.NETWORK_4G.name, NetworkType.NETWORK_5G.name))?.toMutableSet() ?: mutableSetOf()

        if (enabled) {
            // Add 5G and 4G to selected types if not already present
            currentTypes.add(NetworkType.NETWORK_5G.name)
            currentTypes.add(NetworkType.NETWORK_4G.name)
        } else {
            // Remove 5G and 4G from selected types
            currentTypes.remove(NetworkType.NETWORK_5G.name)
            currentTypes.remove(NetworkType.NETWORK_4G.name)
        }

        selectedNetworkTypes.value = currentTypes.map { NetworkType.valueOf(it) }.toSet()

        context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE).edit {
            putStringSet(PREF_SELECTED_NETWORK_TYPES, currentTypes)
        }
    }

    private fun updateIconBlacklist(context: Context) {
        if (!isWriteSecureSettingsEnabled.value) return

        val blacklistItems = BASE_BLACKLIST.split(",").toMutableList()

        // Add or remove mobile from blacklist based on visibility
        if (!isMobileDataVisible.value && !blacklistItems.contains("mobile")) {
            blacklistItems.add("mobile")
        } else if (isMobileDataVisible.value) {
            blacklistItems.remove("mobile")
        }

        // Add or remove wifi from blacklist based on visibility
        if (!isWiFiVisible.value && !blacklistItems.contains("wifi")) {
            blacklistItems.add("wifi")
        } else if (isWiFiVisible.value) {
            blacklistItems.remove("wifi")
        }

        val newBlacklist = blacklistItems.joinToString(",")

        try {
            Settings.Secure.putString(context.contentResolver, ICON_BLACKLIST_SETTING, newBlacklist)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startSmartWiFiUpdates(context: Context) {
        smartWifiJob?.cancel()
        smartWifiJob = scope.launch {
            while (true) {
                val isWifiConnected = isWifiConnected(context)
                updateSmartWiFiBlacklist(context, isWifiConnected)
                delay(1000) // Check every second
            }
        }
    }

    private fun updateSmartWiFiBlacklist(context: Context, wifiConnected: Boolean) {
        if (!isSmartWiFiEnabled.value || !isWriteSecureSettingsEnabled.value) return

        val blacklistItems = BASE_BLACKLIST.split(",").toMutableList()

        // Handle WiFi visibility
        if (!isWiFiVisible.value && !blacklistItems.contains("wifi")) {
            blacklistItems.add("wifi")
        } else if (isWiFiVisible.value) {
            blacklistItems.remove("wifi")
        }

        // Handle Mobile Data visibility with Smart WiFi logic
        if (wifiConnected) {
            // WiFi is connected - hide mobile data if Smart WiFi is enabled
            if (!blacklistItems.contains("mobile")) {
                blacklistItems.add("mobile")
            }
        } else {
            // WiFi is not connected - show mobile data only if it's enabled manually
            if (isMobileDataVisible.value) {
                blacklistItems.remove("mobile")
            } else if (!blacklistItems.contains("mobile")) {
                blacklistItems.add("mobile")
            }
        }

        val newBlacklist = blacklistItems.joinToString(",")

        try {
            Settings.Secure.putString(context.contentResolver, ICON_BLACKLIST_SETTING, newBlacklist)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startSmartDataUpdates(context: Context) {
        smartDataJob?.cancel()
        smartDataJob = scope.launch {
            while (true) {
                val currentNetworkType = getCurrentNetworkType(context)
                updateSmartDataBlacklist(context, currentNetworkType)
                delay(10000) // Check every 10 seconds for network changes
            }
        }
    }

    private fun updateSmartDataBlacklist(context: Context, networkType: NetworkType) {
        if (!isSmartDataEnabled.value || !isWriteSecureSettingsEnabled.value || !isMobileDataVisible.value) {
            return
        }

        val blacklistItems = BASE_BLACKLIST.split(",").toMutableList()

        // Handle WiFi visibility
        if (!isWiFiVisible.value && !blacklistItems.contains("wifi")) {
            blacklistItems.add("wifi")
        } else if (isWiFiVisible.value) {
            blacklistItems.remove("wifi")
        }

        // Handle Mobile Data visibility with Smart Data logic
        val shouldHideMobileData = selectedNetworkTypes.value.contains(networkType) ||
            (selectedNetworkTypes.value.contains(NetworkType.NETWORK_OTHER) &&
             !setOf(NetworkType.NETWORK_5G, NetworkType.NETWORK_4G, NetworkType.NETWORK_3G).contains(networkType))

        if (shouldHideMobileData) {
            // Hide mobile data
            if (!blacklistItems.contains("mobile")) {
                blacklistItems.add("mobile")
            }
        } else {
            // Show mobile data (only if manually enabled)
            if (isMobileDataVisible.value) {
                blacklistItems.remove("mobile")
            }
        }

        val newBlacklist = blacklistItems.joinToString(",")

        try {
            Settings.Secure.putString(context.contentResolver, ICON_BLACKLIST_SETTING, newBlacklist)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCurrentNetworkType(context: Context): NetworkType {
        return try {
            // Check if we have READ_PHONE_STATE permission
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_PHONE_STATE
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                return NetworkType.NETWORK_OTHER
            }

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return NetworkType.NETWORK_OTHER
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NETWORK_OTHER

            // If it's WiFi, return OTHER
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return NetworkType.NETWORK_OTHER
            }

            // For cellular networks, use TelephonyManager to get detailed network type
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            @Suppress("DEPRECATION")
            val networkType = telephonyManager.networkType

            when (networkType) {
                // 5G networks
                TelephonyManager.NETWORK_TYPE_NR -> NetworkType.NETWORK_5G

                // 4G networks
                TelephonyManager.NETWORK_TYPE_LTE,
                TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkType.NETWORK_4G

                // 3G networks
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkType.NETWORK_3G

                // Everything else is OTHER
                else -> NetworkType.NETWORK_OTHER
            }
        } catch (e: Exception) {
            NetworkType.NETWORK_OTHER
        }
    }

    private fun isWifiConnected(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } catch (e: Exception) {
            false
        }
    }

    private fun loadIconVisibilityState(context: Context) {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        isMobileDataVisible.value = prefs.getBoolean("icon_mobile_visible", true)
        isWiFiVisible.value = prefs.getBoolean("icon_wifi_visible", false)
    }

    private fun loadSmartWiFiPref(context: Context) {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        isSmartWiFiEnabled.value = prefs.getBoolean(PREF_SMART_WIFI_ENABLED, false)
    }

    private fun loadSmartDataPref(context: Context) {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        isSmartDataEnabled.value = prefs.getBoolean(PREF_SMART_DATA_ENABLED, false)
    }

    private fun loadSelectedNetworkTypes(context: Context) {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        val currentTypes = prefs.getStringSet(PREF_SELECTED_NETWORK_TYPES, setOf(NetworkType.NETWORK_4G.name, NetworkType.NETWORK_5G.name)) ?: setOf()

        selectedNetworkTypes.value = currentTypes.map { NetworkType.valueOf(it) }.toSet()
    }

    private fun canWriteSecureSettings(context: Context): Boolean {
        return try {
            val currentValue = Settings.Secure.getString(
                context.contentResolver,
                ICON_BLACKLIST_SETTING
            )
            // Try to write the same value back (no-op) to verify permission
            Settings.Secure.putString(
                context.contentResolver,
                ICON_BLACKLIST_SETTING,
                currentValue ?: "$BASE_BLACKLIST,mobile,wifi"
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAdbCommand(): String {
        return "adb shell pm grant com.sameerasw.essentials android.permission.WRITE_SECURE_SETTINGS"
    }

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
        smartWifiJob?.cancel()
        smartDataJob?.cancel()
    }
}
