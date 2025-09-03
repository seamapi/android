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
