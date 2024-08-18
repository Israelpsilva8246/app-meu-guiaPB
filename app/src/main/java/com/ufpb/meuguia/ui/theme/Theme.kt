package com.ufpb.meuguia.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CustomGreen,
    secondary = CustomGreenDark,
    onPrimary = CustomOnPrimary,
    background = CustomBackground,
    surface = CustomSurface,
    onBackground = CustomOnBackground,
    onSurface = CustomOnSurface,
    error = CustomError,
    onError = CustomOnError
)

private val LightColorScheme = lightColorScheme(
    primary = CustomGreen,
    secondary = CustomGreenLight,
    onPrimary = CustomOnPrimary,
    background = Color.White,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = CustomError,
    onError = CustomOnError
)

@Composable
fun MeuGuiaTheme(
    darkTheme: Boolean = false, // Define o tema claro como padrão
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}