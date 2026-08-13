package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class OperationUiState {
    data object Idle : OperationUiState()
    data object Loading : OperationUiState()
    data object Saving: OperationUiState()
    data object Deleting: OperationUiState()
}

