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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.lighten
import co.seam.seamcomponents.ui.theme.SeamTheme
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

    val buttonPrimaryColorLighter = MaterialTheme.colorScheme.primary.lighten(0.5f)
    val buttonPrimaryColor = MaterialTheme.colorScheme.primary

    // Success state - green background with checkmark
    when (unlockPhase) {
        UnlockPhase.IDLE -> {
            Box(
                modifier = Modifier
                    .size(168.dp)
                    .padding(8.dp)
                    .shadow(
                        elevation = 24.dp,
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
                    tint = MaterialTheme.colorScheme.onPrimary,
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
                        .background(MaterialTheme.colorScheme.background)
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
                        tint = MaterialTheme.colorScheme.onBackground,
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
                                colors = listOf(
                                    MaterialTheme.colorScheme.success,
                                    MaterialTheme.colorScheme.success.lighten()
                                ),
                            ),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.onPrimary,
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
    SeamTheme {
        UnlockButton(
            unlockPhase = UnlockPhase.IDLE,
            onPress = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockButtonScanningPreview() {
    SeamTheme {
        UnlockButton(
            unlockPhase = UnlockPhase.SCANNING,
            onPress = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockButtonSuccessPreview() {
    SeamTheme {
        UnlockButton(
            unlockPhase = UnlockPhase.SUCCESS,
            onPress = { },
        )
    }
}
