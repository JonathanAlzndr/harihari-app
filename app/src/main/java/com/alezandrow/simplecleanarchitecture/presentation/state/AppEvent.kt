package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class AppEvent {
    data object NavigateBack : AppEvent()
    data class ShowSnackbar(val message: String): AppEvent()
    data class ShowDialog(val message: String): AppEvent()
}