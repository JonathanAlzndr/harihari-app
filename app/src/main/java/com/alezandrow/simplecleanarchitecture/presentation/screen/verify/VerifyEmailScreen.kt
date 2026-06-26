package com.alezandrow.simplecleanarchitecture.presentation.screen.verify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun VerifyEmailScreen(
    email: String,
    viewModel: VerifyEmailViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verify Your Email",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your account has been created successfully.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "We've sent a verification email to:",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = email,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Please open your inbox and click the verification link before signing in.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Didn't receive the email? Check your spam folder or resend the verification email.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = viewModel::resendEmail,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Resend Email")
        }

        Button(
            onClick = {
                viewModel.refreshCurrentUser()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("I'm Verified")
        }
    }
}