package com.prod.t_tasks.presentation.screens

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.prod.t_tasks.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SplashScreen(
	modifier: Modifier = Modifier
) {
	val animatedLogo = AnimatedImageVector.animatedVectorResource(R.drawable.avd_app_logo)
	var atEnd by remember { mutableStateOf(false) }

	LaunchedEffect(animatedLogo) {
		atEnd = true
		delay(1500)
		atEnd = false
	}

	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Image(
			painter = rememberAnimatedVectorPainter(animatedLogo, atEnd),
			contentDescription = null,
			modifier = Modifier.size(160.dp)
		)
		Spacer(modifier = Modifier.height(24.dp))
		Text(
			text = stringResource(id = R.string.app_name),
			style = MaterialTheme.typography.headlineMedium
		)
	}
}
