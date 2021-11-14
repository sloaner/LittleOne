package com.jsloane.littleone.ui.view.feed

import com.jsloane.littleone.ui.view.login.LoginAction

data class FeedViewState(
    val pendingNavigation: FeedAction? = null,
    val email: String = "",
    val password: String = "",
) {
    companion object {
        val Empty = FeedViewState()
    }
}
