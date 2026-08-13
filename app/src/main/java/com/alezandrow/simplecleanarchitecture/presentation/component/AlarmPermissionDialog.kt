package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alezandrow.simplecleanarchitecture.R

@Composable
fun AlarmPermissionDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.need_exact_alarm_permission)) },
        text = { Text(text = stringResource(R.string.reminder_permission_required)) },
        confirmButton = {
            Button(onClick = onOpenSettings) {
                Text(text = stringResource(R.string.open_settings))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}