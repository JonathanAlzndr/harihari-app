package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDueTimePickerDialog(
    initialDateTime: Long?,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {
    val calendar = remember(initialDateTime) {
        Calendar.getInstance().apply {
            if(initialDateTime != null) {
                timeInMillis = initialDateTime
            }
        }
    }

    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(
                        timePickerState.hour,
                        timePickerState.minute,
                    )
                },
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
            )
        },
    )
}