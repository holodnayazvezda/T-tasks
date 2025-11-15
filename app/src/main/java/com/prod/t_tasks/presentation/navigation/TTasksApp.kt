package com.prod.t_tasks.presentation.navigation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.presentation.theme.TemplateProdChebTheme

@Composable
fun TTasksApp(
	appState: TTasksAppState = rememberTTasksAppState(),
	context: Context,
	sessionDestination: AppDestination? = null
) {
	TemplateProdChebTheme {
		Scaffold(
			modifier = Modifier.fillMaxSize()
		) { padding ->
			TTasksNavHost(
				navController = appState.navController,
				modifier = Modifier.padding(padding),
				sessionDestination = sessionDestination,
				context = context
			)
		}
	}
}
