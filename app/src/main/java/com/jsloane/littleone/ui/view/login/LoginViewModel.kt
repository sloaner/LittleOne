package com.jsloane.littleone.ui.view.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    val loadingState = MutableStateFlow(LoadingState.IDLE)

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        try {
            loadingState.emit(LoadingState.RUNNING)
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            loadingState.emit(LoadingState.SUCCESS)
        } catch (e: Exception) {
            loadingState.emit(LoadingState.FAILED)
        }
    }

    fun signInWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            loadingState.emit(LoadingState.RUNNING)
            Firebase.auth.signInWithCredential(credential).await()
            loadingState.emit(LoadingState.SUCCESS)
        } catch (e: Exception) {
            loadingState.emit(LoadingState.FAILED)
        }
    }

    enum class LoadingState {
        RUNNING,
        SUCCESS,
        FAILED,
        IDLE,
    }
}
