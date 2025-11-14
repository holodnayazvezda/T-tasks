package com.prod.t_tasks.presentation.utils.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo

fun Modifier.clickable(
	rippleColor: Color? = null,
	onClick: () -> Unit
) = composed(
	inspectorInfo = debugInspectorInfo {
		name = "clickable"
		properties["rippleColor"] = rippleColor
		properties["onClick"] = onClick
	}
) {
	this.clickable(
		onClick = onClick,
		indication = rippleColor?.let {
			ripple(
				color = it
			)
		} ?: LocalIndication.current,
		interactionSource = remember { MutableInteractionSource() }
	)
}