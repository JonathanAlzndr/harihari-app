package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.presentation.icon.date_range
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing
import com.alezandrow.simplecleanarchitecture.presentation.util.toFormattedDate

@Composable
fun TaskDueDateSelector(dueDate: Long?, onClick: () -> Unit) {

    val formattedDate = remember(dueDate) {
        dueDate.toFormattedDate()
    }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Text(
            text = "Due date",
            style = MaterialTheme.typography.labelLarge
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(Spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = date_range,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(Spacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if(dueDate != null) {
                            formattedDate
                        } else "No due date",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if(dueDate == null) {
                        Text(
                            text = "Tap to select a date",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}