package com.prod.t_tasks.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.presentation.screens.DashboardScreen
import com.prod.t_tasks.presentation.screens.SplashScreen

@Composable
fun TTasksNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	startDestination: AppDestination = AppDestination.Splash
) {
	NavHost(
		navController = navController,
		startDestination = startDestination.route,
		modifier = modifier
	) {
		composable(AppDestination.Splash.route) {
			SplashScreen(
				onSplashFinished = {
					navController.navigate(AppDestination.Dashboard.route) {
						popUpTo(AppDestination.Splash.route) {
							inclusive = true
						}
					}
				}
			)
		}

		composable(AppDestination.Dashboard.route) {
			DashboardScreen()
		}
	}
}
