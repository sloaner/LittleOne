package com.jsloane.littleone.ui.view.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.observers.AuthStateObserver
import com.jsloane.littleone.domain.usecases.GetFamilyUseCase
import com.jsloane.littleone.navigation.Destination
import com.jsloane.littleone.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val _snackbarFlow = MutableSharedFlow<String>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()

    private val loadingState = MutableStateFlow<Result<Unit>>(Result.Loading())

    val state: StateFlow<LoginViewState> = combine(
        authStateObserver.flow,
        email,
        password
    ) { authState, email, password ->
        LoginViewState(
            email = email,
            password = password,
            snackbar = snackbarFlow
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
                    is Result.Error -> {}
                    is Result.Loading -> {}
                    is Result.Success -> if (it.data) {
                        getFamily(
                            GetFamilyUseCase.Params(
                                user_id = Firebase.auth.currentUser?.uid.orEmpty()
                            )
                        ).collect { family ->
                            when (family) {
                                is Result.Loading -> {}
                                is Result.Error -> {
                                    submitAction(LoginAction.OpenOnboarding)
                                }
                                is Result.Success -> {
                                    submitAction(LoginAction.OpenActivityLog)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun submitAction(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                LoginAction.OpenOnboarding -> NavigationManager.navigate(Destination.Onboarding)
                LoginAction.OpenActivityLog -> NavigationManager.navigate(Destination.Feed)

                is LoginAction.UpdateEmail -> email.emit(action.email)
                is LoginAction.UpdatePassword -> password.emit(action.password)

                is LoginAction.SignInToken -> signInWithCredentials(action.intent)
                is LoginAction.SignInEmail -> signInWithEmailAndPassword(
                    email.value,
                    password.value
                )
                is LoginAction.ForgotPassword -> forgotPassword(email.value)
                is LoginAction.RegisterUser -> registerAccount(
                    action.user,
                    action.pass
                )
            }
        }
    }

    private fun registerAccount(email: String, password: String) {
        viewModelScope.launch {
            val res = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            println(res.user?.uid.orEmpty())
        }
    }

    private fun forgotPassword(email: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                println(_snackbarFlow.subscriptionCount.value)
                _snackbarFlow.emit("Enter an email address and try again")
            } else {
                try {
                    Firebase.auth.sendPasswordResetEmail(email).await()
                    _snackbarFlow.emit("Reset password email sent")
                } catch (e: Throwable) {
                    _snackbarFlow.emit(e.message ?: "Unable to send recovery email.")
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
                _snackbarFlow.emit(e.message.orEmpty())
                println(e.message.orEmpty())
            }
        }

    private fun signInWithCredentials(intent: Intent) = viewModelScope.launch {
        try {
            loadingState.emit(Result.Loading())

            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            Firebase.auth.signInWithCredential(credential).await()

            loadingState.emit(Result.Success(Unit))
        } catch (e: Exception) {
            _snackbarFlow.emit(e.message.orEmpty())
            println(e.message.orEmpty())
        }
    }
}
