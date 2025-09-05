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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.lighten
import co.seam.seamcomponents.ui.theme.SeamComponentsThemeData
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme
import co.seam.seamcomponents.ui.theme.success

/**
 * Creates a darker shade of the given color by reducing brightness
 */

@Composable
fun UnlockButton(
    unlockPhase: UnlockPhase,
    onPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val unlockCardStyle = seamTheme.unlockCard
    val backgroundColor = unlockCardStyle.cardBackground
        ?: MaterialTheme.colorScheme.background
    val buttonGradientColors = unlockCardStyle.keyButtonGradient

    val buttonPrimaryColorLighter = buttonGradientColors?.getOrNull(0)
        ?: MaterialTheme.colorScheme.primary.lighten(0.5f)
    val buttonPrimaryColor = buttonGradientColors?.getOrNull(1)
        ?: MaterialTheme.colorScheme.primary

    val keyIconColor = when (unlockPhase) {
        UnlockPhase.IDLE -> unlockCardStyle.keyIconColorIdle ?: MaterialTheme.colorScheme.onPrimary
        UnlockPhase.SCANNING -> unlockCardStyle.keyIconColorActive ?: MaterialTheme.colorScheme.onBackground
        UnlockPhase.SUCCESS -> MaterialTheme.colorScheme.onPrimary
        UnlockPhase.FAILED -> MaterialTheme.colorScheme.onPrimary
    }

    val successColor = unlockCardStyle.successColor ?: MaterialTheme.colorScheme.success
    val successIconColor = unlockCardStyle.successIconColor ?: MaterialTheme.colorScheme.onPrimary
    val buttonElevation = unlockCardStyle.keyButtonShadowRadius ?: 12.dp

    // Success state - green background with checkmark
    when (unlockPhase) {
        UnlockPhase.IDLE -> {
            Box(
                modifier = Modifier
                    .size(168.dp)
                    .padding(8.dp)
                    .shadow(
                        elevation = buttonElevation,
                        shape = CircleShape,
                        clip = false
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                buttonPrimaryColorLighter,
                                buttonPrimaryColor
                            )
                        ),
                        shape = CircleShape
                    )
                    .clickable(onClick = onPress),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.key_large),
                    contentDescription = "Key",
                    tint = keyIconColor,
                    modifier = Modifier.size(80.dp),
                )
            }
        }
        UnlockPhase.SCANNING -> {
            Box(
                modifier =
                    modifier
                        .size(161.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .clickable(onClick = onPress),
                contentAlignment = Alignment.Center,
            ) {
                CircleSpinner(
                    size = 152,
                    borderWidth = 22f,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.key_large),
                        contentDescription = "Key",
                        tint = keyIconColor,
                        modifier = Modifier.size(80.dp),
                    )
                }
            }
        }
        UnlockPhase.SUCCESS -> {
            Box(
                modifier =
                    modifier
                        .size(160.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(successColor, successColor.lighten())
                            ),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Success",
                    tint = successIconColor,
                    modifier = Modifier.size(80.dp),
                )
            }
        }
        UnlockPhase.FAILED -> {}
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockButtonIdlePreview() {
    SeamThemeProvider {
        UnlockButton(
            unlockPhase = UnlockPhase.IDLE,
            onPress = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockButtonScanningPreview() {
    SeamThemeProvider {
        UnlockButton(
            unlockPhase = UnlockPhase.SCANNING,
            onPress = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockButtonSuccessPreview() {
    SeamThemeProvider {
        UnlockButton(
            unlockPhase = UnlockPhase.SUCCESS,
            onPress = { },
        )
    }
}
