package com.prod.t_tasks.presentation.components.standart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TTPasswordField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	error: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    onDone: (() -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
	var isVisible by remember { mutableStateOf(false) }

    Box(
        Modifier.height(70.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth().height(56.dp)
                .offset(x = 5.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(15.dp)
                )
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().offset(y = 5.dp),
            value = value,
            onValueChange = onValueChange,
            textStyle = typography.labelLarge,
            placeholder = {
                Text(
                    label,
                    style = typography.labelLarge,
                    fontSize = 14.sp
                )
            },
            isError = error != null,
            supportingText = {
                if (error != null) {
                    Text(
                        text = error,
                        style = typography.labelLarge,
                        fontSize = 12.sp
                    )
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone?.invoke()
                }
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorScheme.primary,
                unfocusedContainerColor = colorScheme.primary,
                errorContainerColor = colorScheme.error,

                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,

                focusedPlaceholderColor = colorScheme.onPrimary,
                unfocusedPlaceholderColor = colorScheme.onPrimary,
                errorPlaceholderColor = colorScheme.onError,

                focusedTextColor = colorScheme.onPrimary,
                unfocusedTextColor = colorScheme.onPrimary,
                errorTextColor = colorScheme.onError,

                cursorColor = colorScheme.onPrimary,
                errorCursorColor = colorScheme.onError
            ),
            shape = RoundedCornerShape(15.dp),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colorScheme.onPrimary
                    )
                }
            }
        )
    }
}
