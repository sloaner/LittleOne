package com.jsloane.littleone.ui.view.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        Firebase.auth.signOut()
    }
}
