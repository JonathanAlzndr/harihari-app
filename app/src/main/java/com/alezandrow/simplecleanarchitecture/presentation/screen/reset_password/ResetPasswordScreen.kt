package com.alezandrow.simplecleanarchitecture.presentation.screen.reset_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.component.EmailInputText

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {

    val emailFormState by viewModel.emailFormState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        EmailInputText(
            value = emailFormState.email,
            onValueChange = viewModel::onChangeEmail,
            modifier = Modifier.fillMaxWidth(),
            isError = emailFormState.emailError != null,
            errorMessage = emailFormState.emailError
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = viewModel::requestPasswordReset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Request to Reset Password"
            )
        }

    }
}