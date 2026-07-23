package com.alezandrow.simplecleanarchitecture.presentation.screen.update_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.PasswordInputText
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun UpdatePasswordScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: UpdatePasswordViewModel = hiltViewModel()
) {

    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val uiState by viewModel.authUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AuthEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.lg),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Image(
                painter = painterResource(R.drawable.update_password),
                contentDescription = null,
                modifier = Modifier.size(160.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            Text(
                text = stringResource(R.string.update_password),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            Text(
                text = stringResource(R.string.update_password_supporting),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            PasswordInputText(
                value = formState.currentPassword,
                onValueChange = viewModel::onCurrentPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = "Current Password",
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            PasswordInputText(
                value = formState.newPassword,
                onValueChange = viewModel::onNewPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = "New Password",
                isError = formState.newPasswordError != null,
                errorMessage = formState.newPasswordError
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            PasswordInputText(
                value = formState.confirmNewPassword,
                onValueChange = viewModel::onConfirmNewPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = "Confirm New Password",
                isError = formState.confirmNewPasswordError != null,
                errorMessage = formState.confirmNewPasswordError
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            Button(
                onClick = viewModel::updatePassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.update_password))
            }
        }

        if(uiState is AuthUiState.Loading) {
            LoadingLayout()
        }
    }
}