package com.alezandrow.simplecleanarchitecture.presentation.state

import com.alezandrow.simplecleanarchitecture.domain.entities.Task

sealed class UiState {
    data class Error(val message: String) : UiState()
    data object Loading : UiState()
    data class Success(val tasks: List<Task>): UiState()
}