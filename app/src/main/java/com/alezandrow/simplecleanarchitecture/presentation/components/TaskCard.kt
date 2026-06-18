package com.alezandrow.simplecleanarchitecture.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alezandrow.simplecleanarchitecture.domain.entities.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.TaskStatus

@Composable
fun TaskCard(task: Task, onClick: (Task) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(32.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(modifier.fillMaxWidth()) {
            Box(
                modifier
                    .weight(1f)
                    .background(Color.Gray)
                    .clip(CircleShape)
                    .size(30.dp)
            ) {
                Text(
                    text = task.description[0].toString()
                )
            }

            Spacer(Modifier.width(4.dp))

            Text(
                text = task.description,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(4.dp))

            IconToggleButton(
                checked = task.status == TaskStatus.DONE,
                onCheckedChange = { onClick(task) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (task.status == TaskStatus.DONE) {
                        Icons.Filled.CheckCircle
                    } else {
                        Icons.Outlined.CheckCircle
                    },
                    contentDescription = if (task.status == TaskStatus.DONE)
                        "Checked" else "Unchecked"
                )
            }
        }
    }
}