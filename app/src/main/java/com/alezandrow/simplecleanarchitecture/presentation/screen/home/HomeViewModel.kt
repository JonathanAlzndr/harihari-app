package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.AddNewTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.ChangeTaskStatusUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.DeleteTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.GetAllTasksUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskUiState
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
        .map { TaskUiState.Success(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TaskUiState.Loading
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