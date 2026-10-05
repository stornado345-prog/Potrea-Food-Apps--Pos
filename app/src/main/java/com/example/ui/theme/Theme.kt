package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val PoetraLightColorScheme = lightColorScheme(
    primary = PoetraRedPrimary,
    onPrimary = Color.White,
    primaryContainer = PoetraRedContainer,
    onPrimaryContainer = PoetraOnRedContainer,
    secondary = PoetraAmber,
    onSecondary = Color.White,
    secondaryContainer = PoetraAmberContainer,
    onSecondaryContainer = PoetraOnAmberContainer,
    tertiary = PoetraAmberDark,
    onTertiary = Color.White,
    background = PoetraBackground,
    onBackground = PoetraTextDark,
    surface = PoetraSurface,
    onSurface = PoetraTextDark,
    surfaceVariant = PoetraSurfaceVariant,
    onSurfaceVariant = PoetraTextDark,
    outline = PoetraOutline,
    outlineVariant = PoetraOutlineVariant
)

private val PoetraDarkColorScheme = darkColorScheme(
    primary = PoetraRedLight,
    onPrimary = Color.Black,
    primaryContainer = PoetraRedDark,
    onPrimaryContainer = Color.White,
    secondary = PoetraAmberLight,
    onSecondary = Color.Black,
    background = Color(0xFF141416),
    onBackground = Color(0xFFECECEC),
    surface = Color(0xFF1E1E22),
    onSurface = Color(0xFFECECEC),
    surfaceVariant = Color(0xFF28282E),
    onSurfaceVariant = Color(0xFFECECEC),
    outline = Color(0xFF3F3F46)
)

@Composable
fun PoetraFoodTheme(
    darkTheme: Boolean = false, // Fast-food tablet kiosk defaults to bright, inviting light theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) PoetraDarkColorScheme else PoetraLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
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
