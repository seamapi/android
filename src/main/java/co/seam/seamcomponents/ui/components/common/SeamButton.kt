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

package co.seam.seamcomponents.ui.components.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

/**
 * A customizable button composable that follows Seam design system standards.
 *
 * This is the base button component that supports various styling options including
 * background colors, borders, text colors, and full-width layouts. It provides
 * consistent styling and behavior across the Seam components.
 *
 * @param text The button text to display
 * @param onClick Callback invoked when the button is tapped
 * @param modifier Optional Modifier for styling and layout customization
 * @param backgroundColor Optional background color, defaults to primary theme color
 * @param borderColor Optional border color, null means no border
 * @param textColor Optional text color, defaults to onPrimary theme color
 * @param enabled Whether the button is enabled for interaction, defaults to true
 * @param isFullWidth Whether the button should take the full available width, defaults to false
 *
 */
@Composable
fun SeamButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    textColor: Color? = null,
    enabled: Boolean = true,
    isFullWidth: Boolean = false,
) {
    val theme = seamTheme
    val buttonBackgroundColor = backgroundColor ?: MaterialTheme.colorScheme.primary
    val buttonTextColor = textColor ?: MaterialTheme.colorScheme.onPrimary
    Button(
        onClick = onClick,
        modifier =
            modifier
                .height(54.dp)
                .then(if (isFullWidth) Modifier.fillMaxWidth() else Modifier)
                .then(if (borderColor != null) Modifier.border(1.dp, borderColor, RoundedCornerShape(10.dp)) else Modifier)
                .padding(horizontal = if (!isFullWidth) 24.dp else 0.dp),
        shape = RoundedCornerShape(10.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = buttonBackgroundColor,
                contentColor = buttonTextColor,
                disabledContainerColor = buttonBackgroundColor.copy(alpha = 0.6f),
                disabledContentColor = buttonTextColor.copy(alpha = 0.6f),
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = theme.typography.headlineLarge,
        )
    }
}

/**
 * A primary button composable styled with the primary theme colors.
 *
 * This is a convenience wrapper around SeamButton that provides consistent
 * primary button styling. It's full-width by default and uses the primary
 * color scheme from the Material theme.
 *
 * @param buttonText The button text to display
 * @param modifier Optional Modifier for styling and layout customization
 * @param onClick Callback invoked when the button is tapped
 *
 */
@Composable
fun SeamPrimaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    SeamButton(
        text = buttonText,
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary,
        onClick = onClick,
        isFullWidth = true,
        modifier = modifier.fillMaxWidth(),
    )
}

/**
 * A secondary button composable styled with the secondary theme colors.
 *
 * This is a convenience wrapper around SeamButton that provides consistent
 * secondary button styling. It's full-width by default and uses the secondary
 * color scheme with an outline border.
 *
 * @param buttonText The button text to display
 * @param modifier Optional Modifier for styling and layout customization
 * @param onClick Callback invoked when the button is tapped
 *
 */
@Composable
fun SeamSecondaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    SeamButton(
        text = buttonText,
        backgroundColor = MaterialTheme.colorScheme.secondary,
        borderColor = MaterialTheme.colorScheme.outline,
        textColor = MaterialTheme.colorScheme.onSecondary,
        onClick = onClick,
        isFullWidth = true,
        modifier = modifier.fillMaxWidth(),
    )
}


@Preview(showBackground = true)
@Composable
fun SeamPrimaryButtonPreview() {
    SeamThemeProvider {
        SeamPrimaryButton(
            buttonText = "Primary Button",
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeamSecondaryButtonPreview() {
    SeamThemeProvider {
        SeamSecondaryButton(
            buttonText = "Secondary Button",
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeamButtonPreview() {
    SeamThemeProvider {
        SeamButton(
            text = "Tap to Unlock",
            onClick = { },
            isFullWidth = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeamButtonDisabledPreview() {
    SeamThemeProvider {
        SeamButton(
            text = "Disabled Button",
            onClick = { },
            enabled = false,
            isFullWidth = true,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SeamButtonWithBorderPreview() {
    SeamThemeProvider {
        SeamButton(
            text = "Border Button",
            onClick = { },
            borderColor = MaterialTheme.colorScheme.onSurface,
            isFullWidth = true,
        )
    }
}
