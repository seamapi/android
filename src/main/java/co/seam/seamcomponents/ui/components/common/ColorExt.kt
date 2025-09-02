package co.seam.seamcomponents.ui.components.common

import androidx.compose.ui.graphics.Color

fun Color.darken(factor: Float = 0.8f): Color {
    return Color(
        red = (red * factor).coerceIn(0f, 1f),
        green = (green * factor).coerceIn(0f, 1f),
        blue = (blue * factor).coerceIn(0f, 1f),
        alpha = alpha,
    )
}

fun Color.lighten(factor: Float = 0.8f): Color {
    return Color(
        red = (red / factor).coerceIn(0f, 1f),
        green = (green / factor).coerceIn(0f, 1f),
        blue = (blue / factor).coerceIn(0f, 1f),
        alpha = alpha,
    )
}
