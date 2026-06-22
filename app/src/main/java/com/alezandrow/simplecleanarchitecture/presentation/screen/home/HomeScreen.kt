package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.presentation.components.ErrorLayout
import com.alezandrow.simplecleanarchitecture.presentation.components.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.components.TaskColumnLayout
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskUiState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel) {

    val taskUiState by viewModel.taskUiState.collectAsState()

    when (val state = taskUiState) {
        is TaskUiState.Error -> ErrorLayout(message = state.message, modifier = modifier.fillMaxSize())
        TaskUiState.Loading -> LoadingLayout(modifier = modifier.fillMaxSize())
        is TaskUiState.Success -> TaskColumnLayout(
            tasks = state.tasks,
            onClickAction = viewModel::toggleTask,
            onDeleteTask = viewModel::deleteTask,
            modifier = modifier.fillMaxSize(),
        )
    }

}