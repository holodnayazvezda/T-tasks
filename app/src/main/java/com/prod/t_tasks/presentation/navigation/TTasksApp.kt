package com.prod.t_tasks.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.presentation.theme.TemplateProdChebTheme

@Composable
fun TTasksApp(
	appState: TTasksAppState = rememberTTasksAppState(),
	sessionDestination: AppDestination? = null
) {
	TemplateProdChebTheme {
        val snackbarHostState = remember { SnackbarHostState() }
		Scaffold(
			modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                )
            }
		) { padding ->
			TTasksNavHost(
				navController = appState.navController,
				modifier = Modifier.padding(padding),
				sessionDestination = sessionDestination,
                snackbarHostState = snackbarHostState
			)
		}
	}
}
