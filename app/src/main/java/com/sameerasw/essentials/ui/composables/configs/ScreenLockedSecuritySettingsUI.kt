package com.sameerasw.essentials.ui.composables.configs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.cards.IconToggleItem
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.modifiers.highlight
import com.sameerasw.essentials.viewmodels.MainViewModel

@Composable
fun ScreenLockedSecuritySettingsUI(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    highlightSetting: String? = null
) {
    val context = LocalContext.current
    val isAccessibilityEnabled = viewModel.isAccessibilityEnabled.value

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Security",
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
                title = "Screen locked security",
                isChecked = viewModel.isScreenLockedSecurityEnabled.value,
                onCheckedChange = { isChecked ->
                    viewModel.setScreenLockedSecurityEnabled(isChecked, context)
                },
                enabled = isAccessibilityEnabled && viewModel.isWriteSecureSettingsEnabled.value && viewModel.isDeviceAdminEnabled.value,
                onDisabledClick = {
                    // Handled by parent
                },
                iconRes = R.drawable.rounded_security_24,
                modifier = Modifier.highlight(highlightSetting == "screen_locked_security_toggle")
            )
        }


        // Warning Section
        RoundedCardContainer(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "⚠️ WARNING",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "This feature is not foolproof. There may be edge cases where someone still being able to interact with the tile. \nAlso keep in mind that Android will always allow to do a forced reboot and Pixels will always allow the device to be turned off from the lock screen as well.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Make sure to remove the airplane mode tile from quick settings as that is not preventable because it does not open a dialog window.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )

            }
        }

        Text(
            text = "When enabled, the Quick Settings panel will be immediately closed and the device will be locked down if someone attempt to interact with Internet tiles while the device is locked. \n\nThis will also disable biometric unlock to prevent further unauthorized access. Animation scale will be reduced to 0.1x while locked to make it even harder to interact with.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
