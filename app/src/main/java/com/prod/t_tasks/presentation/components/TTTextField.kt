package com.prod.t_tasks.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TTTextField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	modifier: Modifier = Modifier,
	error: String? = null,
	keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
	visualTransformation: VisualTransformation = VisualTransformation.None,
	singleLine: Boolean = true
) {
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		label = { Text(label) },
		modifier = modifier,
		isError = error != null,
		supportingText = {
			if (error != null) {
				Text(text = error)
			}
		},
		keyboardOptions = keyboardOptions,
		visualTransformation = visualTransformation,
		singleLine = singleLine
	)
}
