package com.prod.t_tasks.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Light color scheme
private val LightColorScheme = lightColorScheme(
	primary = WhitePrimary,
	onPrimary = WhiteOnPrimary,
	primaryContainer = WhitePrimaryContainer,
	onPrimaryContainer = WhiteOnPrimaryContainer,
	inversePrimary = WhiteInversePrimary,

	secondary = WhiteSecondary,
	onSecondary = WhiteOnSecondary,
	secondaryContainer = WhiteSecondaryContainer,
	onSecondaryContainer = WhiteOnSecondaryContainer,

	tertiary = WhiteTertiary,
	onTertiary = WhiteOnTertiary,
	tertiaryContainer = WhiteTertiaryContainer,
	onTertiaryContainer = WhiteOnTertiaryContainer,

	error = WhiteError,
	onError = WhiteOnError,
	errorContainer = WhiteErrorContainer,
	onErrorContainer = WhiteOnErrorContainer,

	background = WhiteBackground,
	onBackground = WhiteOnBackground,
	surface = WhiteSurface,
	onSurface = WhiteOnSurface,
	surfaceVariant = WhiteSurfaceVariant,
	onSurfaceVariant = WhiteOnSurfaceVariant,
	inverseSurface = WhiteInverseSurface,
	inverseOnSurface = WhiteInverseOnSurface,

	outline = WhiteOutline,
	outlineVariant = WhiteOutlineVariant,

	scrim = WhiteScrim,
	surfaceTint = WhiteSurfaceTint,

	// Fixed colors
	primaryFixed = WhitePrimaryFixed,
	onPrimaryFixed = WhiteOnPrimaryFixed,
	primaryFixedDim = WhitePrimaryFixedDim,
	onPrimaryFixedVariant = WhiteOnPrimaryFixedVariant,

	secondaryFixed = WhiteSecondaryFixed,
	onSecondaryFixed = WhiteOnSecondaryFixed,
	secondaryFixedDim = WhiteSecondaryFixedDim,
	onSecondaryFixedVariant = WhiteOnSecondaryFixedVariant,

	tertiaryFixed = WhiteTertiaryFixed,
	onTertiaryFixed = WhiteOnTertiaryFixed,
	tertiaryFixedDim = WhiteTertiaryFixedDim,
	onTertiaryFixedVariant = WhiteOnTertiaryFixedVariant,

	surfaceDim = WhiteSurfaceDim,
	surfaceBright = WhiteSurfaceBright,
	surfaceContainerLowest = WhiteSurfaceContainerLowest,
	surfaceContainerLow = WhiteSurfaceContainerLow,
	surfaceContainer = WhiteSurfaceContainer,
	surfaceContainerHigh = WhiteSurfaceContainerHigh,
	surfaceContainerHighest = WhiteSurfaceContainerHighest
)

// Dark color scheme
private val DarkColorScheme = darkColorScheme(
	primary = DarkPrimary,
	onPrimary = DarkOnPrimary,
	primaryContainer = DarkPrimaryContainer,
	onPrimaryContainer = DarkOnPrimaryContainer,
	inversePrimary = DarkInversePrimary,

	secondary = DarkSecondary,
	onSecondary = DarkOnSecondary,
	secondaryContainer = DarkSecondaryContainer,
	onSecondaryContainer = DarkOnSecondaryContainer,

	tertiary = DarkTertiary,
	onTertiary = DarkOnTertiary,
	tertiaryContainer = DarkTertiaryContainer,
	onTertiaryContainer = DarkOnTertiaryContainer,

	error = DarkError,
	onError = DarkOnError,
	errorContainer = DarkErrorContainer,
	onErrorContainer = DarkOnErrorContainer,

	background = DarkBackground,
	onBackground = DarkOnBackground,
	surface = DarkSurface,
	onSurface = DarkOnSurface,
	surfaceVariant = DarkSurfaceVariant,
	onSurfaceVariant = DarkOnSurfaceVariant,
	inverseSurface = DarkInverseSurface,
	inverseOnSurface = DarkInverseOnSurface,

	outline = DarkOutline,
	outlineVariant = DarkOutlineVariant,

	scrim = DarkScrim,
	surfaceTint = DarkSurfaceTint,

	// Fixed colors
	primaryFixed = DarkPrimaryFixed,
	onPrimaryFixed = DarkOnPrimaryFixed,
	primaryFixedDim = DarkPrimaryFixedDim,
	onPrimaryFixedVariant = DarkOnPrimaryFixedVariant,

	secondaryFixed = DarkSecondaryFixed,
	onSecondaryFixed = DarkOnSecondaryFixed,
	secondaryFixedDim = DarkSecondaryFixedDim,
	onSecondaryFixedVariant = DarkOnSecondaryFixedVariant,

	tertiaryFixed = DarkTertiaryFixed,
	onTertiaryFixed = DarkOnTertiaryFixed,
	tertiaryFixedDim = DarkTertiaryFixedDim,
	onTertiaryFixedVariant = DarkOnTertiaryFixedVariant,

	surfaceDim = DarkSurfaceDim,
	surfaceBright = DarkSurfaceBright,
	surfaceContainerLowest = DarkSurfaceContainerLowest,
	surfaceContainerLow = DarkSurfaceContainerLow,
	surfaceContainer = DarkSurfaceContainer,
	surfaceContainerHigh = DarkSurfaceContainerHigh,
	surfaceContainerHighest = DarkSurfaceContainerHighest
)

@Composable
fun TemplateProdChebTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
