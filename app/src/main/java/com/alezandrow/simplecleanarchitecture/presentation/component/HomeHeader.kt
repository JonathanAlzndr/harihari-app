package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.common.GreetingType
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing
import com.alezandrow.simplecleanarchitecture.util.getGreeting

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
) {
    var greetingType by remember { mutableStateOf(getGreeting()) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Text(
            text = when(greetingType) {
                GreetingType.MORNING -> stringResource(R.string.greeting_morning)
                GreetingType.AFTERNOON -> stringResource(R.string.greeting_afternoon)
                GreetingType.EVENING -> stringResource(R.string.greeting_evening)
                GreetingType.NIGHT -> stringResource(R.string.greeting_night)
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = "Let's get things done today.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}