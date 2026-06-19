package com.alezandrow.simplecleanarchitecture.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.TaskStatus
import com.alezandrow.simplecleanarchitecture.domain.usecase.AddNewTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.ChangeTaskStatusUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.DeleteTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.GetAllTasksUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addNewTaskUseCase: AddNewTaskUseCase,
    private val changeTaskStatusUseCase: ChangeTaskStatusUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    val taskUiState = getAllTasksUseCase()
        .map { UiState.Success(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                status = if (task.status == TaskStatus.DONE) TaskStatus.NEW else TaskStatus.DONE
            )
            changeTaskStatusUseCase(updatedTask)
        }
    }

    fun addNewTask(task: Task) {
        viewModelScope.launch {
            addNewTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }
}