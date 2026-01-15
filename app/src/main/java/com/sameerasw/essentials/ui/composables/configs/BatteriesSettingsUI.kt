package com.sameerasw.essentials.ui.composables.configs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.viewmodels.MainViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BatteriesSettingsUI(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        // AirSync Interaction
        RoundedCardContainer {
            val isAirSyncInstalled = try {
                context.packageManager.getPackageInfo("com.sameerasw.airsync", 0)
                true
            } catch (e: android.content.pm.PackageManager.NameNotFoundException) {
                false
            }

            if (isAirSyncInstalled) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.connect_to_airsync)) },
                    supportingContent = { Text(stringResource(R.string.connect_to_airsync_summary)) },
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = viewModel.isAirSyncConnectionEnabled.value,
                            onCheckedChange = { viewModel.setAirSyncConnectionEnabled(it, context) }
                        )
                    }
                )
            } else {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.download_airsync)) },
                    supportingContent = { Text(stringResource(R.string.download_airsync_summary)) },
                    trailingContent = {
                        androidx.compose.material3.Button(
                            onClick = {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.sameerasw.airsync"))
                                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Download")
                        }
                    }
                )
            }
        }
    }
}
