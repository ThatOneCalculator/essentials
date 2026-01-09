package com.sameerasw.essentials.domain.registry

import android.content.Context
import com.sameerasw.essentials.R
import com.sameerasw.essentials.domain.model.Feature
import com.sameerasw.essentials.domain.model.SearchSetting
import com.sameerasw.essentials.viewmodels.MainViewModel

object FeatureRegistry {
    val ALL_FEATURES = listOf(
        object : Feature(
            id = "Screen off widget",
            title = R.string.feat_screen_off_widget_title,
            iconRes = R.drawable.rounded_settings_power_24,
            category = R.string.cat_tools,
            description = R.string.feat_screen_off_widget_desc,
            permissionKeys = listOf("ACCESSIBILITY"),
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_haptic_title,
                    R.string.search_haptic_desc,
                    "haptic_picker",
                    listOf("vibration", "touch", "feel")
                )
            ),
            showToggle = false
        ) {
            override fun isEnabled(viewModel: MainViewModel) = true
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) {}
        },

        object : Feature(
            id = "Statusbar icons",
            title = R.string.feat_statusbar_icons_title,
            iconRes = R.drawable.rounded_interests_24,
            category = R.string.cat_visuals,
            description = R.string.feat_statusbar_icons_desc,
            permissionKeys = listOf("WRITE_SECURE_SETTINGS"),
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_smart_wifi_title,
                    R.string.search_smart_wifi_desc,
                    "smart_wifi",
                    listOf("network", "visibility", "auto", "hide")
                ),
                SearchSetting(
                    R.string.search_smart_data_title,
                    R.string.search_smart_data_desc,
                    "smart_data",
                    listOf("network", "visibility", "auto", "hide")
                ),
                SearchSetting(
                    R.string.search_reset_icons_title,
                    R.string.search_reset_icons_desc,
                    "reset_icons",
                    listOf("restore", "default", "icon")
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = viewModel.isStatusBarIconControlEnabled.value
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) = viewModel.isWriteSecureSettingsEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) = viewModel.setStatusBarIconControlEnabled(enabled, context)
        },

        object : Feature(
            id = "Caffeinate",
            title = R.string.feat_caffeinate_title,
            iconRes = R.drawable.rounded_coffee_24,
            category = R.string.cat_tools,
            description = R.string.feat_caffeinate_desc,
            permissionKeys = listOf("POST_NOTIFICATIONS"),
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_caffeinate_notif_title,
                    R.string.search_caffeinate_notif_desc,
                    "show_notification",
                    listOf("visible", "alert")
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = viewModel.isCaffeinateActive.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) {
                if (enabled) viewModel.startCaffeinate(context) else viewModel.stopCaffeinate(context)
            }
        },

        object : Feature(
            id = "Maps power saving mode",
            title = R.string.feat_maps_power_saving_title,
            iconRes = R.drawable.rounded_navigation_24,
            category = R.string.cat_tools,
            description = R.string.feat_maps_power_saving_desc,
            permissionKeys = listOf("SHIZUKU", "NOTIFICATION_LISTENER"),
            hasMoreSettings = false
        ) {
            override fun isEnabled(viewModel: MainViewModel) = viewModel.isMapsPowerSavingEnabled.value
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) =
                viewModel.isShizukuAvailable.value && viewModel.isShizukuPermissionGranted.value && viewModel.isNotificationListenerEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) = viewModel.setMapsPowerSavingEnabled(enabled, context)
            override fun onClick(context: Context, viewModel: MainViewModel) {}
        },

        object : Feature(
            id = "Notification lighting",
            title = R.string.feat_notification_lighting_title,
            iconRes = R.drawable.rounded_magnify_fullscreen_24,
            category = R.string.cat_visuals,
            description = R.string.feat_notification_lighting_desc,
            permissionKeys = listOf("DRAW_OVERLAYS", "ACCESSIBILITY", "NOTIFICATION_LISTENER"),
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_lighting_style_title,
                    R.string.search_lighting_style_desc,
                    "style",
                    listOf("animation", "visual", "look")
                ),
                SearchSetting(
                    R.string.search_corner_radius_title,
                    R.string.search_corner_radius_desc,
                    "corner_radius",
                    listOf("round", "shape", "edge")
                ),
                SearchSetting(
                    R.string.search_skip_silent_title,
                    R.string.search_skip_silent_desc,
                    "skip_silent_notifications",
                    listOf("quiet", "ignore", "filter")
                ),
                SearchSetting(
                    R.string.search_flashlight_pulse_title,
                    R.string.search_flashlight_pulse_desc,
                    "flashlight_pulse",
                    listOf("light", "torch", "pulse", "notification")
                ),
                SearchSetting(
                    R.string.search_only_facing_down_title,
                    R.string.search_only_facing_down_desc,
                    "flashlight_pulse_facedown",
                    listOf("proximity", "sensor", "face", "down")
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = viewModel.isNotificationLightingEnabled.value
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) =
                viewModel.isOverlayPermissionGranted.value && viewModel.isNotificationLightingAccessibilityEnabled.value && viewModel.isNotificationListenerEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) = viewModel.setNotificationLightingEnabled(enabled, context)
        },

        object : Feature(
            id = "Sound mode tile",
            title = R.string.feat_sound_mode_tile_title,
            iconRes = R.drawable.rounded_volume_up_24,
            category = R.string.cat_tools,
            description = R.string.feat_sound_mode_tile_desc,
            permissionKeys = listOf("WRITE_SECURE_SETTINGS"),
            showToggle = false
        ) {
            override fun isEnabled(viewModel: MainViewModel) = true
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) {}
        },

        object : Feature(
            id = "Link actions",
            title = R.string.feat_link_actions_title,
            iconRes = R.drawable.rounded_link_24,
            category = R.string.cat_tools,
            description = R.string.feat_link_actions_desc,
            showToggle = false
        ) {
            override fun isEnabled(viewModel: MainViewModel) = false
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) {}
        },

        object : Feature(
            id = "Snooze system notifications",
            title = R.string.feat_snooze_notifications_title,
            iconRes = R.drawable.rounded_snooze_24,
            category = R.string.cat_tools,
            description = R.string.feat_snooze_notifications_desc,
            permissionKeys = listOf("NOTIFICATION_LISTENER"),
            showToggle = false,
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_snooze_debug_title,
                    R.string.search_snooze_debug_desc,
                    "snooze_debugging",
                    listOf("adb", "usb", "debug")
                ),
                SearchSetting(
                    R.string.search_snooze_file_title,
                    R.string.search_snooze_file_desc,
                    "snooze_file_transfer",
                    listOf("usb", "file", "transfer", "mtp")
                ),
                SearchSetting(
                    R.string.search_snooze_charge_title,
                    R.string.search_snooze_charge_desc,
                    "snooze_charging",
                    listOf("battery", "charge", "power")
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = false
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) = viewModel.isNotificationListenerEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) {}
        },

        object : Feature(
            id = "Quick settings tiles",
            title = R.string.feat_qs_tiles_title,
            iconRes = R.drawable.rounded_tile_small_24,
            category = R.string.cat_system,
            description = R.string.feat_qs_tiles_desc,
            showToggle = false,
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_qs_blur_title,
                    R.string.search_qs_blur_desc,
                    "UI Blur",
                    listOf("blur", "glass", "vignette", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_bubbles_title,
                    R.string.search_qs_bubbles_desc,
                    "Bubbles",
                    listOf("float", "window", "overlay", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_sensitive_title,
                    R.string.search_qs_sensitive_desc,
                    "Sensitive Content",
                    listOf("privacy", "lock", "secure", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_wake_title,
                    R.string.search_qs_wake_desc,
                    "Tap to Wake",
                    listOf("touch", "wake", "display", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_aod_title,
                    R.string.search_qs_aod_desc,
                    "AOD",
                    listOf("always", "display", "clock", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_caffeinate_title,
                    R.string.search_qs_caffeinate_desc,
                    "Caffeinate",
                    listOf("stay", "on", "timeout", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_sound_title,
                    R.string.search_qs_sound_desc,
                    "Sound Mode",
                    listOf("audio", "mute", "volume", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_lighting_title,
                    R.string.search_qs_lighting_desc,
                    "Notification Lighting",
                    listOf("glow", "notification", "led", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_night_light_title,
                    R.string.search_qs_night_light_desc,
                    "Dynamic Night Light",
                    listOf("blue", "filter", "auto", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_locked_sec_title,
                    R.string.search_qs_locked_sec_desc,
                    "Locked Security",
                    listOf("wifi", "data", "lock", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_mono_title,
                    R.string.search_qs_mono_desc,
                    "Mono Audio",
                    listOf("sound", "accessibility", "hear", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_flashlight_title,
                    R.string.search_qs_flashlight_desc,
                    "Flashlight",
                    listOf("light", "torch", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_freeze_title,
                    R.string.search_qs_freeze_desc,
                    "App Freezing",
                    listOf("freeze", "shizuku", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                ),
                SearchSetting(
                    R.string.search_qs_pulse_title,
                    R.string.search_qs_pulse_desc,
                    "Flashlight Pulse",
                    listOf("light", "torch", "pulse", "notification", "tile", "qs"),
                    R.string.feat_qs_tiles_title
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = false
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) {}
        },

        object : Feature(
            id = "Button remap",
            title = R.string.feat_button_remap_title,
            iconRes = R.drawable.rounded_switch_access_3_24,
            category = R.string.cat_system,
            description = R.string.feat_button_remap_desc,
            permissionKeys = listOf("ACCESSIBILITY"),
            showToggle = false,
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_remap_enable_title,
                    R.string.search_remap_enable_desc,
                    "enable_remap",
                    listOf("switch", "master")
                ),
                SearchSetting(
                    R.string.search_remap_haptic_title,
                    R.string.search_remap_haptic_desc,
                    "remap_haptic",
                    listOf("vibration", "feel")
                ),
                SearchSetting(
                    R.string.search_remap_flashlight_title,
                    R.string.search_remap_flashlight_desc,
                    "flashlight_toggle",
                    listOf("light", "torch")
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = true
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) = viewModel.isAccessibilityEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) = viewModel.setButtonRemapEnabled(enabled, context)
        },

        object : Feature(
            id = "Dynamic night light",
            title = R.string.feat_dynamic_night_light_title,
            iconRes = R.drawable.rounded_nightlight_24,
            category = R.string.cat_visuals,
            description = R.string.feat_dynamic_night_light_desc,
            permissionKeys = listOf("ACCESSIBILITY", "WRITE_SECURE_SETTINGS"),
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_night_light_enable_title,
                    R.string.search_night_light_enable_desc,
                    "dynamic_night_light_toggle",
                    listOf("switch", "master")
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = viewModel.isDynamicNightLightEnabled.value
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) =
                viewModel.isAccessibilityEnabled.value && viewModel.isWriteSecureSettingsEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) = viewModel.setDynamicNightLightEnabled(enabled, context)
        },

        object : Feature(
            id = "Screen locked security",
            title = R.string.feat_screen_locked_security_title,
            iconRes = R.drawable.rounded_security_24,
            category = R.string.cat_security,
            description = R.string.feat_screen_locked_security_desc,
            permissionKeys = listOf("ACCESSIBILITY", "WRITE_SECURE_SETTINGS", "DEVICE_ADMIN")
        ) {
            override fun isEnabled(viewModel: MainViewModel) = viewModel.isScreenLockedSecurityEnabled.value
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) =
                viewModel.isAccessibilityEnabled.value && viewModel.isWriteSecureSettingsEnabled.value && viewModel.isDeviceAdminEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) = viewModel.setScreenLockedSecurityEnabled(enabled, context)
        },

        object : Feature(
            id = "App lock",
            title = R.string.feat_app_lock_title,
            iconRes = R.drawable.rounded_shield_lock_24,
            category = R.string.cat_security,
            description = R.string.feat_app_lock_desc,
            permissionKeys = listOf("ACCESSIBILITY"),
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_app_lock_enable_title,
                    R.string.search_app_lock_enable_desc,
                    "app_lock_enabled",
                    listOf("secure", "privacy", "biometric", "face", "fingerprint")
                ),
                SearchSetting(
                    R.string.search_app_lock_pick_title,
                    R.string.search_app_lock_pick_desc,
                    "app_lock_selected_apps",
                    listOf("list", "picker", "selection")
                )
            )
        ) {
            override fun isEnabled(viewModel: MainViewModel) = viewModel.isAppLockEnabled.value
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) = viewModel.isAccessibilityEnabled.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) = viewModel.setAppLockEnabled(enabled, context)
        },

        object : Feature(
            id = "Freeze",
            title = R.string.feat_freeze_title,
            iconRes = R.drawable.rounded_mode_cool_24,
            category = R.string.cat_tools,
            description = R.string.feat_freeze_desc,
            permissionKeys = listOf("SHIZUKU"),
            searchableSettings = listOf(
                SearchSetting(
                    R.string.search_freeze_pick_title,
                    R.string.search_freeze_pick_desc,
                    "freeze_selected_apps",
                    listOf("list", "picker", "selection")
                ),
                SearchSetting(
                    R.string.search_freeze_all_title,
                    R.string.search_freeze_all_desc,
                    "freeze_all_manual",
                    listOf("manual", "now", "shizuku")
                ),
                SearchSetting(
                    R.string.search_freeze_locked_title,
                    R.string.search_freeze_locked_desc,
                    "freeze_when_locked_enabled",
                    listOf("automation", "auto", "lock")
                ),
                SearchSetting(
                    R.string.search_freeze_delay_title,
                    R.string.search_freeze_delay_desc,
                    "freeze_lock_delay_index",
                    listOf("timer", "wait", "timeout")
                )
            ),
            showToggle = false
        ) {
            override fun isEnabled(viewModel: MainViewModel) = true
            override fun isToggleEnabled(viewModel: MainViewModel, context: Context) =
                viewModel.isShizukuAvailable.value && viewModel.isShizukuPermissionGranted.value
            override fun onToggle(viewModel: MainViewModel, context: Context, enabled: Boolean) {}
        }
    )
}