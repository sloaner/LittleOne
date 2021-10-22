package com.jsloane.littleone.ui.view.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")

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

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
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
