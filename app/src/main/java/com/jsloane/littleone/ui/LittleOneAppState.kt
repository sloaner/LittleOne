package com.jsloane.littleone.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class LittleOneAppState(
    val isAuthenticated: Boolean = false,
    val isOnboarded: Boolean = false
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberAppState() = remember() {
    LittleOneAppState()
}
