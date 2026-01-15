
package com.sameerasw.essentials.services.widgets

import android.content.Context
import android.os.BatteryManager
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.height
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.glance.Image
import androidx.glance.ImageProvider
import com.sameerasw.essentials.R

class BatteriesWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                // System Service and Android Battery
                val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                
                // Mac Battery
                val prefs = androidx.glance.currentState<androidx.datastore.preferences.core.Preferences>()
                val KEY_AIRSYNC_ENABLED = androidx.datastore.preferences.core.booleanPreferencesKey(com.sameerasw.essentials.data.repository.SettingsRepository.KEY_AIRSYNC_CONNECTION_ENABLED)
                val KEY_MAC_LEVEL = androidx.datastore.preferences.core.intPreferencesKey(com.sameerasw.essentials.data.repository.SettingsRepository.KEY_MAC_BATTERY_LEVEL)
                val KEY_MAC_CONNECTED = androidx.datastore.preferences.core.booleanPreferencesKey(com.sameerasw.essentials.data.repository.SettingsRepository.KEY_AIRSYNC_MAC_CONNECTED)
                
                // Read from Glance State
                val isAirSyncEnabled = prefs[KEY_AIRSYNC_ENABLED] ?: false
                val macLevel = prefs[KEY_MAC_LEVEL] ?: -1
                val isMacConnected = prefs[KEY_MAC_CONNECTED] ?: false
                
                val showMac = isAirSyncEnabled && macLevel != -1 && isMacConnected
                
                val isMacFresh = true
                
                android.util.Log.d("BatteriesWidget", "Update: enabled=$isAirSyncEnabled, level=$macLevel, connected=$isMacConnected -> showMac=$showMac")

                // Style & Colors
                val themedContext = android.view.ContextThemeWrapper(context, R.style.Theme_Essentials)
                val primaryColor = resolveColor(themedContext, android.R.attr.colorActivatedHighlight)
                val errorColor = resolveColor(themedContext, android.R.attr.colorError)
                val surfaceVariant = resolveColor(themedContext, android.R.attr.colorPressedHighlight)
                val surfaceColor = resolveColor(themedContext, android.R.attr.colorForeground)
                val warningColor = android.graphics.Color.parseColor("#FFC107")
                
                val trackColor = ColorUtils.setAlphaComponent(surfaceVariant, 76)

                // Layout
                if (showMac) {
                    Row(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(GlanceTheme.colors.surface)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Android Section
                        val androidRingColor = when {
                            batteryLevel <= 10 -> errorColor
                            batteryLevel < 20 -> warningColor
                            else -> primaryColor
                        }
                        
                        val deviceIcon = ContextCompat.getDrawable(context, R.drawable.rounded_mobile_24)
                        val androidBitmap = com.sameerasw.essentials.utils.BatteryRingDrawer.drawBatteryWidget(
                            context, batteryLevel, androidRingColor, trackColor, primaryColor, surfaceColor, deviceIcon, 300, 300
                        )
                        Box(modifier = GlanceModifier.defaultWeight().fillMaxHeight(), contentAlignment = Alignment.Center) {
                            Image(provider = ImageProvider(androidBitmap), contentDescription = "Android: $batteryLevel%", modifier = GlanceModifier.fillMaxSize())
                        }
                        
                        Spacer(modifier = GlanceModifier.width(8.dp))

                        // Mac Section
                        val macRingColor = when {
                            macLevel <= 10 -> errorColor
                            macLevel < 20 -> warningColor
                            else -> primaryColor
                        }
                        val macIcon = ContextCompat.getDrawable(context, R.drawable.rounded_laptop_mac_24)
                        val macBitmap = com.sameerasw.essentials.utils.BatteryRingDrawer.drawBatteryWidget(
                            context, macLevel, macRingColor, trackColor, primaryColor, surfaceColor, macIcon, 300, 300
                        )
                        Box(modifier = GlanceModifier.defaultWeight().fillMaxHeight(), contentAlignment = Alignment.Center) {
                            Image(provider = ImageProvider(macBitmap), contentDescription = "Mac: $macLevel%", modifier = GlanceModifier.fillMaxSize())
                        }
                        
                    }
                } else {
                    val ringColor = when {
                        batteryLevel <= 10 -> errorColor
                        batteryLevel < 20 -> warningColor
                        else -> primaryColor
                    }
                    val deviceIcon = ContextCompat.getDrawable(context, R.drawable.rounded_mobile_24)
                    val bitmap = com.sameerasw.essentials.utils.BatteryRingDrawer.drawBatteryWidget(
                        context, batteryLevel, ringColor, trackColor, primaryColor, surfaceColor, deviceIcon, 512, 512
                    )

                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(GlanceTheme.colors.surface)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                       Image(
                           provider = ImageProvider(bitmap),
                           contentDescription = "Battery Level $batteryLevel%",
                           modifier = GlanceModifier.fillMaxSize()
                       )
                    }
                }
            }
        }
    }

    private fun resolveColor(context: Context, @androidx.annotation.AttrRes attr: Int): Int {
        val typedValue = android.util.TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}