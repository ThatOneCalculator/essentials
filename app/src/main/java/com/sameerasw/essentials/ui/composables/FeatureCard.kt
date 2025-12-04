package com.sameerasw.essentials.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R

@Composable
fun FeatureCard(
    title: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasMoreSettings: Boolean = true,
    isToggleEnabled: Boolean = true
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ),
        modifier = modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f)
            )
            if (hasMoreSettings) {
                Icon(
                    modifier = Modifier.padding(end = 12.dp),
                    painter = painterResource(id = R.drawable.rounded_chevron_right_24),
                    contentDescription = "More settings"
                )
            }
            // If the toggle is disabled, show it visually OFF and prevent toggling
            Switch(
                checked = if (isToggleEnabled) isEnabled else false,
                onCheckedChange = { checked -> if (isToggleEnabled) onToggle(checked) },
                enabled = isToggleEnabled
            )
        }
    }
}
