package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskStatus

@Composable
fun TaskStatusSelector(status: TaskStatus, onStatusChange: (TaskStatus) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = status == TaskStatus.DONE,
            onCheckedChange = { checked ->
                onStatusChange(
                    if(checked) {
                        TaskStatus.DONE }
                    else    {
                        TaskStatus.NEW
                    }
                )
            }
        )
        Text(text = "Mark as completed")
    }
}