package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.presentation.helper.CredentialAuthHelper
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SignOutUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.GetTasksByTitleAndPriorityUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AppEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskListUiState
import com.alezandrow.simplecleanarchitecture.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getTasksByTitleAndPriority: GetTasksByTitleAndPriorityUseCase,
    private val credentialManager: CredentialManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedPriority = MutableStateFlow<TaskPriority?>(null)
    val selectedPriority = _selectedPriority.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val taskListUiState: StateFlow<TaskListUiState> = combine(
        _searchQuery.debounce(300L.milliseconds).distinctUntilChanged().onEach { Log.d("HomeVM", "query emit: $it")},
        _selectedPriority.onEach{ Log.d("HomeVM", "priority emit : $it")}
    ) { query, priority ->
        Log.d("HomeVM", "combine fired: $query, $priority")
        Pair(query, priority)
    }.flatMapLatest { (query, priority) ->
        Log.d("HomeVM", "flatMapLatest calling repo")
        getTasksByTitleAndPriority(query, priority)
    }.map { result ->
        when (result) {
            is AppResult.Success -> {
                TaskListUiState.Success(result.data)
            }

            is AppResult.Error -> {
                TaskListUiState.Error(mapAppErrorToMessage(result.error))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskListUiState.Loading
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterPriority(priority: TaskPriority?) {
        if (_selectedPriority.value == priority) {
            _selectedPriority.value = null
        } else {
            _selectedPriority.value = priority
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            signOutUseCase()

            val launcher = CredentialAuthHelper(context, credentialManager)
            when(val result = launcher.clearCredentialState()) {
                is AppResult.Error -> Log.e("SignOut", "clearCredentialState: ${result.error}")
                is AppResult.Success -> Log.d("SignOut", "clearCredentialState: success")
            }
        }
    }

}