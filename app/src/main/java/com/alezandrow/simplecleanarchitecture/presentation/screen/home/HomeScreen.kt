package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.component.ErrorLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskColumnLayout
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskUiState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel) {

    val taskUiState by viewModel.taskUiState.collectAsStateWithLifecycle()

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