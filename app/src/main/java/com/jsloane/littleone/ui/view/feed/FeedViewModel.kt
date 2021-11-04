package com.jsloane.littleone.ui.view.feed

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.InvokeStatus
import com.jsloane.littleone.domain.FirestoreCollection
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.observers.ObserveAuthState
import com.jsloane.littleone.domain.usecases.GetFamilyUseCase
import com.jsloane.littleone.ui.view.login.LoginAction
import com.jsloane.littleone.ui.view.login.LoginViewState
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
class FeedViewModel @Inject constructor(
    observeAuthState: ObserveAuthState,
    getFamily: GetFamilyUseCase
) : ViewModel() {
    private val pendingActions = MutableSharedFlow<LoginAction>()

    private val pendingNavigation = MutableStateFlow<LoginAction?>(null)
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    private val loadingState = MutableStateFlow<InvokeStatus>(InvokeStatus.Idle)

    val state: StateFlow<LoginViewState> = combine(
        pendingNavigation,
        observeAuthState.flow,
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
        observeAuthState(UseCase.Params.Empty)

        viewModelScope.launch {
            observeAuthState.flow.collect {
                if (it) {
                    getFamily(UseCase.Params.Empty).collect { user ->
                        if (FirestoreCollection.Users.Field.family == null) {
                            pendingNavigation.emit(LoginAction.OpenOnboarding)
                        } else {
                            pendingNavigation.emit(LoginAction.OpenActivityLog)
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
                loadingState.emit(InvokeStatus.Started)

                Firebase.auth.signInWithEmailAndPassword(email, password).await()

                loadingState.emit(InvokeStatus.Success)
            } catch (e: Exception) {
                loadingState.emit(InvokeStatus.Error(e))
            }
        }

    private fun signInWithCredentials(intent: Intent) = viewModelScope.launch {
        try {
            loadingState.emit(InvokeStatus.Started)

            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            Firebase.auth.signInWithCredential(credential).await()

            loadingState.emit(InvokeStatus.Success)
        } catch (e: Exception) {
            loadingState.emit(InvokeStatus.Error(e))
        }
    }

    fun submitAction(action: LoginAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
