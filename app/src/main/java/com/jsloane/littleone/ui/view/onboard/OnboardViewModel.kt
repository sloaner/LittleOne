package com.jsloane.littleone.ui.view.onboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.usecases.CreateChildUseCase
import com.jsloane.littleone.domain.usecases.CreateFamilyUseCase
import com.jsloane.littleone.domain.usecases.JoinFamilyByInviteCodeUseCase
import com.jsloane.littleone.navigation.Screen
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
    private val createFamilyUseCase: CreateFamilyUseCase,
    private val createChildUseCase: CreateChildUseCase,
    private val joinFamilyByInviteCodeUseCase: JoinFamilyByInviteCodeUseCase
) : ViewModel() {
    private val pendingActions = MutableSharedFlow<OnboardAction>()

    val babyName = MutableStateFlow("")
    val babyBirthday = MutableStateFlow("")
    val inviteCode = MutableStateFlow("")
    val navigateTo = MutableStateFlow<Screen?>(null)

    private val loadingState = MutableStateFlow<Result<Unit>>(Result.Loading())

    val state: StateFlow<OnboardViewState> = combine(
        babyName,
        babyBirthday,
        inviteCode,
        navigateTo
    ) { name, birthday, code, navigate ->
        OnboardViewState(
            baby_name = name,
            baby_birthday = birthday,
            invite_code = code,
            navigateTo = navigate
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
                    is OnboardAction.JoinFamily -> joinFamily(
                        action.inviteCode
                    )
                    else -> {
                    }
                }
            }
        }
    }

    private fun registerNewFamily(name: String, birthday: LocalDate) = viewModelScope.launch {
        try {
            var familyId: String? = null
            createFamilyUseCase(
                CreateFamilyUseCase.Params(Firebase.auth.currentUser?.uid.orEmpty())
            ).collect {
                when (it) {
                    is Result.Error -> TODO()
                    is Result.Loading -> loadingState.emit(Result.Loading())
                    is Result.Success -> familyId = it.data
                }
            }
            createChildUseCase(
                CreateChildUseCase.Params(
                    name = name,
                    birthday = birthday,
                    family_id = familyId.orEmpty()
                )
            ).collect {
                when (it) {
                    is Result.Error -> loadingState.emit(Result.Error(it.message))
                    is Result.Loading -> TODO()
                    is Result.Success -> {
                        navigateTo.emit(Screen.Feed)
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun joinFamily(code: String) = viewModelScope.launch {
        try {
            loadingState.emit(Result.Loading())

            joinFamilyByInviteCodeUseCase(
                JoinFamilyByInviteCodeUseCase.Params(
                    inviteCode = code,
                    userId = Firebase.auth.currentUser?.uid.orEmpty()
                )
            ).collect {
                Log.d("I", "$it")
                navigateTo.emit(Screen.Feed)
            }
        } catch (e: Exception) {
        }
    }

    fun submitAction(action: OnboardAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
