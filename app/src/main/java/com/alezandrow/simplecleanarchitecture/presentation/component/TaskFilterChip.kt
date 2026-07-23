package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.presentation.icon.check_circle_filled

@Composable
fun TaskFilterChip(
    selectedPriority: TaskPriority?,
    onClick: (TaskPriority) -> Unit,
    modifier: Modifier = Modifier
) {
    TaskPriority.entries.forEach { priority ->
        val isSelected = selectedPriority == priority
        FilterChip(
            selected = isSelected,
            onClick = { onClick(priority) },
            label = { Text(priority.name) },
            leadingIcon = {
                if (isSelected) {
                    Icon(
                        imageVector = check_circle_filled,
                        contentDescription = null,
                    )
                }
            }
        )
    }
}