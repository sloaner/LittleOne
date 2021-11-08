package com.jsloane.littleone.ui.view.onboard

import com.jsloane.littleone.navigation.Screen

data class OnboardViewState(
    val baby_name: String = "",
    val baby_birthday: String = "",
    val invite_code: String = "",
    val navigateTo: Screen? = null
) {
    companion object {
        val Empty = OnboardViewState()
    }
}
