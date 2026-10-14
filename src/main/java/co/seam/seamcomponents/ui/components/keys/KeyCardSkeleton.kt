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

package co.seam.seamcomponents.ui.components.keys

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.ShadowCard
import co.seam.seamcomponents.ui.components.common.shimmer
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

/**
 * A skeleton loading placeholder that mimics the KeyCardComponent structure.
 *
 * It draws the key card's own shell from the theme - the same gradient, texture, corner
 * radius and shadow as [KeyCardComponent] - with placeholder bars in the card's text color
 * and a highlight sweeping across. So the placeholder matches the branded card that replaces
 * it, in light and dark, rather than flashing a grey card first.
 *
 * @param modifier Optional Modifier for styling and layout customization
 */
@Composable
fun KeyCardSkeleton(modifier: Modifier = Modifier) {
    // Same theme values and fallbacks as KeyCardComponent.
    val keyCardStyle = seamTheme.keyCard
    val gradientColors =
        keyCardStyle.backgroundGradient ?: listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant,
        )
    val cornerRadius = keyCardStyle.cornerRadius ?: 16.dp
    val shadowColor = keyCardStyle.shadowColor ?: Color(0x33000000)
    val shadowOffsetY = keyCardStyle.shadowYOffset ?: 5.dp
    val textColor = keyCardStyle.textColor ?: MaterialTheme.colorScheme.onPrimary

    // Placeholder bars in the card's text color; labels fainter than values, as on the card.
    val bar = textColor.copy(alpha = 0.18f)
    val faintBar = textColor.copy(alpha = 0.10f)

    // A white sweep reads on both light and dark cards when it's softer on dark ones.
    val isDarkCard = gradientColors.first().luminance() < 0.5f
    val highlight = Color.White.copy(alpha = if (isDarkCard) 0.12f else 0.55f)

    val borderGradient =
        Brush.linearGradient(
            colors =
                listOf(
                    Color(0xff939393), // Bottom left color
                    Color(0xffffffff), // Top right color
                ),
            start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom left
            end = Offset(Float.POSITIVE_INFINITY, 0f), // Top right
        )

    ShadowCard(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1.586f), // us credit card standard ratio,
        cornerRadius = cornerRadius,
        shadowColor = shadowColor,
        shadowBlur = 8.dp,
        shadowOffsetY = shadowOffsetY,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .border(0.5.dp, borderGradient, RoundedCornerShape(cornerRadius)),
        ) {
            // Gradient background
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(Brush.linearGradient(colors = gradientColors)),
            )

            // Background texture overlay, as on the card
            Image(
                painter = painterResource(id = keyCardStyle.backgroundTextureRes ?: R.drawable.card_gradient),
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cornerRadius))
                        .alpha(0.4f),
                contentScale = ContentScale.Crop,
            )

            // Card content
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Brand logo placeholder, where the card puts the logo
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    contentAlignment = keyCardStyle.logoAlignment,
                ) {
                    SkeletonBar(color = bar, width = 100.dp, height = 40.dp, cornerRadius = 8.dp)
                }

                // Key details (bottom left aligned)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Room name placeholder
                    SkeletonBar(color = bar, width = 80.dp, height = 24.dp)

                    // Access code row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonBar(color = faintBar, width = 90.dp, height = 14.dp)
                        SkeletonBar(color = bar, width = 50.dp, height = 14.dp)
                    }

                    // Checkout info row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonBar(color = faintBar, width = 80.dp, height = 14.dp)
                        SkeletonBar(color = bar, width = 150.dp, height = 14.dp)
                    }
                }
            }

            // Highlight sweeping across the whole card
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cornerRadius))
                        .shimmer(
                            baseColor = highlight.copy(alpha = 0f),
                            highlightColor = highlight,
                        ),
            )
        }
    }
}

@Composable
private fun SkeletonBar(
    color: Color,
    width: Dp,
    height: Dp,
    cornerRadius: Dp = 4.dp,
) {
    Box(
        modifier =
            Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(cornerRadius))
                .background(color),
    )
}

@Preview(showBackground = true)
@Composable
fun KeyCardSkeletonPreview() {
    SeamThemeProvider {
        KeyCardSkeleton()
    }
}

@Preview(showBackground = true)
@Composable
fun KeyCardSkeletonWithPaddingPreview() {
    SeamThemeProvider {
        KeyCardSkeleton(
            modifier = Modifier.padding(16.dp),
        )
    }
}
