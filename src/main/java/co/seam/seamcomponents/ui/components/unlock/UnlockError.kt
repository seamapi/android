package co.seam.seamcomponents.ui.components.unlock

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

@Composable
fun UnlockError(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Error icon
        Image(
            painter = painterResource(id = R.drawable.lock_error),
            contentDescription = "Unlock error icon",
            modifier = Modifier
                .size(160.dp)
                .padding(bottom = 40.dp),
        )

        // Error content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
        ) {
            // Title
            Text(
                text = title,
                style = seamTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Description
            Text(
                text = description,
                style = seamTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Try again button

        SeamSecondaryButton(
            buttonText = stringResource(R.string.unlock_try_again),
            onClick = onTryAgain,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockErrorCustomTextPreview() {
    SeamThemeProvider {
        UnlockError(
            onTryAgain = { },
            title = "Connection failed",
            description = "Unable to connect to the lock. Please check your connection and try again.",
        )
    }
}
