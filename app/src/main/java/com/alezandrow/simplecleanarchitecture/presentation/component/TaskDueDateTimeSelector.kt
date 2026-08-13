package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.alezandrow.simplecleanarchitecture.util.toFormattedDateTime

@Composable
fun TaskDueDateTimeSelector(
    dueDateTime: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDateTime  = remember(dueDateTime) {
        dueDateTime.toFormattedDateTime()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        Text(
            text = "Due date",
            style = MaterialTheme.typography.labelLarge
        )

        OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = date_range,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(Spacing.md))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Text(
                        text = if (dueDateTime != null) formattedDateTime
                        else "No due date",
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    if(dueDateTime == null) {
                        Text(
                            text = "Tap to select date & time",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}