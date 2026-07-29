package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun TaskColumnLayout(
    tasks: List<Task>,
    selectedPriority: TaskPriority?,
    onPrioritySelected: (TaskPriority?) -> Unit,
    onClickAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {

        TaskSummary(
            totalTasks = tasks.size,
            completedTasks = tasks.count { it.taskStatus == TaskStatus.DONE }
        )

        TaskPriorityFilter(
            selectedPriority = selectedPriority,
            onPrioritySelected = onPrioritySelected,
        )

        TaskList(
            tasks = tasks,
            onClickAction = onClickAction
        )
    }
}