package com.jsloane.littleone.ui.view.login

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val snackbar: SharedFlow<String> = MutableSharedFlow()
) {
    companion object {
        val Empty = LoginViewState()
    }
}
