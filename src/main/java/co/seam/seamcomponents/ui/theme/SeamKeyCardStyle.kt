package co.seam.seamcomponents.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import co.seam.seamcomponents.R

/**
 * Encapsulates all visual style parameters for a hotel key card brand.
 *
 * ## Visual Description
 * By default, backgrounds use a custom "Dune" gradient inspired by sand dunes from above,
 * creating a premium, tactile look suitable for hotel brands.
 *
 * ## Predefined Styles
 * - `.default`: Theme-driven, universal.
 * - `.grey`: Neutral, universal.
 * - `.purple`: Modern, branded accent.
 * - `.yellow`: Bright, energetic accent.
 *
 * ## Extending
 * To define your own style:
 * ```kotlin
 * val myBrandStyle = SeamKeyCardStyle.default.copy(
 *     backgroundGradient = listOf(Color.Cyan, Color.Blue),
 *     accentColor = Color.Green,
 *     logoAssetName = "my_brand_logo"
 * )
 * ```
 */
@Immutable
data class SeamKeyCardStyle(
    val brandLogoUrl: String? = null,
    @DrawableRes val brandLogoRes: Int? = null,
    @DrawableRes val backgroundTextureRes: Int? = null,
) {
    companion object {
        /**
         * Default key card style using theme colors.
         */
        val default =
            SeamKeyCardStyle(
                brandLogoUrl = null,
                brandLogoRes = null,
                backgroundTextureRes = R.drawable.card_gradient,
            )
    }
}
