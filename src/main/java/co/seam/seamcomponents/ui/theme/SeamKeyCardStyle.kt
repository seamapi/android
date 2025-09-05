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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Immutable
data class SeamKeyCardStyle(
    val brandLogoUrl: String? = null,
    @DrawableRes val brandLogoRes: Int? = null,
    @DrawableRes val backgroundTextureRes: Int? = null,
    val textColor: Color? = null,
    val backgroundGradient: List<Color>? = null,
    val errorColor: Color? = null,
    val accentColor: Color? = null,
    val cornerRadius: Dp? = null,
    val shadowColor: Color? = null,
    val shadowYOffset: Dp? = null,
) {
    companion object {
        /**
         * Default key card style using theme colors.
         */
        val default =
            SeamKeyCardStyle()
    }
}
