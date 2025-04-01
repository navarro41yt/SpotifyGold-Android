package com.navarro.spotifygold.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.views.CurrentScreen
import com.navarro.spotifygold.views.HomeScreen
import com.navarro.spotifygold.views.LibraryScreen
import com.navarro.spotifygold.views.SearchScreen

private const val TIME_DURATION = 300

@Composable
fun SpotifyNavigation(
    navController: NavHostController,
    queue: MutableList<AudioDRO>,
    current: MutableState<AudioDRO>
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Navigation.HOME.name,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(
                    durationMillis = TIME_DURATION,
                    easing = LinearOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(
                    durationMillis = TIME_DURATION,
                    easing = LinearOutSlowInEasing
                )
            )
        }
    ) {
        composable(Navigation.HOME.name) {
            HomeScreen(
                queue = queue,
                current = current
            )
        }
        composable(Navigation.SEARCH.name) {
            SearchScreen()
        }
        composable(Navigation.LIBRARY.name) {
            LibraryScreen(
                queue = queue,
                current = current
            )
        }
        composable(Navigation.CURRENT.name) {
            CurrentScreen(
                navController = navController,
                queue = queue,
                current = current
            )
        }
    }
}