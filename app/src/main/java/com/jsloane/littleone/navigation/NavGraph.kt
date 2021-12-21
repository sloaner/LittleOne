package com.jsloane.littleone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.jsloane.littleone.ui.view.feed.FeedScreen
import com.jsloane.littleone.ui.view.login.LoginScreen
import com.jsloane.littleone.ui.view.onboard.OnboardingScreen
import com.jsloane.littleone.ui.view.settings.SettingsScreen

@Composable
fun LittleOneNavGraph(
    isUserAuthenticated: Boolean = false
) {
    val navController = rememberNavController()

    val DEEPLINK_BASE = "littleone://jsloane.com"

    DisposableEffect(key1 = navController) {
        val listener = NavController.OnDestinationChangedListener { _, _, _ ->
            NavigationManager.clear()
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Feed.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen()
        }

        composable(
            route = Screen.Onboard.route,
            deepLinks = listOf(navDeepLink { uriPattern = "$DEEPLINK_BASE/join/{id}" })
        ) {
            OnboardingScreen(
                inviteCode = it.arguments?.getString("id")
            )
        }

        composable(Screen.Feed.route) {
            FeedScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navigateUp = { navController.navigateUp() })
        }
    }

    NavigationManager.flow.collectAsState().value?.let { destination ->
        val navOptions = NavOptions.Builder().apply {
            if (destination.popBackstackTo != null) {
                this.setPopUpTo(destination.popBackstackTo.route, true)
            }
        }.build()

        navController.navigate(destination.screen.route, navOptions)
    }
}
