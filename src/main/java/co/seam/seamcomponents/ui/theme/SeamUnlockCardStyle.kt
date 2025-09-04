package co.seam.seamcomponents.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class SeamUnlockCardStyle(
    /**
     * Background color for the unlock card container.
     */
    val cardBackground: Color? = null,
    /**
     * Background color for the header section of the unlock card.
     */
    val headerBackground: Color? = null,
    /**
     * Text color for the main header title.
     */
    val headerTitleColor: Color? = null,
    /**
     * Text color for the header subtitle.
     */
    val headerSubtitleColor: Color? = null,

    /**
     * Key icon color when idle (before user interaction).
     */
    val keyIconColorIdle: Color? = null,
    /**
     * Key icon color while actively unlocking.
     */
    val keyIconColorActive: Color? = null,
    /**
     * Gradient colors for the large key button background (top-to-bottom order).
     */
    val keyButtonGradient: List<Color>? = null,
    /**
     * The shadow color that the Key Button casts.
     */
    val keyButtonShadowColor: Color? = null,
    /**
     * The shadow blur radius that the Key Button casts.
     */
    val keyButtonShadowRadius: Dp? = null,
    /**
     * Color used for instructional text beneath the header.
     */
    val instructionTextColor: Color? = null,
    /**
     * Background color for bullet markers in instructional lists.
     */
    val bulletBackground: Color? = null,
    /**
     * Text color for bullet markers in instructional lists.
     */
    val bulletTextColor: Color? = null,
    /**
     * Color used to indicate a successful unlock state.
     */
    val successColor: Color? = null,
    /**
     * Color used for the success icon.
     */
    val successIconColor: Color? = null,
    /**
     * Color used to indicate an error state.
     */
    val errorColor: Color? = null,
) {
    companion object {
        /**
         * Default key card style using theme colors.
         */
        val default =
            SeamUnlockCardStyle()
    }
}
