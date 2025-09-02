package co.seam.seamcomponents.ui.components.common

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun ShadowCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp,
    shadowColor: Color,
    shadowBlur: Dp,
    shadowOffsetY: Dp,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    Box(modifier = modifier) {
        // Shadow layer with proper blur effect using BlurMaskFilter
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = android.graphics.Paint().apply {
                            color = shadowColor.toArgb()
                            maskFilter = BlurMaskFilter(
                                with(density) { shadowBlur.toPx() },
                                BlurMaskFilter.Blur.NORMAL
                            )
                        }

                        // Draw the shadow with offset and side bleeding
                        val shadowPadding = with(density) { shadowBlur.toPx() }
                        canvas.nativeCanvas.drawRoundRect(
                            with(density) {
                                android.graphics.RectF(
                                    -shadowPadding,
                                    shadowOffsetY.toPx(),
                                    size.width + shadowPadding,
                                    size.height + shadowOffsetY.toPx()
                                )
                            },
                            with(density) { cornerRadius.toPx() },
                            with(density) { cornerRadius.toPx() },
                            paint
                        )
                    }
                }
        )

        // Content layer on top
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(cornerRadius))
        ) {
            content()
        }
    }
}
