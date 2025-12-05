package com.sameerasw.essentials.ui.composables.configs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sameerasw.essentials.R
import com.sameerasw.essentials.viewmodels.StatusBarIconViewModel
import com.sameerasw.essentials.ui.components.pickers.NetworkTypePicker
import com.sameerasw.essentials.ui.components.sheets.PermissionItem
import com.sameerasw.essentials.ui.components.sheets.PermissionsBottomSheet
import com.sameerasw.essentials.ui.components.cards.SettingsCard

@Composable
fun StatusBarIconSettingsUI(
    viewModel: StatusBarIconViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPermissionGranted = viewModel.isWriteSecureSettingsEnabled.value
    val isMobileDataVisible = viewModel.isMobileDataVisible.value
    val isWiFiVisible = viewModel.isWiFiVisible.value

    var showPermissionSheet by remember { mutableStateOf(false) }

    // Refresh permission state when composable is shown
    LaunchedEffect(Unit) {
        viewModel.check(context)
    }

    // Permission sheet for Smart Data
    if (showPermissionSheet) {
        PermissionsBottomSheet(
            onDismissRequest = { showPermissionSheet = false },
            featureTitle = "Smart Data",
            permissions = listOf(
                PermissionItem(
                    iconRes = R.drawable.rounded_android_cell_dual_4_bar_24,
                    title = "Read Phone State",
                    description = "Required to detect network type for Smart Data feature",
                    dependentFeatures = listOf("Smart Data"),
                    actionLabel = "Grant Permission",
                    action = {
                        ActivityCompat.requestPermissions(
                            context as ComponentActivity,
                            arrayOf(Manifest.permission.READ_PHONE_STATE),
                            1001
                        )
                    },
                    isGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icon Visibility Settings Card
        SettingsCard(title = "Icon Visibility") {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                // Mobile Data Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_android_cell_dual_4_bar_24),
                        contentDescription = "Mobile Data",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Mobile Data",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isMobileDataVisible,
                        onCheckedChange = { isChecked ->
                            viewModel.setMobileDataVisible(isChecked, context)
                        },
                        enabled = isPermissionGranted
                    )
                }

                // WiFi Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_android_wifi_3_bar_24),
                        contentDescription = "WiFi",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "WiFi",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isWiFiVisible,
                        onCheckedChange = { isChecked ->
                            viewModel.setWiFiVisible(isChecked, context)
                        },
                        enabled = isPermissionGranted
                    )
                }

                // VPN Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_vpn_key_24),
                        contentDescription = "VPN",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "VPN",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = viewModel.isVpnVisible.value,
                        onCheckedChange = { isChecked ->
                            viewModel.setVpnVisible(isChecked, context)
                        },
                        enabled = isPermissionGranted
                    )
                }

                // Alarm Clock Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_alarm_24),
                        contentDescription = "Alarm Clock",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Alarm Clock",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = viewModel.isAlarmClockVisible.value,
                        onCheckedChange = { isChecked ->
                            viewModel.setAlarmClockVisible(isChecked, context)
                        },
                        enabled = isPermissionGranted
                    )
                }

                // Hotspot Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_wifi_tethering_24),
                        contentDescription = "Hotspot",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Hotspot",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = viewModel.isHotspotVisible.value,
                        onCheckedChange = { isChecked ->
                            viewModel.setHotspotVisible(isChecked, context)
                        },
                        enabled = isPermissionGranted
                    )
                }

                // Bluetooth Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_bluetooth_24),
                        contentDescription = "Bluetooth",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Bluetooth",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = viewModel.isBluetoothVisible.value,
                        onCheckedChange = { isChecked ->
                            viewModel.setBluetoothVisible(isChecked, context)
                        },
                        enabled = isPermissionGranted
                    )
                }
            }
        }

        // Smart Visibility Settings Card
        SettingsCard(title = "Smart Visibility") {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                // Smart WiFi Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_cell_wifi_24),
                        contentDescription = "Smart WiFi",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Smart WiFi",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Hide mobile data when WiFi is connected",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = viewModel.isSmartWiFiEnabled.value,
                        onCheckedChange = { isChecked ->
                            viewModel.setSmartWiFiEnabled(isChecked, context)
                        },
                        enabled = isPermissionGranted && viewModel.isMobileDataVisible.value
                    )
                }

                // Smart Data Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_android_cell_dual_5_bar_alert_24),
                        contentDescription = "Smart Data",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Smart Data",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Hide mobile data in certain modes",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier.clickable {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_PHONE_STATE
                            ) == PackageManager.PERMISSION_GRANTED

                            if (!hasPermission) {
                                // Show permission sheet if permission is missing
                                showPermissionSheet = true
                            }
                        }
                    ) {
                        Switch(
                            checked = viewModel.isSmartDataEnabled.value,
                            onCheckedChange = { isChecked ->
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_PHONE_STATE
                                ) == PackageManager.PERMISSION_GRANTED

                                if (isChecked && !hasPermission) {
                                    // Show permission sheet if trying to enable but no permission
                                    showPermissionSheet = true
                                    // Don't enable the feature - the checked state will revert
                                } else {
                                    // Enable/disable normally
                                    viewModel.setSmartDataEnabled(isChecked, context)
                                }
                            },
                            enabled = isPermissionGranted && ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_PHONE_STATE
                            ) == PackageManager.PERMISSION_GRANTED && viewModel.isMobileDataVisible.value
                        )

                        // Invisible overlay catches taps on disabled Switch
                        val isSwitchDisabled =
                            !(isPermissionGranted && ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_PHONE_STATE
                            ) == PackageManager.PERMISSION_GRANTED)

                        if (isSwitchDisabled) {
                            Box(modifier = Modifier.matchParentSize().clickable {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_PHONE_STATE
                                ) == PackageManager.PERMISSION_GRANTED

                                if (!hasPermission) {
                                    // Show permission sheet if permission is missing
                                    showPermissionSheet = true
                                }
                            })
                        }
                    }
                }

                // Network Type Picker (only show when Smart Data is enabled)
                if (viewModel.isSmartDataEnabled.value) {
                    NetworkTypePicker(
                        selectedTypes = viewModel.selectedNetworkTypes.value,
                        onTypesSelected = { selectedTypes ->
                            viewModel.selectedNetworkTypes.value = selectedTypes
                            // Save to preferences
                            val prefs = context.getSharedPreferences(
                                "essentials_prefs",
                                Context.MODE_PRIVATE
                            )
                            prefs.edit().putStringSet(
                                "selected_network_types",
                                selectedTypes.map { it.name }.toSet()
                            ).apply()
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
