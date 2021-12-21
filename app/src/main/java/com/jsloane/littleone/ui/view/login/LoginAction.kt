package com.jsloane.littleone.ui.view.login

import android.content.Intent

sealed class LoginAction {
    object OpenActivityLog : LoginAction()
    object OpenOnboarding : LoginAction()

    class UpdateEmail(val email: String) : LoginAction()
    class UpdatePassword(val password: String) : LoginAction()

    object SignInEmail : LoginAction()
    class SignInToken(val intent: Intent) : LoginAction()
    class RegisterUser(val user: String, val pass: String) : LoginAction()
    object ForgotPassword : LoginAction()
}
