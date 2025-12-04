package com.sameerasw.essentials.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class PermissionItem(
    val title: String,
    val description: String,
    val actionLabel: String? = null,
    val action: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsBottomSheet(
    onDismissRequest: () -> Unit,
    featureTitle: String,
    permissions: List<PermissionItem>
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "$featureTitle requires following permissions", style = MaterialTheme.typography.titleLarge)

            permissions.forEach { perm ->
                Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = perm.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = perm.description, style = MaterialTheme.typography.bodyMedium)
                        if (perm.actionLabel != null && perm.action != null) {
                            Button(onClick = {
                                perm.action.invoke()
                            }, modifier = Modifier.padding(top = 8.dp)) {
                                Text(text = perm.actionLabel)
                            }
                        }
                    }
                }
            }
        }
    }
}
