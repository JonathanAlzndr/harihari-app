package com.alezandrow.simplecleanarchitecture.presentation.screen.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.RefreshCurrentUserUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SendEmailVerificationUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import com.alezandrow.simplecleanarchitecture.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val refreshCurrentUserUseCase: RefreshCurrentUserUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase
) : ViewModel() {

    private val _timer = MutableStateFlow(60)
    val timer = _timer.asStateFlow()

    private val _verifyEvent = MutableSharedFlow<AppEvent>()
    val verifyEvent = _verifyEvent.asSharedFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    fun refreshCurrentUser() {
        viewModelScope.launch {
            val result = refreshCurrentUserUseCase()
            result?.isEmailVerified?.let {
                if(!it) {
                    _verifyEvent.emit(AppEvent.ShowSnackbar("Account not verified"))
                }
            }
        }
    }

    fun startTimer() {
        timerJob?.cancel()
        _timer.value = 60

        timerJob = viewModelScope.launch {
            while (_timer.value > 0) {
                delay(1000L)
                _timer.value -= 1
            }
        }
    }

    fun resendEmail() {
        viewModelScope.launch {
            when (val result = sendEmailVerificationUseCase()) {
                is AppResult.Error -> {
                    _verifyEvent.emit(AppEvent.ShowSnackbar(mapAppErrorToMessage(result.error)))
                }

                is AppResult.Success<*> -> {
                    _verifyEvent.emit(AppEvent.ShowSnackbar(result.data.toString()))
                    startTimer()
                }
            }
        }
    }

}