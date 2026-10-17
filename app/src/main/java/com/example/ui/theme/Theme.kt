package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppTheme = staticCompositionLocalOf { AppTheme.INDUSTRIAL_RED }

fun getThemeDarkColorScheme(theme: AppTheme): ColorScheme {
    return darkColorScheme(
        primary = theme.lightColor,
        onPrimary = CorporateNavy900,
        primaryContainer = theme.accentColor,
        onPrimaryContainer = Color.White,
        secondary = theme.primaryColor,
        onSecondary = Color.White,
        secondaryContainer = theme.containerColor,
        onSecondaryContainer = theme.onContainerColor,
        tertiary = CorporateBluePrimary,
        background = if (theme == AppTheme.OBSIDIAN_DARK) ObsidianBlack else CorporateNavy900,
        onBackground = Slate100,
        surface = if (theme == AppTheme.OBSIDIAN_DARK) ObsidianSurface else CorporateNavy800,
        onSurface = Slate50,
        surfaceVariant = CorporateNavy700,
        onSurfaceVariant = Slate300,
        outline = Slate700,
        outlineVariant = CorporateNavy600
    )
}

fun getThemeLightColorScheme(theme: AppTheme): ColorScheme {
    return lightColorScheme(
        primary = theme.primaryColor,
        onPrimary = Color.White,
        primaryContainer = theme.containerColor,
        onPrimaryContainer = theme.onContainerColor,
        secondary = theme.accentColor,
        onSecondary = Color.White,
        secondaryContainer = theme.containerColor,
        onSecondaryContainer = theme.onContainerColor,
        tertiary = CorporateBluePrimary,
        background = Slate50,
        onBackground = Slate900,
        surface = Color.White,
        onSurface = Slate900,
        surfaceVariant = Slate100,
        onSurfaceVariant = Slate600,
        outline = Slate300,
        outlineVariant = Slate200
    )
}

@Composable
fun WorkforceHealthTheme(
    selectedTheme: AppTheme = AppTheme.INDUSTRIAL_RED,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme || selectedTheme == AppTheme.OBSIDIAN_DARK) {
        getThemeDarkColorScheme(selectedTheme)
    } else {
        getThemeLightColorScheme(selectedTheme)
    }

    CompositionLocalProvider(LocalAppTheme provides selectedTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
