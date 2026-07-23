package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class OperationUiState {
    data object Idle : OperationUiState()
    data object Loading : OperationUiState()
}

