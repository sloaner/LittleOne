package com.jsloane.littleone.navigation

import androidx.annotation.StringRes
import com.jsloane.littleone.R

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Login : Screen("login", R.string.screen_login)
    object Feed : Screen("feed", R.string.screen_feed)
}
