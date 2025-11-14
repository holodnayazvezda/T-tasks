package com.prod.t_tasks.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import kotlinx.coroutines.CoroutineScope

@Stable
class TTasksAppState(
	val navController: NavHostController,
	val coroutineScope: CoroutineScope
) {
	val currentDestination: NavDestination?
		get() = navController.currentDestination

	fun navigateTo(
        destination: AppDestination,
        builder: NavOptionsBuilder.() -> Unit = {}
	) {
		navController.navigate(destination.route, builder)
	}

	fun navigateBack(): Boolean = navController.popBackStack()
}

@Composable
fun rememberTTasksAppState(
	navController: NavHostController = rememberNavController(),
	coroutineScope: CoroutineScope = rememberCoroutineScope()
): TTasksAppState = remember(navController, coroutineScope) {
	TTasksAppState(
		navController = navController,
		coroutineScope = coroutineScope
	)
}
