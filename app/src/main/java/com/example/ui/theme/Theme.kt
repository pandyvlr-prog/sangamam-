package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DeepGreenPrimary,
    onPrimary = SurfaceContainerLowest,
    primaryContainer = ForestGreenPrimaryContainer,
    onPrimaryContainer = LightMintAccent,
    secondary = EmeraldSecondary,
    onSecondary = SurfaceContainerLowest,
    secondaryContainer = EmeraldLightContainer,
    onSecondaryContainer = DeepGreenPrimary,
    tertiary = DarkGreenTertiary,
    onTertiary = SurfaceContainerLowest,
    background = WarmCreamBackground,
    onBackground = OnSurfaceText,
    surface = WarmCreamBackground,
    onSurface = OnSurfaceText,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = OnSurfaceVariantText,
    outline = OutlineBorder,
    outlineVariant = OutlineVariant,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)

private val DarkColorScheme = LightColorScheme // Community app strictly follows signature branded green & cream baseline

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep signature Sangamam brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = DeepGreenPrimary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
