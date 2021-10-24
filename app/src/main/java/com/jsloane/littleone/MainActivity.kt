package com.jsloane.littleone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.navigation.LittleOneNavGraph
import com.jsloane.littleone.ui.AppViewModel
import com.jsloane.littleone.ui.rememberAppState
import com.jsloane.littleone.ui.theme.LittleOneTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {
    val viewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProvideWindowInsets {
                LittleOneTheme {
                    val appState = rememberAppState()

                    LittleOneNavGraph(isUserAuthenticated = Firebase.auth.currentUser != null)
                }
            }
        }
    }
}
