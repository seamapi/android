package co.seam.seamcomponents.ui.components.unlock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.keys.KeyCard
import co.seam.seamcomponents.ui.theme.SeamFontWeight
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Header component for the unlock bottom sheet showing key card information
 */
@Composable
fun UnlockHeader(
    keyCard: KeyCard,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Key card mini preview
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = keyCard.name,
                style = seamTheme.typography.labelMedium,
                fontWeight = SeamFontWeight.medium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            keyCard.checkoutDate?.let { checkoutDate ->
                Text(
                    text = stringResource(
                        R.string.check_out_label,
                        checkoutDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                    ),
                    style = seamTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Mini card visual (removing logo functionality)
        MiniKeyCard()
    }
}

@Composable
private fun MiniKeyCard(
    modifier: Modifier = Modifier,
) {
    val theme = seamTheme
    Box(
        modifier = modifier.size(width = 64.dp, height = 40.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Display brand logo or fallback to styled box
        when {
            theme.keyCard.brandLogoUrl != null -> {
                AsyncImage(
                    model = theme.keyCard.brandLogoUrl,
                    contentDescription = "Brand logo",
                    modifier = Modifier.size(width = 64.dp, height = 40.dp),
                )
            }
            theme.keyCard.brandLogoRes != null -> {
                Image(
                    painter = painterResource(id = theme.keyCard.brandLogoRes),
                    contentDescription = "Brand logo",
                    modifier = Modifier.size(width = 64.dp, height = 40.dp),
                )
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockHeaderPreview() {
    SeamThemeProvider {
        val mockKeyCard =
            KeyCard(
                id = "key-1",
                location = "Grand Hotel & Spa",
                name = "1205",
                checkoutDate = LocalDateTime.now().plusDays(2),
                code = "1234",
            )

        UnlockHeader(
            keyCard = mockKeyCard,
            modifier = Modifier.padding(16.dp),
        )
    }
}
