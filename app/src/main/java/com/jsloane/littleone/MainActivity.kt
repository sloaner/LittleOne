package com.jsloane.littleone

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.insets.ProvideWindowInsets
import com.jsloane.littleone.navigation.LittleOneNavGraph
import com.jsloane.littleone.ui.AppViewModel
import com.jsloane.littleone.ui.LittleOneAppState
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.rememberFlowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {
    val viewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProvideWindowInsets {
                LittleOneTheme {
                    val appState by rememberFlowWithLifecycle(viewModel.appState)
                        .collectAsState(initial = LittleOneAppState())

                    LittleOneNavGraph(isUserAuthenticated = appState.isAuthenticated)
                }
            }
        }
    }
}
