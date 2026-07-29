package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskEditorMode
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun TaskEditorIntro(mode: TaskEditorMode) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Text(
            text = when(mode) {
                TaskEditorMode.CREATE -> stringResource(R.string.add_task_headline)
                TaskEditorMode.EDIT -> stringResource(R.string.edit_task_headline)
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = when(mode) {
                TaskEditorMode.CREATE -> stringResource(R.string.add_task_support)
                TaskEditorMode.EDIT -> stringResource(R.string.edit_task_support)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}