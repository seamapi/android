package co.seam.seamcomponents.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.unlock.CircleSpinner
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

@Composable
fun LoadingContent(
    title: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircleSpinner(
            size = 80,
            borderWidth = 8f,
            showBackgroundRing = false,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title ?: stringResource(id = R.string.loading),
            style = seamTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingContentPreview() {
    SeamThemeProvider {
        LoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingContentWithCustomTitlePreview() {
    SeamThemeProvider {
        LoadingContent(
            title = "Initializing mobile credentials…"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingContentWithLongTitlePreview() {
    SeamThemeProvider {
        LoadingContent(
            title = "Loading your mobile keys and synchronizing with the server…"
        )
    }
}