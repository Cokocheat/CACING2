package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = WormGreen,
    onPrimary = Color.Black,
    primaryContainer = WormGreenDark,
    onPrimaryContainer = Color.White,
    secondary = BoostOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A2800),
    onSecondaryContainer = BoostYellow,
    tertiary = PotionBlue,
    onTertiary = Color.Black,
    background = GameDarkBg,
    onBackground = TextWhite,
    surface = GameSurface,
    onSurface = TextWhite,
    surfaceVariant = GameSurfaceElevated,
    onSurfaceVariant = TextMuted,
    outline = GameCardBorder,
    error = FoodRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
