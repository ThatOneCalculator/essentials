package com.sameerasw.essentials.ui.components.sheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R

data class InstructionStep(
    val instruction: String,
    val imageRes: Int
)

data class InstructionSection(
    val title: String,
    val iconRes: Int,
    val steps: List<InstructionStep>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionsBottomSheet(
    onDismissRequest: () -> Unit
) {
    val sections = listOf(
        InstructionSection(
            title = "Accessibility, Notification and Overlay permissions",
            iconRes = R.drawable.rounded_security_24,
            steps = listOf(
                InstructionStep(
                    instruction = "You may get this access denied message if you try to grant sensitive permissions such as accessibility, notification listener or overlay permissions. To grant it, check the steps below.",
                    imageRes = R.drawable.accessibility_1
                ),
                InstructionStep(
                    instruction = "1. Go to app info page of Essentials.",
                    imageRes = R.drawable.accessibility_2
                ),
                InstructionStep(
                    instruction = "2. Open the 3-dot menu and select 'Allow restricted settings'. You may have to authenticate with biometrics. Once done, Try to grant the permission again.",
                    imageRes = R.drawable.accessibility_3
                )
            )
        )
        // Add more sections here in the future
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Help & Guides",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            sections.forEach { section ->
                ExpandableGuideSection(section)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ExpandableGuideSection(section: InstructionSection) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "arrow_rotation")

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (expanded) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = CardDefaults.outlinedCardBorder(enabled = expanded)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = section.iconRes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(id = R.drawable.rounded_keyboard_arrow_down_24),
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(rotation)
                )
            }

            // Content
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    section.steps.forEachIndexed { index, step ->
                        InstructionStepItem(
                            stepNumber = index + 1,
                            instruction = step.instruction,
                            imageRes = step.imageRes
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstructionStepItem(
    stepNumber: Int,
    instruction: String,
    imageRes: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = instruction,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(fraction = 0.95f),
        )

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Step $stepNumber Image",
            modifier = Modifier
                .fillMaxWidth(fraction = 0.95f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.FillWidth
        )
    }
}
