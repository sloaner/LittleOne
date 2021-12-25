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
import com.jsloane.littleone.navigation.Destination
import com.jsloane.littleone.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
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
    val babyName = MutableStateFlow("")
    val babyBirthday = MutableStateFlow("")
    val inviteCode = MutableStateFlow("")

    private val loadingState = MutableStateFlow<Result<Unit>>(Result.Loading())

    val state: StateFlow<OnboardViewState> = combine(
        babyName,
        babyBirthday,
        inviteCode
    ) { name, birthday, code ->
        OnboardViewState(
            baby_name = name,
            baby_birthday = birthday,
            invite_code = code,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OnboardViewState.Empty
    )

    fun submitAction(action: OnboardAction) {
        viewModelScope.launch {
            when (action) {
                OnboardAction.OpenActivityLog -> NavigationManager.navigate(Destination.Feed)

                is OnboardAction.UpdateBabyName -> babyName.emit(action.name)
                is OnboardAction.UpdateBabyBirthday -> babyBirthday.emit(action.birthday)
                is OnboardAction.UpdateInviteCode -> inviteCode.emit(action.inviteCode)

                is OnboardAction.JoinFamily -> joinFamily(action.inviteCode)
                is OnboardAction.CreateFamily -> registerNewFamily(
                    action.childName,
                    action.childBirthday
                )
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
                    is Result.Error -> {}
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
                    is Result.Loading -> {}
                    is Result.Success -> {
                        NavigationManager.navigate(Destination.Feed)
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
                if (it is Result.Success<Unit>)
                    NavigationManager.navigate(Destination.Feed)
            }
        } catch (e: Exception) {
        }
    }
}
