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

@Composable
fun CircleSpinner(
    size: Int = 160,
    borderWidth: Float = 16f,
    showBackgroundRing: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    val colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary)
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
            androidx.compose.material3.Text(
                text = "Loading...",
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}
