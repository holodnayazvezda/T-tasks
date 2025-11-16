package com.prod.t_tasks.presentation.components.standart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.presentation.theme.Shapes

@Composable
fun BigButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String,
    isLoading: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = Shapes.smallRoundedBox,
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary,
            disabledContainerColor = colorScheme.primary,
            disabledContentColor = colorScheme.onPrimary
        )
    ){
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = buttonText,
                style = typography.labelMedium,
                fontSize = 24.sp,
            )
        }
    }
}

@Composable
fun MediumButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	buttonText: String,
	isLoading: Boolean
) {
	val colorScheme = MaterialTheme.colorScheme
	val typography = MaterialTheme.typography
	Button(
		modifier = modifier
			.fillMaxWidth()
			.height(40.dp),
		shape = Shapes.smallRoundedBox,
		onClick = onClick,
		enabled = !isLoading,
		colors = ButtonColors(
			containerColor = colorScheme.primary,
			contentColor = colorScheme.onPrimary,
			disabledContainerColor = colorScheme.primary,
			disabledContentColor = colorScheme.onPrimary
		)
	){
		if (isLoading) {
			CircularProgressIndicator()
		} else {
			Text(
				text = buttonText,
				style = typography.labelMedium,
				fontSize = 16.sp,
			)
		}
	}
}