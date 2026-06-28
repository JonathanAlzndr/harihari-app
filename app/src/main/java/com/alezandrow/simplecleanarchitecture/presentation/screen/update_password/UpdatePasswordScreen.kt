package com.alezandrow.simplecleanarchitecture.presentation.screen.update_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.component.PasswordInputText
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState

@Composable
fun UpdatePasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: UpdatePasswordViewModel = hiltViewModel()
) {

    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val uiState by viewModel.authUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(
                    (uiState as AuthUiState.Error).message
                )
            }

            AuthUiState.Success -> {
                snackbarHostState.showSnackbar("Password Updated Successfully")
            }

            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        PasswordInputText(
            value = formState.currentPassword,
            onValueChange = viewModel::onCurrentPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            label = "Current Password",
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordInputText(
            value = formState.newPassword,
            onValueChange = viewModel::onNewPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            label = "New Password",
            isError = formState.newPasswordError != null,
            errorMessage = formState.newPasswordError
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordInputText(
            value = formState.confirmNewPassword,
            onValueChange = viewModel::onConfirmNewPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            label = "Confirm New Password",
            isError = formState.confirmNewPasswordError != null,
            errorMessage = formState.confirmNewPasswordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = viewModel::updatePassword
        ) {
            Text(text = "Update Password")
        }

    }
}