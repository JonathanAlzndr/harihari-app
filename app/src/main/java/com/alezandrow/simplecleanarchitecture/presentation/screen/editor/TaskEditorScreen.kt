package com.alezandrow.simplecleanarchitecture.presentation.screen.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.component.ConfirmationDialog
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskDueDatePickerDialog
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskDueTimePickerDialog
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskEditorActions
import com.alezandrow.simplecleanarchitecture.presentation.component.TaskEditorContent
import com.alezandrow.simplecleanarchitecture.presentation.icon.warning_icon
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.OperationUiState
import com.alezandrow.simplecleanarchitecture.presentation.util.combineDateAndTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AppEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        event.message,
                        actionLabel = "Back to Home",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) navigateBack()
                }

                else -> Unit
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TaskEditorContent(
            uiState = uiState,
            onTitleChange = viewModel::updateTitle,
            onDescriptionChange = viewModel::updateDescription,
            onStatusChange = viewModel::updateStatus,
            onPriorityChange = viewModel::updatePriority,
            onDueDateClick = {
                showDatePicker = true
            },
            modifier = Modifier.weight(1f),
        )

        TaskEditorActions(
            mode = uiState.mode,
            isSaving = uiState.operation is OperationUiState.Saving,
            isDeleting = uiState.operation is OperationUiState.Deleting,
            onSave = viewModel::saveTask,
            onDelete = { showAlertDialog = true }
        )
    }

    if (showAlertDialog) {
        ConfirmationDialog(
            onDismissRequest = { showAlertDialog = false },
            onConfirmation = {
                viewModel.deleteTask()
                showAlertDialog = false
            },
            dialogTitle = "Are you sure to delete task?",
            dialogText = "Deleted task can't be restored",
            icon = warning_icon
        )
    }

    if (showDatePicker) {
        TaskDueDatePickerDialog(
            initialDate = uiState.dueDateTime,
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { selectedDate ->
                if (selectedDate != null) {
                    selectedDateMillis = selectedDate
                    showDatePicker = false
                    showTimePicker = true
                }
            }
        )
    }

    if (showTimePicker) {
        TaskDueTimePickerDialog(
            onDismiss = {
                showTimePicker = false
            },
            initialDateTime = uiState.dueDateTime,
            onTimeSelected = { hour, minute ->
                selectedDateMillis?.let { dateMillis ->
                    val dateTime = combineDateAndTime(dateMillis, hour, minute)
                    viewModel.updateDueDate(dateTime)
                }

                selectedDateMillis = null
                showTimePicker = false
            }
        )
    }
}



