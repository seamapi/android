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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Semantic color extensions for Seam components.
 *
 * These provide domain-specific colors (success, danger, warning, info)
 * that complement Material Design 3's base color system.
 *
 * Use these alongside MaterialTheme.colorScheme for complete theming:
 * ```kotlin
 * // Material colors (automatic light/dark mode)
 * color = MaterialTheme.colorScheme.primary
 *
 * // Semantic colors (Seam-specific)
 * color = MaterialTheme.colorScheme.success
 * ```
 */

/**
 * Success color - typically used for positive states, confirmations, and completed actions.
 * Uses Figma design system: emerald/500 (light) and emerald/400 (dark)
 */
val ColorScheme.success: Color
    @Composable get() = if (isSystemInDarkTheme()) {
        Emerald400 // Dark mode success - emerald/400
    } else {
        Emerald500 // Light mode success - emerald/500
    }

/**
 * Danger color - typically used for destructive actions, errors, and critical warnings.
 * Uses Figma design system: red/600 (light) and red/400 (dark)
 */
val ColorScheme.danger: Color
    @Composable get() = if (isSystemInDarkTheme()) {
        Red400 // Dark mode danger - red/400
    } else {
        Red600 // Light mode danger - red/600
    }

/**
 * Warning color - typically used for caution, important notices, and non-critical warnings.
 * Uses Figma design system: amber/500 (light) and amber/300 (dark)
 */
val ColorScheme.warning: Color
    @Composable get() = if (isSystemInDarkTheme()) {
        Amber300 // Dark mode warning - amber/300
    } else {
        Amber500 // Light mode warning - amber/500
    }

/**
 * Info color - typically used for informational messages, tips, and neutral notifications.
 * Uses Figma design system: sky/500 (light) and sky/300 (dark)
 */
val ColorScheme.info: Color
    @Composable get() = if (isSystemInDarkTheme()) {
        Sky300 // Dark mode info - sky/300
    } else {
        Sky500 // Light mode info - sky/500
    }
