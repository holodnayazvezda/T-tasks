package com.prod.t_tasks.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.prod.t_tasks.R


val TinkoffSansFamily = FontFamily(
    Font(
        R.font.tinkoff_sans_bold,
        FontWeight.Bold
    ),
    Font(
        R.font.tinkoff_sans_regular,
        FontWeight.Normal
    ),
    Font(
        R.font.tinkoff_sans_medium,
        FontWeight.Medium
    )
)

val Typography = Typography(
	titleLarge = TextStyle(
		fontFamily = TinkoffSansFamily,
		fontWeight = FontWeight.Bold
	),
    titleMedium = TextStyle(
        fontFamily = TinkoffSansFamily,
        fontWeight = FontWeight.Medium
    ),
    labelLarge = TextStyle(
        fontFamily = TinkoffSansFamily,
        fontWeight = FontWeight.Normal
    )

)