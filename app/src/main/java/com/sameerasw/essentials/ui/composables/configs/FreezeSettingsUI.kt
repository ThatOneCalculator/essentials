package com.sameerasw.essentials.ui.composables.configs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.cards.FeatureCard
import com.sameerasw.essentials.ui.components.cards.IconToggleItem
import com.sameerasw.essentials.ui.components.sheets.AppSelectionSheet
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.viewmodels.MainViewModel
import com.sameerasw.essentials.ui.modifiers.highlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreezeSettingsUI(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    highlightKey: String? = null
) {
    val context = LocalContext.current
    var isAppSelectionSheetOpen by remember { mutableStateOf(false) }
    
    val isFreezeEnabled by viewModel.isFreezeEnabled
    val isShizukuAvailable by viewModel.isShizukuAvailable
    val isShizukuPermissionGranted by viewModel.isShizukuPermissionGranted

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "App Control",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        RoundedCardContainer(
            modifier = Modifier,
            spacing = 2.dp,
            cornerRadius = 24.dp
        ) {
            IconToggleItem(
                iconRes = R.drawable.rounded_mode_cool_24,
                title = "Enable Freeze",
                isChecked = isFreezeEnabled,
                onCheckedChange = { enabled ->
                    viewModel.setFreezeEnabled(enabled, context)
                },
                enabled = isShizukuAvailable && isShizukuPermissionGranted,
                onDisabledClick = {
                    viewModel.requestShizukuPermission()
                },
                modifier = Modifier.highlight(highlightKey == "freeze_enabled")
            )

            FeatureCard(
                title = "Select apps to freeze",
                description = "Choose which apps to disable",
                iconRes = R.drawable.rounded_app_registration_24,
                isEnabled = isFreezeEnabled,
                showToggle = false,
                hasMoreSettings = true,
                onToggle = {},
                onClick = { isAppSelectionSheetOpen = true },
                modifier = Modifier.highlight(highlightKey == "freeze_selected_apps")
            )
        }

        Text(
            text = "Freeze applications to save battery and stop background processes. Frozen apps will disappear from your launcher and cannot run until unfrozen.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "This feature requires Shizuku to be running and authorized. Freezing system apps is not recommended as it may cause system instability.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (isAppSelectionSheetOpen) {
            AppSelectionSheet(
                onDismissRequest = { isAppSelectionSheetOpen = false },
                onLoadApps = { viewModel.loadFreezeSelectedApps(it) },
                onSaveApps = { ctx, apps -> viewModel.saveFreezeSelectedApps(ctx, apps) },
                onAppToggle = { ctx, pkg, enabled -> viewModel.updateFreezeAppEnabled(ctx, pkg, enabled) }
            )
        }
    }
}
