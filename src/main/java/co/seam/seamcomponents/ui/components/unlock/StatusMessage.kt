package co.seam.seamcomponents.ui.components.unlock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import co.seam.seamcomponents.ui.theme.seamTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.theme.SeamThemeProvider

@Composable
fun StatusMessage(
    statusText: String,
    instructionText: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Status message
        Text(
            text = statusText,
            style = seamTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        // Instruction text
        Text(
            text = instructionText ?: "",
            style = seamTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
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
