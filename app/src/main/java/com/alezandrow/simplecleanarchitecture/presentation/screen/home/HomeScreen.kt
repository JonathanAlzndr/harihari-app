package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.component.ErrorLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskColumnLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskSearchBar
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskUiState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel) {

    val taskUiState by viewModel.taskUiState.collectAsStateWithLifecycle()
    val currentPriority by viewModel.selectedPriority.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        TaskSearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        when (val state = taskUiState) {
            is TaskUiState.Error -> ErrorLayout(
                message = state.message,
                modifier = modifier.fillMaxSize()
            )

            TaskUiState.Loading -> LoadingLayout(modifier = modifier.fillMaxSize())
            is TaskUiState.Success -> {
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