package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CinemaRed,
    secondary = CinemaOrangeAdmin,
    tertiary = CinemaGoldStar,
    background = CinemaDarkBackground,
    surface = CinemaCardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = CinemaTextPrimary,
    onSurface = CinemaTextPrimary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
