package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskEditorMode
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskEditorUiState
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun TaskEditorContent(
    uiState: TaskEditorUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (TaskPriority) -> Unit,
    onStatusChange: (TaskStatus) -> Unit,
    onDueDateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        TaskEditorIntro(
            mode = uiState.mode
        )

        TaskTitleField(
            value = uiState.title,
            isError = uiState.titleError != null,
            errorMessage = uiState.titleError,
            onValueChange = onTitleChange
        )

        TaskDescriptionField(
            value = uiState.description,
            isError = uiState.descriptionError != null,
            errorMessage = uiState.descriptionError,
            onValueChange = onDescriptionChange
        )

        TaskPriorityFilter(
            selectedPriority = uiState.priority,
            onPrioritySelected = onPriorityChange
        )

        TaskDueDateTimeSelector(
            dueDateTime = uiState.dueDateTime,
            onClick = onDueDateClick
        )

        if(uiState.mode == TaskEditorMode.EDIT) {
            TaskStatusSelector(uiState.status, onStatusChange = onStatusChange)
        }

    }
}