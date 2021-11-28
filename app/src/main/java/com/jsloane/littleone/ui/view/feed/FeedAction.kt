package com.jsloane.littleone.ui.view.feed

import android.content.Intent
import com.jsloane.littleone.domain.model.ActivityType

sealed class FeedAction {
    object OpenSettings : FeedAction()
    class UpdateSelectedFilters(val filters: List<ActivityType>) : FeedAction()
    class UpdatePassword(val password: String) : FeedAction()
    class SignInEmail() : FeedAction()
    class SignInToken(val intent: Intent) : FeedAction()
}
