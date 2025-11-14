package com.prod.t_tasks.presentation.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
	onSplashFinished: () -> Unit,
	modifier: Modifier = Modifier
) {
	LaunchedEffect(Unit) {
		delay(600)
		onSplashFinished()
	}

	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = "T-tasks",
			style = MaterialTheme.typography.headlineMedium
		)
		CircularProgressIndicator()
	}
}
