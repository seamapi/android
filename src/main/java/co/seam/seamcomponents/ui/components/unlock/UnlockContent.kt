package co.seam.seamcomponents.ui.components.unlock

import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.theme.SeamThemeProvider

@Composable
fun UnlockContent(
    unlockPhase: UnlockPhase,
    modifier: Modifier = Modifier,
    onPressPrimaryButton: () -> Unit,
) {
    // Normal unlock UI
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Unlock button
        UnlockButton(
            unlockPhase = unlockPhase,
            onPress = onPressPrimaryButton,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Status message for active phases
        StatusMessage(
            instructionText = getInstructionTextByUnlockPhase(unlockPhase),
            statusText = getTitleByUnlockPhase(unlockPhase),
        )
        if (unlockPhase == UnlockPhase.IDLE) {
            UnlockInstructions()
        } else if (unlockPhase == UnlockPhase.SCANNING) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.phone_and_salto_lock)
                    .decoderFactory(GifDecoder.Factory())
                    .build(),
                contentDescription = stringResource(R.string.unlock_instructions_phone_illustration_description),
                modifier = Modifier.size(width = 155.dp, height = 174.dp),
            )
        }
    }
}

@Composable
private fun getTitleByUnlockPhase(unlockPhase: UnlockPhase): String {
    return when (unlockPhase) {
        UnlockPhase.IDLE -> stringResource(R.string.unlock_content_tap_to_unlock)
        UnlockPhase.SCANNING -> stringResource(R.string.unlock_content_connecting)
        UnlockPhase.SUCCESS -> stringResource(R.string.unlock_content_unlocked)
        UnlockPhase.FAILED -> stringResource(R.string.unlock_content_failed)
    }
}

@Composable
private fun getInstructionTextByUnlockPhase(unlockPhase: UnlockPhase): String? {
    return when (unlockPhase) {
        UnlockPhase.SCANNING -> stringResource(R.string.unlock_content_scanning_instruction)
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockContentIdlePreview() {
    SeamThemeProvider {
        UnlockContent(
            unlockPhase = UnlockPhase.IDLE,
            onPressPrimaryButton = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockContentLoadingPreview() {
    SeamThemeProvider {
        UnlockContent(
            unlockPhase = UnlockPhase.IDLE,
            onPressPrimaryButton = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockContentScanningPreview() {
    SeamThemeProvider {
        UnlockContent(
            unlockPhase = UnlockPhase.SCANNING,
            onPressPrimaryButton = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockContentSuccessPreview() {
    SeamThemeProvider {
        UnlockContent(
            unlockPhase = UnlockPhase.SUCCESS,
            onPressPrimaryButton = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockContentFailedPreview() {
    SeamThemeProvider {
        UnlockContent(
            unlockPhase = UnlockPhase.FAILED,
            onPressPrimaryButton = { },
        )
    }
}
