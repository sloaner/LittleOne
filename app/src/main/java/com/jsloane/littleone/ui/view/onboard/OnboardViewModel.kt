package com.jsloane.littleone.ui.view.onboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsloane.littleone.base.InvokeStatus
import com.jsloane.littleone.domain.usecases.CreateChildUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
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
class OnboardViewModel @Inject constructor(
    private val createChildUseCase: CreateChildUseCase
) : ViewModel() {
    private val pendingActions = MutableSharedFlow<OnboardAction>()

    private val family_name = MutableStateFlow("")
    private val join_code = MutableStateFlow("")

    private val loadingState = MutableStateFlow<InvokeStatus>(InvokeStatus.Idle)

    val state: StateFlow<OnboardViewState> = combine(
        family_name,
        join_code
    ) { name, code ->
        OnboardViewState(
            family_name = name,
            join_code = code
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OnboardViewState.Empty
    )

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is OnboardAction.CreateFamily -> registerNewFamily(
                        action.childName,
                        action.childBirthday
                    )
                    OnboardAction.OpenActivityLog -> TODO()
                }
            }
        }
    }

    private fun registerNewFamily(name: String, birthday: LocalDate) = viewModelScope.launch {
        try {
            loadingState.emit(InvokeStatus.Started)

            createChildUseCase(CreateChildUseCase.Params(name = name, birthday = birthday))
                .collect {
                    Log.d("I", it?.id ?: "null")
                }

            loadingState.emit(InvokeStatus.Success)
        } catch (e: Exception) {
            loadingState.emit(InvokeStatus.Error(e))
        }
    }

    fun submitAction(action: OnboardAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
