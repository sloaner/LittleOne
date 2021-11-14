package com.jsloane.littleone.ui.view.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsloane.littleone.base.InvokeStatus
import com.jsloane.littleone.domain.LOFirestore
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

    private val pendingNavigation = MutableStateFlow<FeedAction?>(null)
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    private val loadingState = MutableStateFlow<InvokeStatus>(InvokeStatus.Idle)

    val state: StateFlow<FeedViewState> = combine(
        pendingNavigation,
        observeAuthState.flow,
        email,
        password
    ) { navigation, authState, email, password ->
        FeedViewState(
            pendingNavigation = navigation,
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
            observeAuthState.flow.collect {
                if (it) {
                    getFamily(UseCase.Params.Empty).collect { user ->
                        if (LOFirestore.Users.Field.family == null) {
                            pendingNavigation.emit(FeedAction.OpenOnboarding)
                        } else {
                            pendingNavigation.emit(FeedAction.OpenActivityLog)
                        }
                    }
                }
            }
        }

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
