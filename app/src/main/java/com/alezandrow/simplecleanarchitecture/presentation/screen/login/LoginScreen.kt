package com.alezandrow.simplecleanarchitecture.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.component.EmailInputText
import com.alezandrow.simplecleanarchitecture.presentation.component.GoogleSignInButton
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.PasswordInputText
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun LoginScreen(
    navigateToRequestResetPassword: () -> Unit,
    navigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {

    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()
    val formState by viewModel.loginFormState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.signInWithAutoFill(context)
    }

    LaunchedEffect(authUiState) {
        if (authUiState is AuthUiState.Error) {
            snackbarHostState.showSnackbar((authUiState as AuthUiState.Error).message)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.md)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.md)
        ) {

            Image(
                painter = painterResource(R.drawable.app_logo),
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            Text(
                text = stringResource(R.string.sign_in_headline),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            Text(
                text = stringResource(R.string.sign_in_supporting),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            EmailInputText(
                value = formState.email,
                isError = formState.emailError != null,
                onValueChange = viewModel::onEmailChanged,
                errorMessage = formState.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            PasswordInputText(
                value = formState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = stringResource(R.string.password_label),
                isError = formState.passwordError != null,
                errorMessage = formState.passwordError,
                imeAction = ImeAction.Done,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.xs))

            TextButton(
                onClick = navigateToRequestResetPassword
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            Button(
                onClick = viewModel::signInManual,
                modifier = Modifier.fillMaxWidth(),
                enabled = authUiState !is AuthUiState.Loading
            ) {
                Text(text = stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(Spacing.lg))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(Spacing.lg))

            GoogleSignInButton(
                onClick = { viewModel.signInWithGoogle(context) }
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            TextButton(
                onClick = navigateToRegister
            ) {
                Text(
                    text = stringResource(R.string.no_account),
                    style = MaterialTheme.typography.bodyMedium
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