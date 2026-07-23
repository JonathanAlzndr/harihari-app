package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class TaskEvent {
    data object NavigateBack : TaskEvent()
    data class ShowSnackbar(val message: String): TaskEvent()
}