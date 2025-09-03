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

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Font size constants matching React Native theme
object SeamFontSize {
    val small = 12.sp // fontSizeSmall: 12
    val medium = 14.sp // fontSizeMedium: 14
    val large = 18.sp // fontSizeLarge: 18
    val xLarge = 24.sp // fontSizeXLarge: 24
}

// Font weight constants matching React Native theme
object SeamFontWeight {
    val regular = FontWeight.Normal // fontWeightRegular: '400'
    val medium = FontWeight.Medium // fontWeightMedium: '500'
    val bold = FontWeight.SemiBold // fontWeightBold: '600'
}
