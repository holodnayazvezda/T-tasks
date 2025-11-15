package com.prod.t_tasks.presentation.components.standart

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R

@Composable
fun TTFloatingActionButton(
    modifier: Modifier,
    onClick: () -> Unit,
    text: String
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    ExtendedFloatingActionButton(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(10.dp),
        containerColor = colorScheme.tertiaryContainer,
        contentColor = colorScheme.onTertiaryContainer,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
    ) {
        Row {
            Text(
                text = text,
                style = typography.titleMedium,
                fontSize = 16.sp
            )
            Spacer(modifier.width(10.dp))
            Icon(
                painter = painterResource(R.drawable.add_square_outline),
                contentDescription = null,
                modifier = modifier.size(22.dp)
            )
        }
    }
}