package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class AppEvent {
    data object NavigateBack : AppEvent()
    data class ShowSnackbar(val message: String): AppEvent()
}