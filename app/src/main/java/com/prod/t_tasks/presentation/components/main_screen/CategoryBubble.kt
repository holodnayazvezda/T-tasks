package com.prod.t_tasks.presentation.components.main_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ui.clickable

@Composable
fun CategoryBubble(
    modifier: Modifier = Modifier,
    name: String,
    isSelected: Boolean,
    error: String? = null,
    onClick: () -> Unit
) {
    val bubbleColor =
        animateColorAsState(if (error == null) { if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary } else { MaterialTheme.colorScheme.onErrorContainer })
    val bubbleBorderColor =
        animateColorAsState(if (error == null) { if (isSelected) MaterialTheme.colorScheme.primaryFixed else MaterialTheme.colorScheme.secondaryFixed } else { MaterialTheme.colorScheme.errorContainer } )
    val textColor =
        animateColorAsState(if (error == null) { if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary } else { MaterialTheme.colorScheme.onError })
    Text(
        modifier = modifier
            .background(
                color = bubbleColor.value,
                shape = Shapes.smallRoundedBox
            )
            .border(
                width = 1.dp,
                color = bubbleBorderColor.value,
                shape = Shapes.smallRoundedBox
            )
            .clip(Shapes.smallRoundedBox)
            .clickable(MaterialTheme.colorScheme.primary, onClick = onClick)
            .padding(horizontal = Paddings.small, vertical = Paddings.verySmall),
        text = name,
        style = Typography.labelLarge,
        fontSize = 16.sp,
        color = textColor.value
    )
}