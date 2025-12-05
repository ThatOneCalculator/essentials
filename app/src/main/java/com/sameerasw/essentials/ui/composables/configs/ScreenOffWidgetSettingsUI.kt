package com.sameerasw.essentials.ui.composables.configs

import android.content.SharedPreferences
import android.os.Vibrator
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.sameerasw.essentials.viewmodels.MainViewModel
import com.sameerasw.essentials.ui.components.pickers.HapticFeedbackPicker
import com.sameerasw.essentials.ui.components.cards.SettingsCard
import com.sameerasw.essentials.utils.HapticFeedbackType
import com.sameerasw.essentials.utils.performHapticFeedback

@Composable
fun ScreenOffWidgetSettingsUI(
    viewModel: MainViewModel,
    selectedHaptic: HapticFeedbackType,
    onHapticSelected: (HapticFeedbackType) -> Unit,
    vibrator: Vibrator?,
    prefs: SharedPreferences,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    SettingsCard(title = "Haptic Feedback", modifier = modifier) {
        HapticFeedbackPicker(
            selectedFeedback = selectedHaptic,
            onFeedbackSelected = { type ->
                prefs.edit {
                    putString("haptic_feedback_type", type.name)
                }
                onHapticSelected(type)
                viewModel.setHapticFeedback(type, context)
                if (vibrator != null) {
                    performHapticFeedback(vibrator, type)
                }
            }
        )
    }
}
