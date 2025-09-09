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

package co.seam.seamcomponents.ui.components.unlock

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

/**
 * A customizable animated circular spinner composable with gradient effects.
 * 
 * This component displays a rotating circular spinner with a gradient sweep effect.
 * It supports theming through SeamUnlockCardStyle and can optionally display a 
 * background ring and custom content in the center.
 * 
 * @param size The diameter of the spinner in dp, defaults to 160
 * @param borderWidth The width of the spinner stroke in pixels, defaults to 16f
 * @param showBackgroundRing Whether to display a subtle background ring, defaults to false
 * @param modifier Optional Modifier for styling and layout customization
 * @param content Optional composable content to display in the center of the spinner
 */
@Composable
fun CircleSpinner(
    size: Int = 160,
    borderWidth: Float = 16f,
    showBackgroundRing: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    val unlockCardStyle = seamTheme.unlockCard
    val spinnerColorPrimary = unlockCardStyle.keyButtonGradient?.getOrNull(0)
        ?: MaterialTheme.colorScheme.primary
    val spinnerColorSecondary = unlockCardStyle.cardBackground
        ?: MaterialTheme.colorScheme.background
    // colors are inverted to make the animation look better
    val colors = listOf(spinnerColorSecondary, spinnerColorPrimary)
    val infiniteTransition = rememberInfiniteTransition(label = "spinner_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "rotation",
    )

    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Background ring (optional)
        if (showBackgroundRing) {
            Canvas(modifier = Modifier.size(size.dp)) {
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.2f),
                    style = Stroke(width = borderWidth),
                )
            }
        }

        // Animated spinner arc
        Canvas(modifier = Modifier.size(size.dp)) {
            rotate(rotation) {
                val gradient =
                    Brush.sweepGradient(
                        colors = colors + colors.map { it.copy(alpha = 0f) },
                    )
                drawArc(
                    brush = gradient,
                    startAngle = 0f,
                    sweepAngle = 120f, // Show about 1/3 of the circle
                    useCenter = false,
                    style = Stroke(width = borderWidth, cap = StrokeCap.Round),
                )
            }
        }

        // Content in the center
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun CircleSpinnerPreview() {
    SeamThemeProvider {
        CircleSpinner(
            size = 160,
            showBackgroundRing = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircleSpinnerWithContentPreview() {
    SeamThemeProvider {
        CircleSpinner(
            size = 160,
            showBackgroundRing = true,
        ) {
            Text(
                text = "Loading...",
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}
