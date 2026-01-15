package com.sameerasw.essentials.ui.composables.configs

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.viewmodels.CaffeinateViewModel
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.cards.IconToggleItem
import com.sameerasw.essentials.ui.components.sheets.PermissionItem
import com.sameerasw.essentials.ui.components.sheets.PermissionsBottomSheet
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.modifiers.highlight
import androidx.core.net.toUri

@Composable
fun CaffeinateSettingsUI(
    viewModel: CaffeinateViewModel,
    modifier: Modifier = Modifier,
    highlightSetting: String? = null
) {
    val context = LocalContext.current

    var showPermissionSheet by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.postNotificationsGranted.value = isGranted
    }

    // Refresh state when composable is shown
    LaunchedEffect(Unit) {
        viewModel.check(context)
    }

    if (showPermissionSheet) {
        PermissionsBottomSheet(
            onDismissRequest = { showPermissionSheet = false },
            featureTitle = R.string.permission_show_notification_title,
            permissions = listOf(
                PermissionItem(
                    iconRes = R.drawable.rounded_notifications_unread_24,
                    title = R.string.permission_post_notifications_title,
                    description = R.string.permission_post_notifications_desc,
                    dependentFeatures = listOf(R.string.permission_show_notification_title),
                    actionLabel = R.string.permission_grant_action,
                    action = {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    },
                    isGranted = viewModel.postNotificationsGranted.value
                ),
                PermissionItem(
                    iconRes = R.drawable.rounded_battery_android_frame_alert_24,
                    title = R.string.perm_battery_optimization_title,
                    description = R.string.perm_battery_optimization_desc,
                    dependentFeatures = listOf(R.string.feat_caffeinate_title),
                    actionLabel = R.string.permission_grant_action,
                    action = {
                        val intent = Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                            data = android.net.Uri.parse("package:${context.packageName}")
                        }
                        context.startActivity(intent)
                    },
                    isGranted = viewModel.batteryOptimizationGranted.value
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Battery Category
        Text(
            text = stringResource(R.string.perm_battery_optimization_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        RoundedCardContainer(
            modifier = Modifier,
            spacing = 2.dp,
            cornerRadius = 24.dp
        ) {
            IconToggleItem(
                title = stringResource(R.string.caffeinate_battery_optimization_title),
                isChecked = viewModel.batteryOptimizationGranted.value,
                onCheckedChange = { _ ->
                    val intent = Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = "package:${context.packageName}".toUri()
                    }
                    context.startActivity(intent)
                },
                iconRes = R.drawable.rounded_battery_android_frame_alert_24,
            )
        }
    }
}
