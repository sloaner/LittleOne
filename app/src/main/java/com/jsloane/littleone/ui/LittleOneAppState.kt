package com.jsloane.littleone.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterialApi::class)
class LittleOneAppState constructor(
    val navController: NavController,
    val scaffoldState: ScaffoldState,
    val backdropState: BackdropScaffoldState
) {
    val items = listOf("Songs", "Artists", "Playlists")
    var selectedNavItem = 0
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberAppState(
    navController: NavController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    backdropState: BackdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
) = remember(navController, scaffoldState, backdropState) {
    LittleOneAppState(navController, scaffoldState, backdropState)
}
