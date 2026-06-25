package com.example.shuksha.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark Orange Primary Color
private val DarkOrange = Color(0xFFE8741B)
private val DarkOrangeDim = Color(0xFFD45E0A)
private val DarkOrangeLight = Color(0xFFFFA726)

// Neutral colors for dark theme
private val DarkBackground = Color(0xFF121212)
private val DarkSurface = Color(0xFF1E1E1E)
private val DarkSurfaceVariant = Color(0xFF2C2C2C)
private val LightText = Color(0xFFFEFEFE)
private val DimText = Color(0xFFB3B3B3)

private val darkColorScheme = darkColorScheme(
    primary = DarkOrange,
    onPrimary = Color.White,
    primaryContainer = DarkOrangeDim,
    onPrimaryContainer = DarkOrangeLight,
    secondary = DarkOrangeLight,
    onSecondary = Color.Black,
    tertiary = DarkOrange,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DimText,
    error = Color(0xFFCF6679),
    onError = Color.Black,
    outline = Color(0xFF8C8C8C)
)

@Composable
fun ShukshaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = darkColorScheme,
        content = content
    )
}
