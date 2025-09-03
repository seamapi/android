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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.ShadowCard
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun KeyCardComponent(
    keyCard: KeyCard,
    onPress: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {

    // Use theme for all styling
    val gradientColors = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surfaceVariant
    )

    val cornerRadius = 16.dp

    // Format checkout date
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d 'at' h:mm a", Locale.getDefault())
    val formattedDate = keyCard.checkoutDate?.format(formatter) ?: "No expiry"

    val now = LocalDateTime.now()
    val hasExpired = keyCard.checkoutDate != null && keyCard.checkoutDate.isBefore(now)

    val borderGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xff939393), // Bottom left color
            Color(0xffffffff)  // Top right color
        ),
        start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom left
        end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top right
    )
    ShadowCard(
        modifier =
            modifier
                .fillMaxWidth()
                .height(192.dp),
        cornerRadius = cornerRadius,
        shadowColor = Color(0x33000000),
        shadowBlur = 8.dp,
        shadowOffsetY = 5.dp,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .border(0.5.dp, borderGradient, RoundedCornerShape(cornerRadius))
                    .clickable(enabled = onPress != null && !hasExpired) { onPress?.invoke() },
        ) {
            // Gradient background
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(Brush.linearGradient(colors = gradientColors))
            )

            // Background texture overlay
            Image(
                painter = painterResource(id = seamTheme.keyCard.backgroundTextureRes ?: R.drawable.card_gradient),
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cornerRadius))
                        .alpha(0.4f),
                contentScale = ContentScale.Crop,
            )

            // Accent bar at top right
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .width(149.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(topEnd = cornerRadius))
                        .background(MaterialTheme.colorScheme.primary),
            )

            // Card content
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Brand section (top right aligned)
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    val brandLogoRes = seamTheme.keyCard.brandLogoRes
                    // Brand logo or name display
                    when {
                        seamTheme.keyCard.brandLogoUrl != null -> {
                            AsyncImage(
                                model = seamTheme.keyCard.brandLogoUrl,
                                contentDescription = "Brand logo",
                                modifier =
                                    Modifier
                                        .size(width = 120.dp, height = 80.dp),
                            )
                        }
                        brandLogoRes != null -> {
                            Image(
                                painter = painterResource(id = brandLogoRes),
                                contentDescription = "Brand logo",
                                modifier =
                                    Modifier
                                        .size(width = 120.dp, height = 80.dp),
                            )
                        }
                    }
                }


                // Hotel info section (bottom left aligned)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = spacedBy(4.dp),
                ) {
                    Text(
                        text = keyCard.name,
                        style = seamTheme.typography.titleSmallSemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    // Room info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.access_code_label),
                            style = seamTheme.typography.captionSemiBold,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text(
                            text = keyCard.code ?: "-",
                            style = seamTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }

                    // Checkout info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.check_out_uppercase),
                            style = seamTheme.typography.captionSemiBold,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text(
                            text = formattedDate,
                            style = seamTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }

            if (hasExpired) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xAAFFFFFF))
                )
                Row(
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_error),
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "Error icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Expired",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun KeyCardComponentPreview() {
    SeamThemeProvider {
        KeyCardComponent(
            keyCard =
                KeyCard(
                    id = "1",
                    location = "Grand Hotel & Spa",
                    name = "205",
                    code = "1234",
                    checkoutDate = LocalDateTime.now().plusDays(2),
                ),
            onPress = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KeyCardComponentSimplePreview() {
    SeamThemeProvider {
        KeyCardComponent(
            keyCard =
                KeyCard(
                    id = "2",
                    location = "Downtown Plaza",
                    name = "1401",
                    checkoutDate = LocalDateTime.now().plusDays(1),
                    code = "1234",
                ),
            onPress = { },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun KeyCardComponentExpiredPreview() {
    SeamThemeProvider {
        KeyCardComponent(
            keyCard =
                KeyCard(
                    id = "3",
                    location = "Small Hotel",
                    name = "1201",
                    checkoutDate = LocalDateTime.now().minusDays(1),
                    code = null,
                ),
            onPress = { },
        )
    }
}
