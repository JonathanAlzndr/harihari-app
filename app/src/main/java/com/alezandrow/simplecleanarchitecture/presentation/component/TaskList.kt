package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun TaskList(
    tasks: List<Task>,
    onClickAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tasks.isEmpty()) {
        EmptyTaskState(modifier = modifier.fillMaxWidth())
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        items(
            items = tasks,
            key = { task -> task.id }
        ) { task ->
            TaskItem(
                task = task,
                onClickAction = { onClickAction(task.id) }
            )
        }
    }
}