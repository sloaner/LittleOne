package com.jsloane.littleone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.navigation.LittleOneNavGraph
import com.jsloane.littleone.ui.rememberAppState
import com.jsloane.littleone.ui.theme.LittleOneTheme

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        setContent {
            ProvideWindowInsets {
                LittleOneTheme {
                    val appState = rememberAppState()

                    LittleOneNavGraph()
                }
            }
        }
    }
}