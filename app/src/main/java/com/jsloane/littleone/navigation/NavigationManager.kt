package com.jsloane.littleone.navigation

import kotlinx.coroutines.flow.MutableStateFlow

object NavigationManager {
    val flow = MutableStateFlow<Destination?>(null)

    fun navigate(destination: Destination) {
        flow.value = destination
    }

    fun clear() {
        flow.value = null
    }
}
