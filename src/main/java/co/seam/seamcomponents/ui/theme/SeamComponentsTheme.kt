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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
internal data class SeamComponentsThemeData(
    val typography: SeamTypography = SeamTypography.default,
    val keyCard: SeamKeyCardStyle = SeamKeyCardStyle.default,
    val unlockCard: SeamUnlockCardStyle = SeamUnlockCardStyle.default,
)

data class SeamTheme(
    val keyCard: SeamKeyCardStyle = SeamKeyCardStyle.default,
    val unlockCard: SeamUnlockCardStyle = SeamUnlockCardStyle.default,
)

/**
 * CompositionLocal for providing SeamTheme to the composition tree.
 */
internal val LocalSeamComponentsTheme = staticCompositionLocalOf { SeamComponentsThemeData() }

/**
 * Provides the current SeamTheme from the composition.
 */
internal val seamTheme: SeamComponentsThemeData
    @Composable
    get() = LocalSeamComponentsTheme.current

@Composable
internal fun SeamThemeProvider(
    lightColorScheme: ColorScheme = SeamComponentsLightColorScheme(),
    darkColorScheme: ColorScheme = SeamComponentsDarkColorScheme(),
    seamComponentsTheme: SeamComponentsThemeData = SeamComponentsThemeData(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        if (isSystemInDarkTheme()) {
            darkColorScheme
        } else {
            lightColorScheme
        }

    CompositionLocalProvider(
        LocalSeamComponentsTheme provides seamComponentsTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = seamComponentsTheme.typography.toMaterial3Typography(),
            content = content,
        )
    }
}

@Composable
fun SeamComponentsTheme(
    seamTheme: SeamTheme = SeamTheme(),
    typography: Typography = SeamTypography.default.toMaterial3Typography(),
    colorScheme: ColorScheme,
    content: @Composable () -> Unit,
) {
    val seamComponentsTheme =
        SeamComponentsThemeData(
            keyCard = seamTheme.keyCard,
            unlockCard = seamTheme.unlockCard,
            typography = typography.fromMaterial3Typography(),
        )
    CompositionLocalProvider(
        LocalSeamComponentsTheme provides seamComponentsTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

/**
 * Default light color scheme matching iOS/Material Design conventions.
 */
@Composable
fun SeamComponentsLightColorScheme() =
    lightColorScheme(
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
fun SeamComponentsDarkColorScheme() =
    darkColorScheme(
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
