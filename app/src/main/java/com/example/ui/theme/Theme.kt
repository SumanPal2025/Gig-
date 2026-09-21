package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HomezyLightColorScheme =
  lightColorScheme(
    primary = HomezyPrimary,
    onPrimary = Color.White,
    primaryContainer = HomezyPrimaryContainer,
    onPrimaryContainer = HomezyPrimary,
    secondary = HomezySecondary,
    onSecondary = Color.White,
    tertiary = HomezyAccent,
    onTertiary = Color.Black,
    tertiaryContainer = HomezyAccentContainer,
    onTertiaryContainer = Color.Black,
    background = HomezyBackground,
    onBackground = Color.Black,
    surface = HomezyCard,
    onSurface = Color.Black,
    surfaceVariant = HomezySurfaceVariant,
    onSurfaceVariant = HomezyTextSecondary,
    outline = HomezyBorder,
    error = HomezyError,
    onError = Color.White,
  )

private val HomezyDarkColorScheme =
  darkColorScheme(
    primary = HomezySecondary,
    onPrimary = Color.Black,
    primaryContainer = HomezyPrimary,
    onPrimaryContainer = Color.White,
    secondary = HomezySecondary,
    onSecondary = Color.Black,
    tertiary = HomezyAccent,
    onTertiary = Color.Black,
    background = HomezyBackground,
    onBackground = Color.Black,
    surface = HomezyCard,
    onSurface = Color.Black,
    surfaceVariant = HomezySurfaceVariant,
    onSurfaceVariant = HomezyTextSecondary,
    outline = HomezyBorder,
    error = HomezyError,
    onError = Color.White,
  )

@Composable
fun HomezyTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) HomezyDarkColorScheme else HomezyLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit,
) = HomezyTheme(darkTheme = darkTheme, content = content)
