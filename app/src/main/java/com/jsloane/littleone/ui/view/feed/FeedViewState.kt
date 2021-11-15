package com.jsloane.littleone.ui.view.feed

data class FeedViewState(
    val email: String = "",
    val password: String = "",
) {
    companion object {
        val Empty = FeedViewState()
    }
}
