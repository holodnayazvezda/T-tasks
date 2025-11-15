package com.prod.t_tasks.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.presentation.screens.login.LoginScreen
import com.prod.t_tasks.presentation.screens.register.RegisterScreen
import com.prod.t_tasks.presentation.screens.DashboardScreen
import com.prod.t_tasks.presentation.screens.main.MainScreen
import com.prod.t_tasks.presentation.utils.ErrorCallbacks
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope

@Composable
fun TTasksNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	context: Context,
	sessionDestination: AppDestination? = null
) {
	ErrorCollectorScope(context, navController, object : ErrorCallbacks {
		override fun processConnectionError(networkError: NetworkError.Connection) {

		}

		override fun processUnexpectedError(networkError: NetworkError.Unexpected) {

		}

	}) {
		sessionDestination?.let { startDestination ->
			NavHost(
				navController = navController,
				startDestination = startDestination.route,
				modifier = modifier
			) {
				composable(AppDestination.Login.route) {
					LoginScreen(
						onRegisterClick = {
							navController.navigate(AppDestination.Register.route)
						},
						onSuccess = {
							navController.navigate(AppDestination.Main.route) {
								popUpTo(AppDestination.Login.route) { inclusive = true }
							}
						}
					)
				}
				composable(AppDestination.Register.route) {
					RegisterScreen(
						onLoginClick = {
							navController.popBackStack()
						},
						onSuccess = {
							navController.navigate(AppDestination.Main.route) {
								popUpTo(AppDestination.Register.route) { inclusive = true }
							}
						}
					)
				}
				composable(AppDestination.Main.route) {
					MainScreen()
				}

				composable(AppDestination.Dashboard.route) {
					DashboardScreen()
				}
			}
		}
	}
}
