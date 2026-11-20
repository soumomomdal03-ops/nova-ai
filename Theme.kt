package com.nova.ai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class NovaExtendedColors(
    val backgroundTop: Color,
    val backgroundBottom: Color,
    val glassSurface: Color,
    val glassHighlight: Color,
    val glassBorder: Color,
    val emeraldGlow: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val isDark: Boolean
)

val LocalNovaColors = staticCompositionLocalOf {
    NovaExtendedColors(
        backgroundTop = NovaDeepForest,
        backgroundBottom = NovaObsidian,
        glassSurface = NovaCardDark,
        glassHighlight = Color(0x1A6EE7B7),
        glassBorder = NovaBorderDark,
        emeraldGlow = NovaEmeraldBright,
        textPrimary = NovaTextPrimaryDark,
        textSecondary = NovaTextSecondaryDark,
        isDark = true
    )
}

private val DarkMaterialScheme = darkColorScheme(
    primary = NovaEmerald,
    onPrimary = NovaObsidian,
    secondary = NovaMintGlow,
    onSecondary = NovaObsidian,
    tertiary = NovaPlasmaCyan,
    background = NovaObsidian,
    onBackground = NovaTextPrimaryDark,
    surface = NovaDeepForest,
    onSurface = NovaTextPrimaryDark,
    surfaceVariant = NovaCardElevatedDark,
    onSurfaceVariant = NovaTextSecondaryDark,
    outline = NovaBorderDark,
    error = NovaDanger
)

private val LightMaterialScheme = lightColorScheme(
    primary = NovaEmeraldDark,
    onPrimary = Color.White,
    secondary = NovaEmerald,
    onSecondary = Color.White,
    tertiary = NovaPlasmaCyan,
    background = NovaLightBg,
    onBackground = NovaTextPrimaryLight,
    surface = NovaLightSurface,
    onSurface = NovaTextPrimaryLight,
    surfaceVariant = Color(0xFFE3F5ED),
    onSurfaceVariant = NovaTextSecondaryLight,
    outline = NovaBorderLight,
    error = NovaDanger
)

@Composable
fun NovaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) {
        NovaExtendedColors(
            backgroundTop = Color(0xFF0A261D),
            backgroundBottom = NovaObsidian,
            glassSurface = NovaCardDark,
            glassHighlight = Color(0x1F34D399),
            glassBorder = NovaBorderDark,
            emeraldGlow = NovaEmeraldBright,
            textPrimary = NovaTextPrimaryDark,
            textSecondary = NovaTextSecondaryDark,
            isDark = true
        )
    } else {
        NovaExtendedColors(
            backgroundTop = Color(0xFFDEF7EC),
            backgroundBottom = NovaLightBg,
            glassSurface = NovaCardLight,
            glassHighlight = Color(0x1A047857),
            glassBorder = NovaBorderLight,
            emeraldGlow = NovaEmeraldDark,
            textPrimary = NovaTextPrimaryLight,
            textSecondary = NovaTextSecondaryLight,
            isDark = false
        )
    }

    CompositionLocalProvider(LocalNovaColors provides extendedColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkMaterialScheme else LightMaterialScheme,
            typography = NovaTypography,
            content = content
        )
    }
}
