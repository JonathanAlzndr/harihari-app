package com.alezandrow.simplecleanarchitecture.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.presentation.components.ErrorLayout
import com.alezandrow.simplecleanarchitecture.presentation.components.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.components.TaskColumnLayout
import com.alezandrow.simplecleanarchitecture.presentation.state.UiState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel) {

    val taskUiState by viewModel.taskUiState.collectAsState()

    when (val state = taskUiState) {
        is UiState.Error -> ErrorLayout(message = state.message, modifier = modifier.fillMaxSize())
        UiState.Loading -> LoadingLayout(modifier = modifier.fillMaxSize())
        is UiState.Success -> TaskColumnLayout(
            tasks = state.tasks,
            onClickAction = viewModel::toggleTask,
            onDeleteTask = viewModel::deleteTask,
            modifier = modifier.fillMaxSize(),
        )
    }

}