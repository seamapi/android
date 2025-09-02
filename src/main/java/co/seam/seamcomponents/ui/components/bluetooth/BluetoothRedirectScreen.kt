package co.seam.seamcomponents.ui.components.bluetooth

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.unlock.SeamPrimaryButton
import co.seam.seamcomponents.ui.components.unlock.SeamSecondaryButton
import co.seam.seamcomponents.ui.theme.SeamThemeProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothRedirectScreen(
    modifier: Modifier = Modifier,
    onSkipClicked: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // Main content area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bluetooth),
                contentDescription = "Bluetooth",
                tint = null,
                modifier = Modifier.size(100.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.bluetooth_turned_off_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground, // slate-900
                textAlign = TextAlign.Center,
                lineHeight = 29.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.bluetooth_turned_off_description),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Settings button
            SeamPrimaryButton(
                buttonText = stringResource(R.string.go_to_settings),
                onClick = {
                    val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                    context.startActivity(intent)
                }
            )
        }

        // Skip button at bottom
        if (onSkipClicked != null) {
            SeamSecondaryButton(
                buttonText = stringResource(R.string.skip),
                modifier = Modifier.padding(bottom = 72.dp),
                onClick = onSkipClicked
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BluetoothRedirectScreenPreview() {
    SeamThemeProvider {
        BluetoothRedirectScreen(
            onSkipClicked = { /* Skip action */ }
        )
    }
}
