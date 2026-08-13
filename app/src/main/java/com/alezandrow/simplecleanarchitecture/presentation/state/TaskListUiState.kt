package com.alezandrow.simplecleanarchitecture.presentation.state

import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task

sealed class TaskListUiState {
    data class Error(val message: String) : TaskListUiState()
    data object Loading : TaskListUiState()
    data class Success(val tasks: List<Task>) : TaskListUiState()
}