package com.prod.t_tasks.presentation.components.standart

import com.prod.t_tasks.R
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.prod.t_tasks.presentation.theme.Shapes

@Composable
fun TCheckBox(modifier: Modifier = Modifier, checked: Boolean, color: Color) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .border(width = 1.dp, color = color, shape = Shapes.verySmallRoundedBox)
    ) {
        AnimatedContent(checked) {
            if (it) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp),
                    painter = painterResource(R.drawable.ic_checkmark),
                    tint = color,
                    contentDescription = "checkmark"
                )
            }
        }
    }
}