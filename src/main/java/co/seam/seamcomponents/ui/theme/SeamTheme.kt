/*
 * MIT License
 *
 * Copyright (c) 2025 Seam Labs, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package co.seam.seamcomponents.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * A semantic theme for all SeamComponents, providing customizable styling for `typography` and `keyCard`.
 *
 * Colors are handled directly through Material Design 3's ColorScheme system, which provides
 * automatic light/dark mode switching and better integration with Material components.
 *
 * Semantic colors (success, danger, warning, info) are available as extension properties
 * on MaterialTheme.colorScheme.
 *
 * ## Usage Example
 * ```kotlin
 * // Custom Material colors
 * val lightColors = lightColorScheme(
 *     primary = Color(0xFF1976D2),
 *     secondary = Color(0xFF03DAC5)
 * )
 * val darkColors = darkColorScheme(
 *     primary = Color(0xFF90CAF9),
 *     secondary = Color(0xFF03DAC5)
 * )
 *
 * SeamThemeProvider(
 *     lightColorScheme = lightColors,
 *     darkColorScheme = darkColors
 * ) {
 *     // Use Material colors
 *     color = MaterialTheme.colorScheme.primary
 *
 *     // Use semantic colors
 *     color = MaterialTheme.colorScheme.success
 * }
 * ```
 */
@Immutable
data class SeamTheme(
    val typography: SeamTypography,
    val keyCard: SeamKeyCardStyle,
) {
    companion object {
        /**
         * The default theme with Material Design styling.
         */
        val default = SeamTheme(
            typography = SeamTypography.default,
            keyCard = SeamKeyCardStyle.default,
        )
    }
}

/**
 * CompositionLocal for providing SeamTheme to the composition tree.
 */
val LocalSeamTheme = staticCompositionLocalOf { SeamTheme.default }

/**
 * Provides the current SeamTheme from the composition.
 */
val seamTheme: SeamTheme
    @Composable
    get() = LocalSeamTheme.current

/**
 * Provides comprehensive theming for Seam components using Material Design 3.
 *
 * This composable sets up both Material theming (with automatic light/dark mode switching)
 * and Seam-specific theming (typography, key card styles).
 *
 * ## Material Colors
 * Use `MaterialTheme.colorScheme.primary`, `MaterialTheme.colorScheme.background`, etc.
 * for automatic light/dark mode switching.
 *
 * ## Semantic Colors
 * Use `MaterialTheme.colorScheme.success`, `MaterialTheme.colorScheme.danger`, etc.
 * for domain-specific colors with automatic light/dark mode switching.
 *
 * @param lightColorScheme Material color scheme for light mode
 * @param darkColorScheme Material color scheme for dark mode
 * @param seamTheme Seam-specific theme (typography, key card styles)
 * @param content The composable content that will have access to the theme
 */
@Composable
fun SeamThemeProvider(
    lightColorScheme: androidx.compose.material3.ColorScheme = defaultLightColorScheme(),
    darkColorScheme: androidx.compose.material3.ColorScheme = defaultDarkColorScheme(),
    seamTheme: SeamTheme = SeamTheme.default,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorScheme
    } else {
        lightColorScheme
    }

    CompositionLocalProvider(
        LocalSeamTheme provides seamTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = seamTheme.typography.toMaterial3Typography(),
            content = content,
        )
    }
}

/**
 * Legacy SeamTheme composable for backward compatibility.
 * Use SeamThemeProvider instead for new code.
 */
@Composable
@Deprecated(
    "Use SeamThemeProvider instead for better Material Design 3 integration",
    ReplaceWith("SeamThemeProvider(seamTheme = theme, content = content)")
)
fun SeamTheme(
    theme: SeamTheme = SeamTheme.default,
    content: @Composable () -> Unit,
) {
    SeamThemeProvider(
        seamTheme = theme,
        content = content
    )
}

/**
 * Default light color scheme matching iOS/Material Design conventions.
 */
@Composable
private fun defaultLightColorScheme() = lightColorScheme(
    primary = Slate800,
    onPrimary = BWWhite,
    secondary = BWWhite,
    onSecondary = Slate700,
    background = BWWhite,
    onBackground = Slate700,
    surface = Slate800,
    onSurface = BWWhite,
    surfaceVariant = Slate400,
    onSurfaceVariant = BWWhite,
    outline = Slate400,
    error = Amber300,
    onError = BWWhite,
)

/**
 * Default dark color scheme matching iOS/Material Design conventions.
 */
@Composable
private fun defaultDarkColorScheme() = darkColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF1C1C1E),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF1C1C1E),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1C1C1E),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFF8E8E93),
    outline = Color(0xFF8E8E93),
    error = Color(0xFFFF453A),
    onError = Color(0xFF000000),
)
