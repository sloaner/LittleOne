package com.jsloane.littleone.ui.view.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsloane.littleone.base.InvokeStatus
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.observers.ObserveAuthState
import com.jsloane.littleone.domain.usecases.GetFamilyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    observeAuthState: ObserveAuthState,
    getFamily: GetFamilyUseCase
) : ViewModel() {
    private val pendingActions = MutableSharedFlow<FeedAction>()

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    private val loadingState = MutableStateFlow<InvokeStatus>(InvokeStatus.Idle)

    val state: StateFlow<FeedViewState> = combine(
        observeAuthState.flow,
        email,
        password
    ) { authState, email, password ->
        FeedViewState(
            email = email,
            password = password
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedViewState.Empty
    )

    init {
        observeAuthState(UseCase.Params.Empty)

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    else -> {
                    }
                }
            }
        }
    }

    fun submitAction(action: FeedAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
