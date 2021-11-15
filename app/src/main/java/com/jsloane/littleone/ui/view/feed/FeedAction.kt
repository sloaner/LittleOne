package com.jsloane.littleone.ui.view.feed

import android.content.Intent

sealed class FeedAction {
    object OpenSettings : FeedAction()
    class UpdateEmail(val email: String) : FeedAction()
    class UpdatePassword(val password: String) : FeedAction()
    class SignInEmail() : FeedAction()
    class SignInToken(val intent: Intent) : FeedAction()
}
