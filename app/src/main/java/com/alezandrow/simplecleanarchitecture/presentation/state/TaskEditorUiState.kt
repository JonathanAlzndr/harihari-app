package com.alezandrow.simplecleanarchitecture.presentation.state

import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus

data class TaskEditorUiState(
    val taskId: String? = null,
    val mode: TaskEditorMode = TaskEditorMode.CREATE,
    val title: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val status: TaskStatus = TaskStatus.NEW,
    val dueDateTime: Long? = null,
    val operation: OperationUiState = OperationUiState.Idle,
    val isLoading: Boolean = false,
    val isDeleteLoading: Boolean = false,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val dueDateTimeError: Long? = null,
)
