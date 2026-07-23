package com.alezandrow.simplecleanarchitecture.presentation.screen.register

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.component.EmailInputText
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.PasswordInputText
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.theme.SimpleCleanArchitectureTheme
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    navigateToVerify: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(authUiState) {
        when (authUiState) {
            AuthUiState.Success -> {
                navigateToVerify(formState.email)
            }

            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(
                    (authUiState as AuthUiState.Error).message
                )
            }

            else -> Unit
        }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(Spacing.lg)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Image(
                painter = painterResource(R.drawable.app_logo),
                modifier = Modifier.size(160.dp),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            Text(
                text = stringResource(R.string.sign_up_headline),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            Text(
                text = stringResource(R.string.sign_up_supporting),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            EmailInputText(
                value = formState.email,
                isError = formState.emailError != null,
                onValueChange = { viewModel.onEmailChanged(it) },
                errorMessage = formState.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            PasswordInputText(
                value = formState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Password",
                isError = formState.passwordError != null,
                errorMessage = formState.passwordError,
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            PasswordInputText(
                value = formState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = "Confirm Password",
                imeAction = ImeAction.Done,
                isError = formState.confirmPasswordError != null,
                errorMessage = formState.confirmPasswordError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            Button(
                onClick = {
                    viewModel.signUp(context)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = authUiState !is AuthUiState.Loading
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            TextButton(
                onClick = navigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.have_account),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        if (authUiState is AuthUiState.Loading) {
            LoadingLayout(modifier = Modifier.fillMaxSize())
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SimpleCleanArchitectureTheme {
        RegisterScreen({},{})
    }
}