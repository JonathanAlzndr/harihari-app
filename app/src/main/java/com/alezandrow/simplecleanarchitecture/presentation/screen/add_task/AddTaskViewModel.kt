package com.alezandrow.simplecleanarchitecture.presentation.screen.add_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.AddNewTaskUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.OperationUiState
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addNewTaskUseCase: AddNewTaskUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<OperationUiState>(OperationUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var _uiEvent = MutableSharedFlow<AppEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun addNewTask(task: Task) {
        _uiState.value = OperationUiState.Loading

        viewModelScope.launch {
            when (addNewTaskUseCase(task)) {
                is AppResult.Error -> {
                    _uiState.value = OperationUiState.Idle
                    _uiEvent.emit(
                        AppEvent.ShowSnackbar("Failed to Add Task")
                    )
                }

                is AppResult.Success -> {
                    _uiState.value = OperationUiState.Idle
                    _uiEvent.emit(
                        AppEvent.ShowSnackbar("Task Added Successfully")
                    )
                }
            }
        }
    }
}