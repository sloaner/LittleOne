package com.jsloane.littleone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.jsloane.littleone.ui.LittleOneApp
import com.jsloane.littleone.ui.theme.LittleOneTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LittleOneTheme {
                LittleOneApp()
            }
        }
    }
}