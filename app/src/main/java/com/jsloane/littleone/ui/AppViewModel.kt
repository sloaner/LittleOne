package com.jsloane.littleone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.observers.AuthStateObserver
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.usecases.GetFamilyIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.shareIn

@HiltViewModel
class AppViewModel @Inject constructor(
    appSettingsRepository: AppSettingsRepository,
    authStateObserver: AuthStateObserver,
    familyIdUseCase: GetFamilyIdUseCase
) : ViewModel() {

    val userId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    private val familyIdFlow = familyIdUseCase(GetFamilyIdUseCase.Params(userId))

    val appState = combine(
        authStateObserver.flow.filterIsInstance<Result.Success<Boolean>>(),
        familyIdFlow.filterIsInstance<Result.Success<String>>()
    ) { auth, family ->
        LittleOneAppState(
            isAuthenticated = auth.data,
            isOnboarded = family.data.isNotBlank()
        )
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        authStateObserver(UseCase.Params.Empty)
    }
}
