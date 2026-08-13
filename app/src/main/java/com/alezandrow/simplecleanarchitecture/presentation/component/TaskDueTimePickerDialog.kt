package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.util.toLocalTimeAtSystemZone
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDueTimePickerDialog(
    initialDateTime: Long?,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {

    val initialLocalTime = remember(initialDateTime) {
        initialDateTime?.toLocalTimeAtSystemZone() ?: LocalTime.now()
    }

    val timePickerState = rememberTimePickerState(
        initialHour = initialLocalTime.hour,
        initialMinute = initialLocalTime.minute,
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
                Text(text = stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
            )
        },
    )
}