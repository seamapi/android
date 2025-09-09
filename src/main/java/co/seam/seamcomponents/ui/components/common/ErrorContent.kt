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

package co.seam.seamcomponents.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

/**
 * A composable that displays an error state with appropriate messaging and retry functionality.
 *
 * This component shows a full-screen error interface with a title, contextual error message
 * based on the error type, and a retry button. It handles different types of Seam errors
 * and provides user-friendly messages for each scenario.
 *
 * @param errorState The specific error state that occurred, used to determine the displayed message
 * @param onRetry Callback invoked when the user taps the retry button to attempt the operation again
 * @param modifier Optional Modifier for styling and layout customization
 *
 */
@Composable
fun ErrorContent(
    errorState: SeamErrorState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.error_content_title),
            style = seamTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = getErrorMessage(errorState),
            style = seamTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SeamSecondaryButton(
            buttonText = stringResource(id = R.string.error_content_try_again),
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun getErrorMessage(errorState: SeamErrorState): String {
    return when (errorState) {
        is SeamErrorState.InitializationRequired -> stringResource(id = R.string.error_initialization_required)
        is SeamErrorState.ActivationRequired -> stringResource(id = R.string.error_activation_required)
        is SeamErrorState.IntegrationNotFound -> stringResource(id = R.string.error_integration_not_found)
        is SeamErrorState.AlreadyInitialized -> stringResource(id = R.string.error_already_initialized)
        is SeamErrorState.DeactivationInProgress -> stringResource(id = R.string.error_deactivation_in_progress)
        is SeamErrorState.InvalidCredentialId -> stringResource(id = R.string.error_invalid_credential_id)
        is SeamErrorState.InternetConnectionRequired -> stringResource(id = R.string.error_internet_connection_required)
        is SeamErrorState.InvalidClientSessionToken -> stringResource(id = R.string.error_invalid_client_session_token)
        is SeamErrorState.InvalidUnlockProximity -> stringResource(id = R.string.error_invalid_unlock_proximity)
        is SeamErrorState.Unknown -> stringResource(id = R.string.error_unknown)
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentPreview() {
    SeamThemeProvider {
        ErrorContent(
            errorState = SeamErrorState.InternetConnectionRequired,
            onRetry = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentInitializationRequiredPreview() {
    SeamThemeProvider {
        ErrorContent(
            errorState = SeamErrorState.InitializationRequired,
            onRetry = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentUnknownPreview() {
    SeamThemeProvider {
        ErrorContent(
            errorState = SeamErrorState.Unknown,
            onRetry = { },
        )
    }
}
