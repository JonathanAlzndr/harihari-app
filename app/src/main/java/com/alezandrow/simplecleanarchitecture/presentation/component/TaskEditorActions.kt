package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskEditorMode
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun TaskEditorActions(
    mode: TaskEditorMode,
    isSaving: Boolean,
    isDeleting: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {

        Button(
            onClick = onSave,
            enabled = !isSaving && !isDeleting,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = when(mode) {
                        TaskEditorMode.CREATE -> "Create Task"
                        TaskEditorMode.EDIT -> "Save Changes"
                    }
                )
            }
        }

        if(mode == TaskEditorMode.EDIT) {
            TextButton(
                onClick = {
                    onDelete()
                },
                enabled = !isSaving && !isDeleting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if(isDeleting) {
                        "Deleting..."
                    } else {
                        "Delete Task"
                    },
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}