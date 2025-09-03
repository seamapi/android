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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

@Composable
fun UnlockInstructions(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().animateContentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                InstructionStep(
                    number = "1",
                    text = stringResource(R.string.unlock_instructions_step1),
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                InstructionStep(
                    number = "2",
                    text = stringResource(R.string.unlock_instructions_step2),
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                InstructionStep(
                    number = "3",
                    text = stringResource(R.string.unlock_instructions_step3),
                )
            }

            // Illustration
            Image(
                painter = painterResource(id = R.drawable.hold_phone),
                contentDescription = stringResource(R.string.unlock_instructions_phone_illustration_description),
                modifier = Modifier.size(width = 120.dp, height = 148.dp),
            )
        }
    }
}

@Composable
private fun InstructionStep(
    number: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    val theme = seamTheme
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(22.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number,
                style = theme.typography.bodyMediumBold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Text(
            text = text,
            style = theme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockInstructionsPreview() {
    SeamThemeProvider {
        UnlockInstructions()
    }
}
