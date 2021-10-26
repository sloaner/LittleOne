package com.jsloane.littleone.ui.view.login

data class LoginViewState(
    val pendingNavigation: LoginAction? = null,
    val email: String = "",
    val password: String = "",
) {
    companion object {
        val Empty = LoginViewState()
    }
}
