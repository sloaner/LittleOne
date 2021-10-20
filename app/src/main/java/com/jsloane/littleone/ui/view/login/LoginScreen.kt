package com.jsloane.littleone.ui.view.login

import android.content.res.Configuration
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jsloane.littleone.navigation.MainActions
import com.jsloane.littleone.ui.theme.LittleOneTheme

@Composable
fun LoginScreen(actions: MainActions? = null) {
    Button(onClick = { actions?.gotoFeed?.invoke() }) {
        Text(text = "Click me")
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    LittleOneTheme {
        LoginScreen()
    }
}
