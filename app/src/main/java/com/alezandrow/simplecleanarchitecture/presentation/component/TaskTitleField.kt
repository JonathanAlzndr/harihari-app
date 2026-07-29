package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun TaskTitleField(
    value: String,
    isError: Boolean,
    errorMessage: String?,
    onValueChange: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Text(
            text = "Task title",
            style = MaterialTheme.typography.labelLarge,
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = if(isError && errorMessage != null) errorMessage else "What needs to be done?"
                )
            },
            isError = isError,
            singleLine = true,
        )
    }
}