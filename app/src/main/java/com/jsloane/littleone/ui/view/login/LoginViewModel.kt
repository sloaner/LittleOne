package com.jsloane.littleone.ui.view.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.observers.AuthStateObserver
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
import kotlinx.coroutines.tasks.await

@HiltViewModel
class LoginViewModel @Inject constructor(
    authStateObserver: AuthStateObserver,
    getFamily: GetFamilyUseCase
) : ViewModel() {
    private val pendingActions = MutableSharedFlow<LoginAction>()

    private val pendingNavigation = MutableStateFlow<LoginAction?>(null)
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    private val loadingState = MutableStateFlow<Result<Unit>>(Result.Loading())

    val state: StateFlow<LoginViewState> = combine(
        pendingNavigation,
        authStateObserver.flow,
        email,
        password
    ) { navigation, authState, email, password ->
        LoginViewState(
            pendingNavigation = navigation,
            email = email,
            password = password
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginViewState.Empty
    )

    init {
        authStateObserver(UseCase.Params.Empty)

        viewModelScope.launch {
            authStateObserver.flow.collect {
                when (it) {
                    is Result.Error -> TODO()
                    is Result.Loading -> TODO()
                    is Result.Success -> if (it.data) {
                        getFamily(
                            GetFamilyUseCase.Params(
                                user_id = Firebase.auth.currentUser?.uid.orEmpty()
                            )
                        ).collect { family ->
                            if (family == null) {
                                pendingNavigation.emit(LoginAction.OpenOnboarding)
                            } else {
                                pendingNavigation.emit(LoginAction.OpenActivityLog)
                            }
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is LoginAction.UpdateEmail -> email.emit(action.email)
                    is LoginAction.UpdatePassword -> password.emit(action.password)
                    is LoginAction.SignInEmail -> signInWithEmailAndPassword(
                        email.value,
                        password.value
                    )
                    is LoginAction.SignInToken -> signInWithCredentials(action.intent)
                    else -> {
                    }
                }
            }
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) =
        viewModelScope.launch {
            try {
                loadingState.emit(Result.Loading())

                Firebase.auth.signInWithEmailAndPassword(email, password).await()

                loadingState.emit(Result.Success(Unit))
            } catch (e: Exception) {
                loadingState.emit(Result.Error(e.message.orEmpty()))
            }
        }

    private fun signInWithCredentials(intent: Intent) = viewModelScope.launch {
        try {
            loadingState.emit(Result.Loading())

            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            Firebase.auth.signInWithCredential(credential).await()

            loadingState.emit(Result.Success(Unit))
        } catch (e: Exception) {
            loadingState.emit(Result.Error(e.message.orEmpty()))
        }
    }

    fun submitAction(action: LoginAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
