package com.sameerasw.essentials.ui.components.linkActions

import android.content.pm.ResolveInfo
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import com.sameerasw.essentials.R

@Composable
fun AppPickerItem(
    resolveInfo: ResolveInfo,
    modifier: Modifier = Modifier,
    togglePin: (String) -> Unit,
    pinnedPackages: Set<String>,
    demo: Boolean = false,
    onTapAction: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val packageName = resolveInfo.activityInfo.packageName
    val isPinned = pinnedPackages.contains(packageName)
    val haptic = LocalHapticFeedback.current

    Log.d("LinkPicker", "AppPickerItem, demo = $demo")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (demo) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    } else {
                        onTapAction?.invoke()
                    }
                },
                onLongClick = {
                    togglePin(packageName)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = RoundedCornerShape(MaterialTheme.shapes.extraSmall.bottomEnd)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        AsyncImage(
            model = resolveInfo.loadIcon(context.packageManager),
            contentDescription = "App icon",
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )


        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = resolveInfo.loadLabel(context.packageManager).toString(),
                modifier = Modifier
                    .padding(start = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (isPinned) {
            Icon(
                painter = painterResource(id = R.drawable.rounded_bookmark_24),
                contentDescription = "Pinned",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.padding())
        }
    }
}
