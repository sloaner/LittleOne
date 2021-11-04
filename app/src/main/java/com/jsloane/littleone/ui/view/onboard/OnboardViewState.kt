package com.jsloane.littleone.ui.view.onboard

data class OnboardViewState(
    val family_name: String = "",
    val join_code: String = "",
) {
    companion object {
        val Empty = OnboardViewState()
    }
}
