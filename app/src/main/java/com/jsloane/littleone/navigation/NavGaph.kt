package com.jsloane.littleone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jsloane.littleone.ui.view.feed.FeedScreen
import com.jsloane.littleone.ui.view.login.LoginScreen

@Composable
fun LittleOneNavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(actions)
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

    val gotoFeed: () -> Unit = {
        navController.navigate(Screen.Feed.route, navOptions = NavOptions.Builder().setPopUpTo(Screen.Login.route, inclusive = true).build())
    }

}