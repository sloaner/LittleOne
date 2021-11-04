package com.jsloane.littleone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.jsloane.littleone.ui.view.feed.FeedScreen
import com.jsloane.littleone.ui.view.login.LoginScreen
import com.jsloane.littleone.ui.view.onboard.OnboardingScreen

@Composable
fun LittleOneNavGraph(
    isUserAuthenticated: Boolean = false
) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    val DEEPLINK_BASE = "littleone://jsloane.com"

    NavHost(
        navController = navController,
        startDestination = when (isUserAuthenticated) {
            true -> Screen.Feed.route
            false -> Screen.Login.route
        }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                openActivityLog = actions.gotoFeed,
                openOnboarding = actions.openOnboarding
            )
        }

        composable(
            route = Screen.Onboard.route,
            deepLinks = listOf(navDeepLink { uriPattern = "$DEEPLINK_BASE/join/{id}" })
        ) {
            OnboardingScreen(
                openActivityLog = actions.gotoFeed,
                inviteCode = it.arguments?.getString("id")
            )
        }

        composable(Screen.Feed.route) {
            FeedScreen(actions)
        }
    }
}

class MainActions(navController: NavHostController) {

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

    val popBackStack: () -> Unit = {
        navController.popBackStack()
    }

    val gotoLogin: () -> Unit = {
        navController.navigate(
            Screen.Login.route,
            NavOptions.Builder().setPopUpTo(Screen.Login.route, inclusive = true).build()
        )
    }

    val openOnboarding: () -> Unit = {
        navController.navigate(
            Screen.Onboard.route,
            NavOptions.Builder().setPopUpTo(Screen.Login.route, inclusive = true).build()
        )
    }

    val gotoFeed: () -> Unit = {
        navController.navigate(
            Screen.Feed.route,
            NavOptions.Builder().setPopUpTo(Screen.Login.route, inclusive = true).build()
        )
    }
}
