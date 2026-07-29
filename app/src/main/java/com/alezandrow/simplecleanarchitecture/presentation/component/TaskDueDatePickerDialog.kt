package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.alezandrow.simplecleanarchitecture.presentation.util.getTodayMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDueDatePickerDialog(
    initialDate: Long?,
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit,
) {

    val todayMillis = getTodayMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate,
        selectableDates = object: SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(
                        datePickerState.selectedDateMillis
                    )
                },
            ) {
                Text("Next")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}