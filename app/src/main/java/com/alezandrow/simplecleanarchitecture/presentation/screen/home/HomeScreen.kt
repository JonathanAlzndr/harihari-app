package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
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
import com.alezandrow.simplecleanarchitecture.presentation.component.HomeHeader
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskColumnLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskSearchBar
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskListUiState
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val taskUiState by viewModel.taskListUiState.collectAsStateWithLifecycle()
    val currentPriority by viewModel.selectedPriority.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is AppEvent.ShowSnackbar) {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        HomeHeader()

        TaskSearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier.fillMaxWidth(),
        )

        when (val state = taskUiState) {
            TaskListUiState.Loading -> {
                LoadingLayout(
                    modifier = Modifier.weight(1f),
                )
            }

            is TaskListUiState.Error -> {
                ErrorLayout(
                    message = state.message,
                    modifier = Modifier.weight(1f),
                )
            }

            is TaskListUiState.Success -> {
                TaskColumnLayout(
                    tasks = state.tasks,
                    selectedPriority = currentPriority,
                    onPrioritySelected = viewModel::setFilterPriority,
                    onClickAction = viewModel::toggleTaskStatus,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}