package com.alezandrow.simplecleanarchitecture.presentation.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.component.EmailInputText
import com.alezandrow.simplecleanarchitecture.presentation.component.PasswordInputText
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState

@Composable
fun RegisterScreen(navigateToLogin: () -> Unit, modifier: Modifier = Modifier, viewModel: RegisterViewModel = hiltViewModel()) {

    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authUiState) {
        when (authUiState) {
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar((authUiState as AuthUiState.Error).message)
            }
            AuthUiState.Idle -> {}
            AuthUiState.Loading -> {}
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            EmailInputText(
                value = formState.email,
                isError = formState.emailError != null,
                onValueChange = { viewModel.onEmailChanged(it) },
                errorMessage = formState.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordInputText(
                value = formState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Password",
                isError = formState.passwordError != null,
                errorMessage = formState.passwordError,
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordInputText(
                value = formState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = "Confirm Password",
                imeAction = ImeAction.Done,
                isError = formState.passwordError != null,
                errorMessage = formState.passwordError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signUp() },
                modifier = Modifier.fillMaxWidth(),
                enabled = authUiState !is AuthUiState.Loading
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = navigateToLogin
            ) {
                Text(
                    text = "Already have an account? Go to Login",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        if (authUiState is AuthUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}