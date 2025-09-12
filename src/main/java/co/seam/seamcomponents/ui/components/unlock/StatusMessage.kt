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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

/**
 * A composable that displays status information during the unlock process.
 *
 * This component shows a primary status message and optional instructional text
 * below it. It's used to provide feedback to users about the current state of
 * the unlock operation with appropriate styling from the theme.
 *
 * @param statusText The primary status message to display
 * @param instructionText Optional secondary instruction text, null hides this text
 * @param modifier Optional Modifier for styling and layout customization
 */
@Composable
fun StatusMessage(
    statusText: String,
    modifier: Modifier = Modifier,
    instructionText: String? = null,
) {
    val unlockCardStyle = seamTheme.unlockCard
    val backgroundColor = unlockCardStyle.cardBackground ?: MaterialTheme.colorScheme.background
    val titleColor = unlockCardStyle.headerTitleColor ?: MaterialTheme.colorScheme.onBackground
    val subtitleColor = unlockCardStyle.headerSubtitleColor ?: MaterialTheme.colorScheme.onBackground
    Column(
        modifier = modifier.background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Status message
        Text(
            text = statusText,
            style = seamTheme.typography.titleLarge,
            color = titleColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        // Instruction text
        Text(
            text = instructionText ?: "",
            style = seamTheme.typography.bodyLarge,
            color = subtitleColor,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusMessageConnectingPreview() {
    SeamThemeProvider {
        StatusMessage(
            statusText = "Connecting...",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusMessageUnlockingPreview() {
    SeamThemeProvider {
        StatusMessage(
            statusText = "Unlocking...",
            instructionText = "Please place your key card on the reader.",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusMessageSuccessPreview() {
    SeamThemeProvider {
        StatusMessage(
            statusText = "Success!",
        )
    }
}
