package com.sameerasw.essentials.domain

import com.sameerasw.essentials.R

/**
 * Data class representing a status bar icon with all its variants across different OEMs
 * @param id Unique identifier for this icon type
 * @param displayName Display name for UI
 * @param blacklistNames List of blacklist names this icon can have across different ROMs/OEMs
 * @param defaultVisible Whether this icon should be visible by default
 * @param preferencesKey Key to store preference in SharedPreferences
 * @param category Category for UI grouping
 * @param iconRes Optional icon resource for UI
 */
data class StatusBarIcon(
    val id: String,
    val displayName: String,
    val blacklistNames: List<String>,
    val defaultVisible: Boolean = true,
    val preferencesKey: String = "icon_${id}_visible",
    val category: String = "Other",
    val iconRes: Int? = null
)

/**
 * Defines all supported status bar icons with their variants
 * This centralizes all icon configurations and reduces code duplication
 */
object StatusBarIconRegistry {

    const val CAT_CONNECTIVITY = "Connectivity"
    const val CAT_PHONE_NETWORK = "Phone & Network"
    const val CAT_AUDIO_MEDIA = "Audio & Media"
    const val CAT_SYSTEM_STATUS = "System Status"
    const val CAT_OEM_SPECIFIC = "OEM Specific"

    // Build the complete list of all supported icons
    val ALL_ICONS = listOf(
        // --- Connectivity ---
        StatusBarIcon(
            id = "wifi",
            displayName = "WiFi",
            blacklistNames = listOf("wifi", "wifi_oxygen", "wifi_p2p"),
            category = CAT_CONNECTIVITY,
            iconRes = R.drawable.rounded_android_wifi_3_bar_24
        ),
        StatusBarIcon(
            id = "bluetooth",
            displayName = "Bluetooth",
            blacklistNames = listOf("bluetooth", "bluetooth_handsfree_battery", "ble_unlock_mode"),
            category = CAT_CONNECTIVITY,
            iconRes = R.drawable.rounded_bluetooth_24
        ),
        StatusBarIcon(
            id = "nfc",
            displayName = "NFC / Felica",
            blacklistNames = listOf("nfc", "nfc_on", "nfclock", "felica_lock"),
            category = CAT_CONNECTIVITY,
            iconRes = R.drawable.rounded_nfc_24
        ),
        StatusBarIcon(
            id = "vpn",
            displayName = "VPN",
            blacklistNames = listOf("vpn"),
            category = CAT_CONNECTIVITY,
            iconRes = R.drawable.rounded_vpn_key_24
        ),
        StatusBarIcon(
            id = "airplane_mode",
            displayName = "Airplane Mode",
            blacklistNames = listOf("airplane", "airplane_mode"),
            category = CAT_CONNECTIVITY,
            iconRes = R.drawable.rounded_flight_24
        ),
        StatusBarIcon(
            id = "hotspot",
            displayName = "Hotspot",
            blacklistNames = listOf("hotspot", "wifi_ap"),
            category = CAT_CONNECTIVITY,
            iconRes = R.drawable.rounded_wifi_tethering_24
        ),
        StatusBarIcon(
            id = "cast",
            displayName = "Cast",
            blacklistNames = listOf("cast"),
            category = CAT_CONNECTIVITY,
            iconRes = R.drawable.rounded_cast_24
        ),

        // --- Phone & Network ---
        StatusBarIcon(
            id = "mobile_data",
            displayName = "Mobile Data",
            blacklistNames = listOf("mobile", "data_connection"),
            category = CAT_PHONE_NETWORK,
            iconRes = R.drawable.rounded_android_cell_dual_4_bar_24
        ),
        StatusBarIcon(
            id = "phone_signal",
            displayName = "Phone Signal",
            blacklistNames = listOf("phone_signal", "phone_signal_second_stub", "phone_evdo_signal", "cdma_eri", "wimax"),
            category = CAT_PHONE_NETWORK,
            iconRes = R.drawable.rounded_signal_cellular_alt_24
        ),
        StatusBarIcon(
            id = "volte",
            displayName = "VoLTE / VoNR",
            blacklistNames = listOf("volte", "ims_volte", "volte_call", "unicom_call"),
            category = CAT_PHONE_NETWORK,
            iconRes = R.drawable.rounded_wifi_calling_bar_3_24
        ),
        StatusBarIcon(
            id = "wifi_calling",
            displayName = "WiFi Calling / VoWiFi",
            blacklistNames = listOf("wifi_calling", "vowifi"),
            category = CAT_PHONE_NETWORK,
            iconRes = R.drawable.rounded_wifi_calling_bar_3_24
        ),
        StatusBarIcon(
            id = "remote_call",
            displayName = "Call Status / Sync",
            blacklistNames = listOf("remote_call", "call_record", "answering_memo", "missed_call"),
            category = CAT_PHONE_NETWORK,
            iconRes = R.drawable.rounded_call_log_24
        ),
        StatusBarIcon(
            id = "tty",
            displayName = "TTY",
            blacklistNames = listOf("tty"),
            category = CAT_PHONE_NETWORK,
            iconRes = R.drawable.rounded_settings_accessibility_24
        ),

        // --- Audio & Media ---
        StatusBarIcon(
            id = "volume",
            displayName = "Volume",
            blacklistNames = listOf("volume", "mute", "quiet"),
            category = CAT_AUDIO_MEDIA,
            iconRes = R.drawable.rounded_volume_up_24
        ),
        StatusBarIcon(
            id = "headset",
            displayName = "Headset",
            blacklistNames = listOf("headset", "earphone"),
            defaultVisible = false,
            category = CAT_AUDIO_MEDIA,
            iconRes = R.drawable.rounded_headset_mic_24
        ),
        StatusBarIcon(
            id = "speakerphone",
            displayName = "Speakerphone",
            blacklistNames = listOf("speakerphone"),
            category = CAT_AUDIO_MEDIA,
            iconRes = R.drawable.rounded_volume_up_24
        ),
        StatusBarIcon(
            id = "dmb",
            displayName = "DMB",
            blacklistNames = listOf("dmb"),
            category = CAT_AUDIO_MEDIA,
            iconRes = R.drawable.rounded_play_arrow_24
        ),

        // --- System Status ---
        StatusBarIcon(
            id = "clock",
            displayName = "Clock",
            blacklistNames = listOf("clock"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_nest_clock_farsight_analog_24
        ),
        StatusBarIcon(
            id = "ime",
            displayName = "Input Method (IME)",
            blacklistNames = listOf("ime"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_settings_accessibility_24
        ),
        StatusBarIcon(
            id = "alarm",
            displayName = "Alarm",
            blacklistNames = listOf("alarm", "alarm_clock"),
            defaultVisible = false,
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_alarm_24
        ),
        StatusBarIcon(
            id = "battery",
            displayName = "Battery",
            blacklistNames = listOf("battery"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_battery_android_frame_6_24
        ),
        StatusBarIcon(
            id = "power_saver",
            displayName = "Power Saving",
            blacklistNames = listOf("power_saver", "powersavingmode"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_battery_android_frame_plus_24
        ),
        StatusBarIcon(
            id = "data_saver",
            displayName = "Data Saver",
            blacklistNames = listOf("data_saver"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_data_saver_on_24
        ),
        StatusBarIcon(
            id = "rotate",
            displayName = "Rotation Lock",
            blacklistNames = listOf("rotate"),
            defaultVisible = false,
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_mobile_rotate_24
        ),
        StatusBarIcon(
            id = "location",
            displayName = "Location / GPS",
            blacklistNames = listOf("location", "gps", "lbs"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_navigation_24
        ),
        StatusBarIcon(
            id = "sync",
            displayName = "Sync",
            blacklistNames = listOf("sync_active", "sync_failing"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_sync_24
        ),
        StatusBarIcon(
            id = "managed_profile",
            displayName = "Managed Profile",
            blacklistNames = listOf("managed_profile"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_security_24
        ),
        StatusBarIcon(
            id = "dnd",
            displayName = "Do Not Disturb",
            blacklistNames = listOf("do_not_disturb", "dnd", "zen"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_do_not_disturb_on_24
        ),
        StatusBarIcon(
            id = "privacy",
            displayName = "Privacy & Secure Folder",
            blacklistNames = listOf("privacy_mode", "private_mode", "knox_container"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_security_24
        ),
        StatusBarIcon(
            id = "secure",
            displayName = "Security Status (SU)",
            blacklistNames = listOf("secure", "su"),
            category = CAT_SYSTEM_STATUS,
            iconRes = R.drawable.rounded_security_24
        ),

        // --- OEM Specific ---
        StatusBarIcon(
            id = "otg",
            displayName = "OTG Mouse / Keyboard",
            blacklistNames = listOf("otg_mouse", "otg_keyboard"),
            category = CAT_OEM_SPECIFIC,
            iconRes = R.drawable.rounded_settings_accessibility_24
        ),
        StatusBarIcon(
            id = "samsung_smart",
            displayName = "Samsung Smart Features",
            blacklistNames = listOf("glove", "gesture", "smart_scroll", "face", "smart_network", "smart_bonding"),
            category = CAT_OEM_SPECIFIC,
            iconRes = R.drawable.rounded_fiber_smart_record_24
        ),
        StatusBarIcon(
            id = "samsung_services",
            displayName = "Samsung Services",
            blacklistNames = listOf("wearable_gear", "femtoicon", "com.samsung.rcs", "toddler", "keyguard_wakeup", "safezone"),
            category = CAT_OEM_SPECIFIC,
            iconRes = R.drawable.rounded_interests_24
        )
    )

    // Create a map for quick lookup
    private val iconMap = ALL_ICONS.associateBy { it.id }
    private val blacklistNameMap = ALL_ICONS.flatMap { icon ->
        icon.blacklistNames.map { name -> name to icon }
    }.toMap()

    fun getIconById(id: String): StatusBarIcon? = iconMap[id]

    fun getIconByBlacklistName(blacklistName: String): StatusBarIcon? = blacklistNameMap[blacklistName]

    /**
     * Get all blacklist names that should be hidden
     * @param iconVisibilities Map of icon IDs to their visibility state
     * @return Set of blacklist names that should be in the blacklist
     */
    fun getBlacklistNames(iconVisibilities: Map<String, Boolean>): Set<String> {
        val blacklistNames = mutableSetOf<String>()

        for (icon in ALL_ICONS) {
            val isVisible = iconVisibilities[icon.id] ?: icon.defaultVisible
            // If icon is not visible, add all its blacklist names
            if (!isVisible) {
                blacklistNames.addAll(icon.blacklistNames)
            }
        }

        return blacklistNames
    }

    /**
     * Get visibility state for all icons based on current blacklist
     * @param blacklist Comma-separated blacklist string from settings
     * @return Map of icon ID to visibility state
     */
    fun getVisibilityState(blacklist: String?): Map<String, Boolean> {
        val blacklistSet = blacklist?.split(",")?.toSet() ?: emptySet()
        val visibilities = mutableMapOf<String, Boolean>()

        for (icon in ALL_ICONS) {
            // Check if any of this icon's blacklist names are in the blacklist
            val isHidden = icon.blacklistNames.any { it in blacklistSet }
            visibilities[icon.id] = !isHidden
        }

        return visibilities
    }
}

