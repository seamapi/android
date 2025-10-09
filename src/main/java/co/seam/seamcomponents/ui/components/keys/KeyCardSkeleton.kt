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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.components.common.ShadowCard
import co.seam.seamcomponents.ui.components.common.shimmer
import co.seam.seamcomponents.ui.theme.SeamThemeProvider

/**
 * A skeleton loading placeholder that mimics the KeyCardComponent structure.
 *
 * This composable displays a grayscale version of the key card with a shimmer animation
 * to indicate loading state. It matches the dimensions and layout structure of the actual
 * KeyCardComponent to provide a smooth transition when the real data loads.
 *
 * @param modifier Optional Modifier for styling and layout customization
 */
@Composable
fun KeyCardSkeleton(modifier: Modifier = Modifier) {
    val cornerRadius = 16.dp
    val shadowColor = Color(0x1A000000)
    val shadowOffsetY = 5.dp

    // Grayscale colors for skeleton
    val baseGray = Color(0xFFE0E0E0)
    val lightGray = Color(0xFFF0F0F0)
    val mediumGray = Color(0xFFD0D0D0)

    val borderGradient =
        Brush.linearGradient(
            colors =
                listOf(
                    Color(0xFFE0E0E0),
                    Color(0xFFF5F5F5),
                ),
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset(Float.POSITIVE_INFINITY, 0f),
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
                        .background(
                            Brush.linearGradient(
                                colors = listOf(lightGray, baseGray),
                            ),
                        ),
            )

            // Accent bar at top right
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .width(149.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(topEnd = cornerRadius))
                        .shimmer(
                            baseColor = mediumGray,
                            highlightColor = lightGray,
                        ),
            )

            // Card content
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Brand logo placeholder (top right aligned)
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .width(100.dp)
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .shimmer(
                                    baseColor = mediumGray,
                                    highlightColor = lightGray,
                                ),
                    )
                }

                // Hotel info section (bottom left aligned)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Room name placeholder
                    Box(
                        modifier =
                            Modifier
                                .width(80.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmer(
                                    baseColor = mediumGray,
                                    highlightColor = lightGray,
                                ),
                    )

                    // Access code row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .width(90.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmer(
                                        baseColor = mediumGray.copy(alpha = 0.6f),
                                        highlightColor = lightGray.copy(alpha = 0.6f),
                                    ),
                        )
                        Box(
                            modifier =
                                Modifier
                                    .width(50.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmer(
                                        baseColor = mediumGray,
                                        highlightColor = lightGray,
                                    ),
                        )
                    }

                    // Checkout info row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .width(80.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmer(
                                        baseColor = mediumGray.copy(alpha = 0.6f),
                                        highlightColor = lightGray.copy(alpha = 0.6f),
                                    ),
                        )
                        Box(
                            modifier =
                                Modifier
                                    .width(150.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmer(
                                        baseColor = mediumGray,
                                        highlightColor = lightGray,
                                    ),
                        )
                    }
                }
            }
        }
    }
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
