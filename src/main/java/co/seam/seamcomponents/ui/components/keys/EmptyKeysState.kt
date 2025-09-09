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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.SeamSecondaryButton
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

/**
 * A composable that displays an empty state when no keys are available.
 *
 * This component shows a centered layout with an illustration, title, subtitle, and refresh button
 * to inform users that no mobile keys are currently available and provide an action to retry.
 *
 * @param onRefresh Callback invoked when the user taps the refresh button to retry loading keys
 * @param modifier Optional Modifier for styling and layout customization
 *
 */
@Composable
fun EmptyKeysState(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // No keys image
        Image(
            painter = painterResource(id = R.drawable.image_nokeys),
            contentDescription = "No mobile keys",
            modifier =
                Modifier
                    .size(105.dp)
                    .padding(bottom = 24.dp),
        )

        // Title - using SeamFontSize.xLarge (24sp) and SeamFontWeight.bold
        Text(
            text = stringResource(R.string.empty_keys_title),
            style = seamTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        // Subtitle - using SeamFontSize.medium (14sp)
        Text(
            text = stringResource(R.string.empty_keys_subtitle),
            style = seamTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 40.dp),
        )

        // Refresh button
        SeamSecondaryButton(
            buttonText = stringResource(R.string.empty_keys_button),
            onClick = onRefresh,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyKeysStatePreview() {
    SeamThemeProvider {
        EmptyKeysState(
            onRefresh = { },
        )
    }
}
