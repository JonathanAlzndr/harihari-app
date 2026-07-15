package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SignOutUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.AddNewTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.ChangeTaskStatusUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.DeleteTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.GetAllTasksUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.GetTaskByPriorityUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addNewTaskUseCase: AddNewTaskUseCase,
    private val changeTaskStatusUseCase: ChangeTaskStatusUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getTaskByPriorityUseCase: GetTaskByPriorityUseCase
) : ViewModel() {

    private val _selectedPriority = MutableStateFlow<TaskPriority?>(null)
    val selectedPriority = _selectedPriority.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val taskUiState: StateFlow<TaskUiState> = _selectedPriority
        .flatMapLatest { priority ->
            if (priority == null) {
                getAllTasksUseCase()
            } else {
                getTaskByPriorityUseCase(priority)
            }
        }.map { TaskUiState.Success(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState.Loading
        )

    fun setFilterPriority(priority: TaskPriority?) {
        if(_selectedPriority.value == priority) {
            _selectedPriority.value = null
        } else {
            _selectedPriority.value = priority
        }
    }

    fun toggleTaskStatus(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                taskStatus = if (task.taskStatus == TaskStatus.DONE) TaskStatus.NEW else TaskStatus.DONE
            )
            changeTaskStatusUseCase(updatedTask)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
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