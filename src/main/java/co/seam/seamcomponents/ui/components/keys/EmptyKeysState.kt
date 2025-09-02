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
import co.seam.seamcomponents.ui.components.unlock.SeamSecondaryButton
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

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
                .padding(24.dp),
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
