package com.alezandrow.simplecleanarchitecture.presentation.screen.reset_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.component.EmailInputText
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthEvent
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun ResetPasswordScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {

    val emailFormState by viewModel.emailFormState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is AuthEvent.ShowSnackbar -> snackbarHostState.showSnackbar(uiEvent.message)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Image(
            painter = painterResource(R.drawable.forgot_password),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(Spacing.lg))

        Text(
            text = stringResource(R.string.reset_password_headline),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        Text(
            text = stringResource(R.string.reset_password_supporting),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.lg))

        EmailInputText(
            value = emailFormState.email,
            onValueChange = viewModel::onChangeEmail,
            modifier = Modifier.fillMaxWidth(),
            isError = emailFormState.emailError != null,
            errorMessage = emailFormState.emailError
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        Button(
            onClick = viewModel::requestPasswordReset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.send_reset_link)
            )
        }

    }
}