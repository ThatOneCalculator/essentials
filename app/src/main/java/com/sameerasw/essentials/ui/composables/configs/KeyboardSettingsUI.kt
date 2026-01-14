package com.sameerasw.essentials.ui.composables.configs

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.cards.IconToggleItem
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.components.sliders.ConfigSliderItem
import com.sameerasw.essentials.ui.modifiers.highlight
import com.sameerasw.essentials.viewmodels.MainViewModel

@Composable
fun KeyboardSettingsUI(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    highlightSetting: String? = null
) {
    val context = LocalContext.current
    val view = LocalView.current
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Test Field
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(R.string.test_keyboard_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Customization
        Text(
            text = stringResource(R.string.feat_system_keyboard_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        RoundedCardContainer(spacing = 2.dp) {
            ConfigSliderItem(
                title = stringResource(R.string.label_keyboard_height),
                value = viewModel.keyboardHeight.floatValue,
                onValueChange = { 
                    viewModel.setKeyboardHeight(it, context)
                    com.sameerasw.essentials.utils.HapticUtil.performSliderHaptic(view)
                },
                valueRange = 40f..80f,
                steps = 0,
                modifier = Modifier.highlight(highlightSetting == "keyboard_height")
            )

            ConfigSliderItem(
                title = stringResource(R.string.label_keyboard_bottom_padding),
                value = viewModel.keyboardBottomPadding.floatValue,
                onValueChange = { 
                    viewModel.setKeyboardBottomPadding(it, context)
                    com.sameerasw.essentials.utils.HapticUtil.performSliderHaptic(view)
                },
                valueRange = 0f..100f,
                steps = 0,
                modifier = Modifier.highlight(highlightSetting == "keyboard_bottom_padding")
            )

            ConfigSliderItem(
                title = stringResource(R.string.label_keyboard_roundness),
                value = viewModel.keyboardRoundness.floatValue,
                onValueChange = { 
                    viewModel.setKeyboardRoundness(it, context)
                    com.sameerasw.essentials.utils.HapticUtil.performSliderHaptic(view)
                },
                valueRange = 4f..30f,
                steps = 0,
                modifier = Modifier.highlight(highlightSetting == "keyboard_roundness")
            )

            IconToggleItem(
                iconRes = R.drawable.rounded_mobile_vibrate_24,
                title = stringResource(R.string.label_keyboard_haptics),
                isChecked = viewModel.isKeyboardHapticsEnabled.value,
                onCheckedChange = { viewModel.setKeyboardHapticsEnabled(it, context) },
                modifier = Modifier.highlight(highlightSetting == "keyboard_haptics")
            )
        }
    }
}
