package com.prod.t_tasks.presentation.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DashboardScreen(
	onStubAction: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = "Главный экран готов к наполнению")
		Button(onClick = onStubAction) {
			Text(text = "Действие")
		}
	}
}
