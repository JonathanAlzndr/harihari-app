package com.alezandrow.simplecleanarchitecture.presentation.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.presentation.component.ConfirmationDialog
import com.alezandrow.simplecleanarchitecture.presentation.component.DangerZoneSection
import com.alezandrow.simplecleanarchitecture.presentation.component.ErrorLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.InfoRow
import com.alezandrow.simplecleanarchitecture.presentation.component.LoadingLayout
import com.alezandrow.simplecleanarchitecture.presentation.component.ProfileHeader
import com.alezandrow.simplecleanarchitecture.presentation.component.ProfileItem
import com.alezandrow.simplecleanarchitecture.presentation.icon.arrow_right_icon
import com.alezandrow.simplecleanarchitecture.presentation.icon.lock
import com.alezandrow.simplecleanarchitecture.presentation.icon.warning_icon
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.ProfileUiState
import com.alezandrow.simplecleanarchitecture.presentation.theme.Spacing

@Composable
fun ProfileScreen(
    snackbarHostState: SnackbarHostState,
    navigateToChangePassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.currentUser.collectAsStateWithLifecycle()
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AuthEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val user = uiState) {
            is ProfileUiState.Error -> ErrorLayout(user.message)

            ProfileUiState.Loading -> LoadingLayout()

            is ProfileUiState.Success -> {
                ProfileContent(
                    userData = user.userData,
                    onChangePasswordClick = navigateToChangePassword,
                    onDeleteAccountClick = { showAlertDialog = true }
                )

                if(showAlertDialog) {
                    ConfirmationDialog(
                        onDismissRequest = { showAlertDialog = false },
                        onConfirmation = {
                            viewModel.deleteAccount()
                            showAlertDialog = false
                        },
                        dialogTitle = "Are you sure to delete account?",
                        dialogText = "Associated task will be deleted and can't be restored",
                        icon = warning_icon
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    userData: AuthUser,
    onChangePasswordClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.md),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            photoUrl = userData.photoUrl?.toUri(),
            displayName = userData.displayName,
            email = userData.email
        )

        Spacer(Modifier.height(Spacing.lg))

        SectionLabel(text = stringResource(R.string.account_information))

        Spacer(Modifier.height(Spacing.sm))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs)) {
                InfoRow(label = stringResource(R.string.email_label), value = userData.email)
                InfoRow(
                    label = stringResource(R.string.sign_in_provider),
                    value = userData.providerId,
                    showDivider = false
                )
            }
        }

        Spacer(Modifier.height(Spacing.xl))

        SectionLabel(text = stringResource(R.string.account))

        Spacer(Modifier.height(Spacing.sm))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            ProfileItem(
                icon = lock,
                title = stringResource(R.string.update_password),
                onClick = onChangePasswordClick,
                trailingContent = { Icon(arrow_right_icon, contentDescription = null) }
            )
        }

        Spacer(Modifier.height(16.dp))

        DangerZoneSection(onDeleteAccountClick = onDeleteAccountClick)
    }
}

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = Spacing.xs)
    )
}
