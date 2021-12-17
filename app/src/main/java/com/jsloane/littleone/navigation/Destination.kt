package com.jsloane.littleone.navigation

data class Destination(
    val screen: Screen,
    val popBackstackTo: Screen? = null
) {
    companion object {
        val Login = Destination(Screen.Login, popBackstackTo = Screen.Feed)
        val Onboarding = Destination(Screen.Onboard, popBackstackTo = Screen.Login)
        val Feed = Destination(Screen.Feed, popBackstackTo = Screen.Feed)
        val Settings = Destination(Screen.Settings)
    }
}
