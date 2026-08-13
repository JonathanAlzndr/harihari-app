package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
private fun TaskPrioritySelector(
    selectedPriority: TaskPriority,
    onPrioritySelected: (TaskPriority?) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.labelLarge,
        )

        TaskPriorityFilter(
            selectedPriority = selectedPriority,
            onPrioritySelected = onPrioritySelected,
        )
    }
}