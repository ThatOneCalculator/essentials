package com.sameerasw.essentials.ui.ime

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.utils.HapticUtil
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

enum class ShiftState {
    OFF,
    ON,
    LOCKED
}

private fun Modifier.bounceClick(interactionSource: MutableInteractionSource): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1f, label = "scale")
    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun KeyboardInputView(
    onType: (String) -> Unit,
    onKeyPress: (Int) -> Unit
) {
    val view = LocalView.current
    fun performLightHaptic() {
        HapticUtil.performLightHaptic(view)
    }
    fun performHeavyHaptic() {
        HapticUtil.performHeavyHaptic(view)
    }

    var isSymbols by remember { mutableStateOf(false) }
    var shiftState by remember { mutableStateOf(ShiftState.OFF) }

    val keyHeight = 64.dp
    val CustomFontFamily = FontFamily(Font(R.font.google_sans_flex))

    // Layers
    val numberRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    
    val row1Letters = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
    val row2Letters = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
    val row3Letters = listOf("z", "x", "c", "v", "b", "n", "m")
    
    val row1Symbols = listOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")")
    val row2Symbols = listOf("-", "_", "+", "=", "[", "]", "{", "}", "\\", "|")
    // Adjusted row 3 symbols (8 items to roughly match letter row width when no shift)
    val row3Symbols = listOf(";", ":", "'", "\"", ",", ".", "<", ">") 

    val currentRow1 = if (isSymbols) row1Symbols else row1Letters
    val currentRow2 = if (isSymbols) row2Symbols else row2Letters
    val currentRow3 = if (isSymbols) row3Symbols else row3Letters

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(bottom = 48.dp, start = 6.dp, end = 6.dp, top = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Dedicated Number Row
        ButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .height(keyHeight), // Slightly compact
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                numberRow.forEach { char ->
                    val numInteraction = remember { MutableInteractionSource() }
                    FilledTonalIconButton(
                        onClick = {
                            performLightHaptic()
                            onType(char)
                        },
                        interactionSource = numInteraction,
                        colors = IconButtonDefaults.iconButtonVibrantColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .bounceClick(numInteraction),
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            fontFamily = CustomFontFamily
                        )
                    }
                }
            }
        )

        // Row 1
        ButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .height(keyHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                currentRow1.forEach { char ->
                    val displayLabel = if (shiftState != ShiftState.OFF && !isSymbols) char.uppercase() else char
                    val row1Interaction = remember { MutableInteractionSource() }
                    FilledTonalIconButton(
                        onClick = {
                            performLightHaptic()
                            onType(displayLabel)
                            if (shiftState == ShiftState.ON) shiftState = ShiftState.OFF
                        },
                        interactionSource = row1Interaction,
                        colors = IconButtonDefaults.iconButtonVibrantColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .bounceClick(row1Interaction),
                    ) {
                        Text(
                            text = displayLabel,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = CustomFontFamily
                        )
                    }
                }
            }
        )

        // Row 2
        ButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .height(keyHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                if (!isSymbols) Spacer(modifier = Modifier.weight(0.5f)) // Indent for letters
                currentRow2.forEach { char ->
                    val displayLabel = if (shiftState != ShiftState.OFF && !isSymbols) char.uppercase() else char
                    val row2Interaction = remember { MutableInteractionSource() }
                    FilledTonalIconButton(
                        onClick = {
                            performLightHaptic()
                            onType(displayLabel)
                            if (shiftState == ShiftState.ON) shiftState = ShiftState.OFF
                        },
                        interactionSource = row2Interaction,
                        colors = IconButtonDefaults.iconButtonVibrantColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .bounceClick(row2Interaction),
                    ) {
                        Text(
                            text = displayLabel,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = CustomFontFamily
                        )
                    }
                }
                if (!isSymbols) Spacer(modifier = Modifier.weight(0.5f))
            }
        )

        // Row 3 (with Shift/Backspace logic)
        ButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .height(keyHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                // Shift Key - Only show if not in symbols mode
                if (!isSymbols) {
                    val shiftInteraction = remember { MutableInteractionSource() }

                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .fillMaxHeight()
                            .bounceClick(shiftInteraction)
                            .clip(RoundedCornerShape(24.dp))
                            .combinedClickable(
                                onClick = {
                                    performLightHaptic()
                                    shiftState = if (shiftState == ShiftState.OFF) ShiftState.ON else ShiftState.OFF
                                },
                                onLongClick = {
                                    performHeavyHaptic()
                                    shiftState = ShiftState.LOCKED
                                },
                                interactionSource = shiftInteraction,
                                indication = null
                            )
                            .background(
                                if (shiftState != ShiftState.OFF) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceTint
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.key_shift),
                            contentDescription = "Shift",
                            modifier = Modifier.size(24.dp),
                            tint = if (shiftState != ShiftState.OFF) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                } else {
                     // Spacing balance for symbols mode
                     Spacer(modifier = Modifier.weight(0.5f))
                }

                currentRow3.forEach { char ->
                    val displayLabel = if (shiftState != ShiftState.OFF && !isSymbols) char.uppercase() else char
                    val row3Interaction = remember { MutableInteractionSource() }
                    FilledTonalIconButton(
                        onClick = {
                            performLightHaptic()
                            onType(displayLabel)
                            if (shiftState == ShiftState.ON) shiftState = ShiftState.OFF
                        },
                        interactionSource = row3Interaction,
                        colors = IconButtonDefaults.iconButtonVibrantColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .bounceClick(row3Interaction),
                    ) {
                        Text(
                            text = displayLabel,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = CustomFontFamily
                        )
                    }
                }

                // Backspace Key
                val backspaceInteraction = remember { MutableInteractionSource() }
                var delAccumulatedDx by remember { mutableStateOf(0f) }
                val delSweepThreshold = 25f

                Box(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight()
                        .bounceClick(backspaceInteraction)
                        .clip(RoundedCornerShape(24.dp))
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragStart = { delAccumulatedDx = 0f },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    delAccumulatedDx += dragAmount
                                    // Moving left (negative dx) for delete
                                    if (delAccumulatedDx <= -delSweepThreshold) {
                                        val steps = (kotlin.math.abs(delAccumulatedDx) / delSweepThreshold).toInt()
                                        repeat(steps) {
                                            performLightHaptic()
                                            onKeyPress(KeyEvent.KEYCODE_DEL)
                                        }
                                        delAccumulatedDx %= delSweepThreshold
                                    }
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    performLightHaptic()
                                    onKeyPress(KeyEvent.KEYCODE_DEL)
                                }
                            )
                        }
                        .background(MaterialTheme.colorScheme.surfaceTint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_backspace_24),
                        contentDescription = "Backspace",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )

        // Row 4 (Sym, Space, Return)
        ButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .height(keyHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                // Symbols Toggle
                val symInteraction = remember { MutableInteractionSource() }
                FilledIconButton(
                    onClick = {
                        performLightHaptic()
                        isSymbols = !isSymbols
                    },
                    interactionSource = symInteraction,
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .bounceClick(symInteraction),
                ) {
                    Text(
                        text = if (isSymbols) "ABC" else "?#/",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                            fontFamily = CustomFontFamily
                        )
                }

                // Comma Key
                val commaInteraction = remember { MutableInteractionSource() }
                FilledIconButton(
                    onClick = {
                        performLightHaptic()
                        onType(",")
                    },
                    interactionSource = commaInteraction,
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                        .bounceClick(commaInteraction),
                ) {
                    Text(
                        text = ",",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                            fontFamily = CustomFontFamily
                        )
                }

                // Space
                val spaceInteraction = remember { MutableInteractionSource() }
                var accumulatedDx by remember { mutableStateOf(0f) }
                val sweepThreshold = 25f // pixels per cursor move

                Box(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight()
                        .bounceClick(spaceInteraction)
                        .clip(RoundedCornerShape(24.dp))
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragStart = { accumulatedDx = 0f },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    accumulatedDx += dragAmount
                                    val absDx = kotlin.math.abs(accumulatedDx)
                                    if (absDx >= sweepThreshold) {
                                        val steps = (absDx / sweepThreshold).toInt()
                                        val keycode = if (accumulatedDx > 0) KeyEvent.KEYCODE_DPAD_RIGHT else KeyEvent.KEYCODE_DPAD_LEFT
                                        repeat(steps) {
                                            performLightHaptic()
                                            onKeyPress(keycode)
                                        }
                                        accumulatedDx %= sweepThreshold
                                    }
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    performLightHaptic()
                                    onType(" ")
                                }
                            )
                        }
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    // Empty space
                }

                // Dot Key
                val dotInteraction = remember { MutableInteractionSource() }
                FilledIconButton(
                    onClick = {
                        performLightHaptic()
                        onType(".")
                    },
                    interactionSource = dotInteraction,
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                        .bounceClick(dotInteraction),
                ) {
                    Text(
                        text = ".",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                            fontFamily = CustomFontFamily
                        )
                }

                // Return
                val returnInteraction = remember { MutableInteractionSource() }
                FilledIconButton(
                    onClick = {
                        performLightHaptic()
                        onKeyPress(KeyEvent.KEYCODE_ENTER)
                    },
                    interactionSource = returnInteraction,
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight()
                        .bounceClick(returnInteraction),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_keyboard_return_24),
                        contentDescription = "Return",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}
