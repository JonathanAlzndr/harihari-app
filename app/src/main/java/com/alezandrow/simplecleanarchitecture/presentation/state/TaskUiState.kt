package com.alezandrow.simplecleanarchitecture.presentation.state

import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task

sealed class TaskUiState {
    data class Error(val message: String) : TaskUiState()
    data object Loading : TaskUiState()
    data class Success(val tasks: List<Task>): TaskUiState()
}