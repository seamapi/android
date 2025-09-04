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
import co.seam.seamcomponents.ui.theme.SeamComponentsThemeData
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

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
            style = theme.typography.buttonText,
        )
    }
}

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
