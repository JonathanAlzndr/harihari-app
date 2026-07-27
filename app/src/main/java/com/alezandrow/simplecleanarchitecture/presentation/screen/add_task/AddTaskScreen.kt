package com.alezandrow.simplecleanarchitecture.presentation.screen.add_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.icon.check
import com.alezandrow.simplecleanarchitecture.presentation.icon.date_range
import com.alezandrow.simplecleanarchitecture.presentation.state.OperationUiState
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import com.alezandrow.simplecleanarchitecture.presentation.util.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTaskViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var description by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    val formattedDate = remember(dueDate) {
        dueDate.toFormattedDate()
    }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->

            if (event is AppEvent.ShowSnackbar) {
                val result = snackbarHostState.showSnackbar(
                    event.message,
                    "Back to home",
                    duration = SnackbarDuration.Indefinite
                )

                when (result) {
                    SnackbarResult.Dismissed -> Unit
                    SnackbarResult.ActionPerformed -> navigateBack()
                }
            }
        }
    }


    Box(modifier = modifier.fillMaxSize()) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Create New Task",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Priority",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskPriority.entries.forEach { priority ->
                            val isSelected = selectedPriority == priority
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedPriority = priority },
                                label = { Text(priority.name) },
                                leadingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = check,
                                            contentDescription = null,
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Task Description") },
                    placeholder = { Text("What needs to be done?") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showDatePicker = true },
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = date_range,
                            contentDescription = "Select Due Date",
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Due Date",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (dueDate != null) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        val task = Task(
                            title = title,
                            description = description,
                            taskStatus = TaskStatus.NEW,
                            dueDate = dueDate,
                            priority = selectedPriority
                        )
                        viewModel.addNewTask(task)
                    },
                    enabled = description.isNotBlank() && title.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Create Task", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dueDate = datePickerState.selectedDateMillis
                            showDatePicker = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }

    if (uiState == OperationUiState.Loading) {
        LoadingLayout(modifier = Modifier.fillMaxSize())
    }
}



