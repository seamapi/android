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
