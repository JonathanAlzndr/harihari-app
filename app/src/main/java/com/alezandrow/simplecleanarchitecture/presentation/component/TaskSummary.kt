package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun TaskSummary(
    totalTasks: Int,
    completedTasks: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        SummaryItem(
            value = totalTasks,
            label = "Total tasks",
            modifier = Modifier.weight(1f),
        )

        SummaryItem(
            value = completedTasks,
            label = "Completed",
            modifier = Modifier.weight(1f),
        )
    }
}