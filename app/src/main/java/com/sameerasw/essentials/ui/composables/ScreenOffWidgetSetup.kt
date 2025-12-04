package com.sameerasw.essentials.ui.composables

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.provider.Settings
import android.widget.RemoteViews
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.FeatureSettingsActivity
import com.sameerasw.essentials.MainViewModel
import com.sameerasw.essentials.R
import com.sameerasw.essentials.PermissionRegistry
import com.sameerasw.essentials.ui.theme.EssentialsTheme

// Preview helper view model instance (avoid constructing a ViewModel inside a composable)
private val previewMainViewModel = MainViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenOffWidgetSetup(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val isAccessibilityEnabled by viewModel.isAccessibilityEnabled
    val isWidgetEnabled by viewModel.isWidgetEnabled
    val context = LocalContext.current

    var showSheet by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    // If the sheet is open and the required permission(s) are now granted, close the sheet automatically.
    LaunchedEffect(showSheet, isAccessibilityEnabled) {
        if (showSheet) {
            val missing = mutableListOf<PermissionItem>()
            if (!isAccessibilityEnabled) {
                missing.add(
                    PermissionItem(
                        iconRes = R.drawable.rounded_settings_accessibility_24,
                        title = "Accessibility",
                        description = "Required to perform screen off actions via widget",
                        dependentFeatures = PermissionRegistry.getFeatures("ACCESSIBILITY"),
                        actionLabel = "Open Accessibility Settings",
                        action = {
                            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        },
                        isGranted = isAccessibilityEnabled
                    )
                )
            }

            if (missing.isEmpty()) {
                // All required permissions are now granted -> close the sheet
                showSheet = false
            }
            // else: keep sheet open and it will display the missing items (handled below)
        }
    }

    if (showSheet) {
        val permissionItems = listOf(
            PermissionItem(
                iconRes = R.drawable.rounded_settings_accessibility_24,
                title = "Accessibility",
                description = "Required to perform screen off actions via widget",
                dependentFeatures = PermissionRegistry.getFeatures("ACCESSIBILITY"),
                actionLabel = "Open Accessibility Settings",
                action = {
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                },
                isGranted = isAccessibilityEnabled
            )
        )

        PermissionsBottomSheet(
            onDismissRequest = { showSheet = false },
            featureTitle = "Screen off widget",
            permissions = permissionItems
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        FeatureCard(
            title = "Screen off widget",
            isEnabled = isWidgetEnabled,
            onToggle = {
                viewModel.setWidgetEnabled(it, context)
                // Update all existing widgets
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val componentName = ComponentName(context, com.sameerasw.essentials.ScreenOffWidgetProvider::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context.packageName, R.layout.screen_off_widget)
                    if (it) {
                        val intent = Intent(context, com.sameerasw.essentials.ScreenOffAccessibilityService::class.java).apply {
                            action = "LOCK_SCREEN"
                        }
                        val pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
                    }
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            },
            onClick = { context.startActivity(Intent(context, FeatureSettingsActivity::class.java).apply { putExtra("feature", "Screen off widget") }) },
            modifier = Modifier.padding(16.dp),
            isToggleEnabled = isAccessibilityEnabled,
            onDisabledToggleClick = {
                // Show bottom sheet
                showSheet = true
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenOffWidgetSetupPreview() {
    EssentialsTheme {
        // Provide a mock ViewModel for preview
        val mockViewModel = previewMainViewModel.apply {
            // Set up any necessary state for the preview
            isAccessibilityEnabled.value = false
        }
        ScreenOffWidgetSetup(viewModel = mockViewModel)
    }
}
