package com.jsloane.littleone.ui.view.onboard

data class OnboardViewState(
    val baby_name: String = "",
    val baby_birthday: String = "",
    val invite_code: String = ""
) {
    companion object {
        val Empty = OnboardViewState()
    }
}
