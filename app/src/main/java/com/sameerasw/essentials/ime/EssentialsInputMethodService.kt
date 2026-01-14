package com.sameerasw.essentials.ime

import android.app.Service
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.sameerasw.essentials.ui.ime.KeyboardInputView
import com.sameerasw.essentials.ui.theme.EssentialsTheme
import com.sameerasw.essentials.data.repository.SettingsRepository
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sameerasw.essentials.viewmodels.MainViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect

class EssentialsInputMethodService : InputMethodService(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }
    private val store by lazy { ViewModelStore() }
    private val savedStateRegistryController by lazy { SavedStateRegistryController.create(this) }

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore: ViewModelStore get() = store
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onCreateInputView(): View {
        // Move to STARTED state before creating the view to ensure
        // the lifecycle is ready when the ComposeView attaches
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        val view = ComposeView(this)

        view.setViewTreeLifecycleOwner(this)
        view.setViewTreeViewModelStoreOwner(this)
        view.setViewTreeSavedStateRegistryOwner(this)

        view.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        view.setContent {
            EssentialsTheme {
                val context = androidx.compose.ui.platform.LocalContext.current
                val prefs = remember { context.getSharedPreferences("essentials_prefs", MODE_PRIVATE) }
                
                // State variables for settings
                var keyboardHeight by remember { mutableFloatStateOf(prefs.getFloat(SettingsRepository.KEY_KEYBOARD_HEIGHT, 54f)) }
                var bottomPadding by remember { mutableFloatStateOf(prefs.getFloat(SettingsRepository.KEY_KEYBOARD_BOTTOM_PADDING, 0f)) }
                var keyboardRoundness by remember { mutableFloatStateOf(prefs.getFloat(SettingsRepository.KEY_KEYBOARD_ROUNDNESS, 24f)) }
                var isFunctionsBottom by remember { mutableStateOf(prefs.getBoolean(SettingsRepository.KEY_KEYBOARD_FUNCTIONS_BOTTOM, false)) }
                var functionsPadding by remember { mutableFloatStateOf(prefs.getFloat(SettingsRepository.KEY_KEYBOARD_FUNCTIONS_PADDING, 0f)) }
                var isHapticsEnabled by remember { mutableStateOf(prefs.getBoolean(SettingsRepository.KEY_KEYBOARD_HAPTICS_ENABLED, true)) }

                // Observe SharedPreferences changes
                DisposableEffect(prefs) {
                    val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                        when (key) {
                            SettingsRepository.KEY_KEYBOARD_HEIGHT -> {
                                keyboardHeight = sharedPreferences.getFloat(SettingsRepository.KEY_KEYBOARD_HEIGHT, 54f)
                            }
                            SettingsRepository.KEY_KEYBOARD_BOTTOM_PADDING -> {
                                bottomPadding = sharedPreferences.getFloat(SettingsRepository.KEY_KEYBOARD_BOTTOM_PADDING, 0f)
                            }
                            SettingsRepository.KEY_KEYBOARD_ROUNDNESS -> {
                                keyboardRoundness = sharedPreferences.getFloat(SettingsRepository.KEY_KEYBOARD_ROUNDNESS, 24f)
                            }
                            SettingsRepository.KEY_KEYBOARD_FUNCTIONS_BOTTOM -> {
                                isFunctionsBottom = sharedPreferences.getBoolean(SettingsRepository.KEY_KEYBOARD_FUNCTIONS_BOTTOM, false)
                            }
                            SettingsRepository.KEY_KEYBOARD_FUNCTIONS_PADDING -> {
                                functionsPadding = sharedPreferences.getFloat(SettingsRepository.KEY_KEYBOARD_FUNCTIONS_PADDING, 0f)
                            }
                            SettingsRepository.KEY_KEYBOARD_HAPTICS_ENABLED -> {
                                isHapticsEnabled = sharedPreferences.getBoolean(SettingsRepository.KEY_KEYBOARD_HAPTICS_ENABLED, true)
                            }
                        }
                    }
                    prefs.registerOnSharedPreferenceChangeListener(listener)
                    onDispose {
                        prefs.unregisterOnSharedPreferenceChangeListener(listener)
                    }
                }

                KeyboardInputView(
                    keyboardHeight = keyboardHeight.dp,
                    bottomPadding = bottomPadding.dp,
                    keyRoundness = keyboardRoundness.dp,
                    isHapticsEnabled = isHapticsEnabled,
                    isFunctionsBottom = isFunctionsBottom,
                    functionsPadding = functionsPadding.dp,
                    onType = { text ->
                        currentInputConnection?.commitText(text, 1)
                    },
                    onKeyPress = { keyCode ->
                        handleKeyPress(keyCode)
                    }
                )
            }
        }
        return view
    }

    override fun onBindInput() {
        super.onBindInput()
        // Also set lifecycle owners on the window decor view if available
        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeViewModelStoreOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
    }

    override fun onStartInputView(info: android.view.inputmethod.EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        // Ensure lifecycle is in RESUMED state when view becomes visible
        if (lifecycleRegistry.currentState != Lifecycle.State.RESUMED) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    private fun handleKeyPress(keyCode: Int) {
        val inputConnection = currentInputConnection ?: return
        
        when (keyCode) {
            KeyEvent.KEYCODE_DEL -> {
                val selectedText = inputConnection.getSelectedText(0)
                if (selectedText != null && selectedText.isNotEmpty()) {
                    inputConnection.commitText("", 1)
                } else {
                    inputConnection.deleteSurroundingText(1, 0)
                }
            }
            else -> {
                sendDownUpKeyEvents(keyCode)
            }
        }
    }
}
