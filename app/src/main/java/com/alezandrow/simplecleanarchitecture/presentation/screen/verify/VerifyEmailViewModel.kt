package com.alezandrow.simplecleanarchitecture.presentation.screen.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.RefreshCurrentUserUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SendEmailVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val refreshCurrentUserUseCase: RefreshCurrentUserUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase
) : ViewModel() {

    fun refreshCurrentUser() {
        viewModelScope.launch {
           refreshCurrentUserUseCase()
        }
    }

    fun resendEmail() {
        viewModelScope.launch {
            sendEmailVerificationUseCase()
        }
    }

}