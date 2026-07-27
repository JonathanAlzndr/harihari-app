package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.component.ErrorLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskColumnLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskSearchBar
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskListUiState
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun HomeScreen(snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier, viewModel: HomeViewModel) {

    val taskUiState by viewModel.taskListUiState.collectAsStateWithLifecycle()
    val currentPriority by viewModel.selectedPriority.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when(it) {
                is AppEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)
                }
                else -> Unit
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(Spacing.md)) {
        TaskSearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier.fillMaxWidth()
        )

        when (val state = taskUiState) {
            is TaskListUiState.Error -> ErrorLayout(
                message = state.message,
                modifier = modifier.fillMaxSize()
            )

            TaskListUiState.Loading -> LoadingLayout(modifier = modifier.fillMaxSize())
            is TaskListUiState.Success -> {
                TaskColumnLayout(
                    tasks = state.tasks,
                    selectedPriority = currentPriority,
                    onPrioritySelectedAction = viewModel::setFilterPriority,
                    onClickAction = viewModel::toggleTaskStatus,
                    onDeleteTask = viewModel::deleteTask,
                )
            }
        }
    }

}