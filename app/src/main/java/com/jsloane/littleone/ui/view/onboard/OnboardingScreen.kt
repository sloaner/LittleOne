package com.jsloane.littleone.ui.view.onboard

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jsloane.littleone.ui.theme.LittleOneTheme

@Composable
fun OnboardingScreen() {
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun Preview() {
    LittleOneTheme {
        OnboardingScreen()
    }
}
