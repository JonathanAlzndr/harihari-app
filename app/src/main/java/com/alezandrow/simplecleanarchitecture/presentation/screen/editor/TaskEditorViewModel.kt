package com.alezandrow.simplecleanarchitecture.presentation.screen.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.AddNewTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.DeleteTaskUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.GetTaskByIdUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.UpdateTaskUseCase
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.OperationUiState
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskEditorMode
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskEditorUiState
import com.alezandrow.simplecleanarchitecture.presentation.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    private val addNewTaskUseCase: AddNewTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskEditorUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        val route = savedStateHandle.toRoute<Destination.TaskEditorRoute>()
        val taskId = route.taskId

        if (taskId != null) {
            _uiState.update { it.copy(taskId = taskId, mode = TaskEditorMode.EDIT) }
            getTaskById()
        }
    }

    fun updateTitle(value: String) {
        _uiState.update {
            it.copy(title = value)
        }
    }

    fun updateDescription(value: String) {
        _uiState.update {
            it.copy(description = value)
        }
    }

    fun updatePriority(priority: TaskPriority) {
        _uiState.update {
            it.copy(priority = priority)
        }
    }

    fun updateDueDate(date: Long?) {
        _uiState.update {
            it.copy(dueDateTime = date)
        }
    }

    fun updateStatus(status: TaskStatus) {
        _uiState.update {
            it.copy(status = status)
        }
    }

    fun saveTask() {
        val state = _uiState.value

        if (!validateForm(state)) {
            return
        }

        val task = Task(
            id = state.taskId ?: "",
            title = state.title.trim(),
            description = state.description.trim(),
            taskStatus = state.status,
            dueDate = state.dueDateTime,
            priority = state.priority,
        )

        when (state.mode) {
            TaskEditorMode.CREATE -> createTask(task)
            TaskEditorMode.EDIT -> updateTask(task)
        }
    }


    fun getTaskById() {
        viewModelScope.launch {
            val currentState = _uiState.value
            when (val task = getTaskByIdUseCase(currentState.taskId ?: "")) {
                is AppResult.Error -> {
                    _uiEvent.emit(AppEvent.ShowSnackbar(mapAppErrorToMessage(task.error)))
                }

                is AppResult.Success<Task> -> {
                    val existingTask = task.data
                    _uiState.update {
                        it.copy(
                            taskId = existingTask.id,
                            dueDateTime = existingTask.dueDate,
                            title = existingTask.title,
                            status = existingTask.taskStatus,
                            description = existingTask.description,
                            priority = existingTask.priority
                        )
                    }
                }
            }
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(operation = OperationUiState.Saving)
            }

            when (addNewTaskUseCase(task)) {
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(operation = OperationUiState.Idle)
                    }

                    _uiEvent.emit(AppEvent.ShowSnackbar("Failed to add task"))

                }

                is AppResult.Success<*> -> {
                    _uiState.update {
                        it.copy(operation = OperationUiState.Idle)
                    }

                    _uiEvent.emit(
                        AppEvent.ShowSnackbar("Task added successfully")
                    )
                }
            }
        }
    }


    fun updateTask(task: Task) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(operation = OperationUiState.Saving)
            }
            when (updateTaskUseCase(task)) {
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(operation = OperationUiState.Idle)
                    }
                    _uiEvent.emit(AppEvent.ShowSnackbar("Failed update task"))
                }

                is AppResult.Success<*> -> {
                    _uiState.update {
                        it.copy(operation = OperationUiState.Idle)
                    }
                    _uiEvent.emit(AppEvent.ShowSnackbar("Task updated successfully"))
                }
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(operation = OperationUiState.Deleting)
            }
            val taskId = _uiState.value.taskId
            if (taskId != null) {
                when (deleteTaskUseCase(taskId)) {
                    is AppResult.Error -> {
                        _uiState.update {
                            it.copy(operation = OperationUiState.Idle)
                        }
                        _uiEvent.emit(AppEvent.ShowSnackbar("Failed to delete task"))
                    }

                    is AppResult.Success<*> -> {
                        _uiState.update {
                            it.copy(operation = OperationUiState.Idle)
                        }
                        _uiEvent.emit(AppEvent.ShowSnackbar("Success to delete task"))
                    }
                }
            }
            _uiEvent.emit(AppEvent.ShowSnackbar("Failed to delete task"))
        }
    }

    private fun validateForm(state: TaskEditorUiState): Boolean {
        val titleError = if (state.title.isBlank()) {
            "Title can't be empty"
        } else {
            null
        }

        val descriptionError = if (state.description.isBlank()) {
            "Description can't be empty"
        } else {
            null
        }

        _uiState.update {
            it.copy(
                titleError = titleError,
                descriptionError = descriptionError,
            )
        }

        return titleError == null && descriptionError == null
    }


}