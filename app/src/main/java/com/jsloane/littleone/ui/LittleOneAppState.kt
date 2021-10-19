package com.jsloane.littleone.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterialApi::class)
class LittleOneAppState {
    val items = listOf("Songs", "Artists", "Playlists")
    var selectedNavItem = 0
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberAppState() = remember() {
    LittleOneAppState()
}
