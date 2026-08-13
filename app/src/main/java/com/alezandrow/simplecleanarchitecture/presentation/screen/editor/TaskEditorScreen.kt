package com.alezandrow.simplecleanarchitecture.presentation.screen.editor

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.component.AlarmPermissionDialog
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
    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && !viewModel.hasExactAlarmPermission()) {
            viewModel.updateAlarmPermissionVisibility(true)
        } else {
            viewModel.saveTask()
        }
    }

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
                viewModel.updateDatePickerVisibility(true)
            },
            modifier = Modifier.weight(1f),
        )

        TaskEditorActions(
            mode = uiState.mode,
            isSaving = uiState.operation is OperationUiState.Saving,
            isDeleting = uiState.operation is OperationUiState.Deleting,
            onSave = {
                when {
                    !viewModel.hasNotificationPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }

                    !viewModel.hasExactAlarmPermission() -> {
                        viewModel.updateAlarmPermissionVisibility(true)
                    }

                    else -> {
                        viewModel.saveTask()
                    }
                }
            },
            onDelete = { viewModel.updateConfirmationDialogVisibility(true) }
        )
    }

    if (uiState.showConfirmationDialog) {
        ConfirmationDialog(
            onDismissRequest = { viewModel.updateConfirmationDialogVisibility(false) },
            onConfirmation = {
                viewModel.deleteTask()
                viewModel.updateConfirmationDialogVisibility(false)
            },
            dialogTitle = stringResource(R.string.task_delete_confirmation),
            dialogText = stringResource(R.string.task_deleted_cannot_be_restored),
            icon = warning_icon
        )
    }

    if (uiState.showAlarmPermission) {
        AlarmPermissionDialog(
            onOpenSettings = {
                viewModel.requestExactAlarmPermission()
                viewModel.updateAlarmPermissionVisibility(false)
                viewModel.saveTask()
            },
            onDismiss = {
                viewModel.updateAlarmPermissionVisibility(false)
                viewModel.saveTask()
            }
        )
    }

    if (uiState.showDatePicker) {
        TaskDueDatePickerDialog(
            initialDate = uiState.dueDateTime,
            onDismiss = {
                viewModel.updateDatePickerVisibility(false)
            },
            onDateSelected = { selectedDate ->
                if (selectedDate != null) {
                    selectedDateMillis = selectedDate
                    viewModel.updateDatePickerVisibility(false)
                    viewModel.updateTimePickerVisibility(true)
                }
            }
        )
    }

    if (uiState.showTimePicker) {
        TaskDueTimePickerDialog(
            onDismiss = {
                viewModel.updateTimePickerVisibility(false)
            },
            initialDateTime = uiState.dueDateTime,
            onTimeSelected = { hour, minute ->
                selectedDateMillis?.let { dateMillis ->
                    val dateTime = combineDateAndTime(dateMillis, hour, minute)
                    viewModel.updateDueDate(dateTime)
                }

                selectedDateMillis = null
                viewModel.updateTimePickerVisibility(false)
            }
        )
    }
}