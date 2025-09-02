package co.seam.seamcomponents.ui.components.unlock

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.theme.SeamTheme
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme

@Composable
fun SeamButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    textColor: Color? = null,
    enabled: Boolean = true,
    isFullWidth: Boolean = false,
) {
    val theme = seamTheme
    val buttonBackgroundColor = backgroundColor ?: MaterialTheme.colorScheme.primary
    val buttonTextColor = textColor ?: MaterialTheme.colorScheme.onPrimary
    Button(
        onClick = onClick,
        modifier =
            modifier
                .height(54.dp)
                .then(if (isFullWidth) Modifier.fillMaxWidth() else Modifier)
                .then(if (borderColor != null) Modifier.border(1.dp, borderColor, RoundedCornerShape(10.dp)) else Modifier)
                .padding(horizontal = if (!isFullWidth) 24.dp else 0.dp),
        shape = RoundedCornerShape(10.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = buttonBackgroundColor,
                contentColor = buttonTextColor,
                disabledContainerColor = buttonBackgroundColor.copy(alpha = 0.6f),
                disabledContentColor = buttonTextColor.copy(alpha = 0.6f),
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = theme.typography.buttonText,
        )
    }
}

@Composable
fun SeamPrimaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    SeamButton(
        text = buttonText,
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary,
        onClick = onClick,
        isFullWidth = true,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun SeamSecondaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    SeamButton(
        text = buttonText,
        backgroundColor = MaterialTheme.colorScheme.secondary,
        borderColor = MaterialTheme.colorScheme.outline,
        textColor = MaterialTheme.colorScheme.onSecondary,
        onClick = onClick,
        isFullWidth = true,
        modifier = modifier.fillMaxWidth(),
    )
}


@Preview(showBackground = true)
@Composable
fun SeamPrimaryButtonPreview() {
    SeamThemeProvider {
        SeamPrimaryButton(
            buttonText = "Primary Button",
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeamSecondaryButtonPreview() {
    SeamThemeProvider {
        SeamSecondaryButton(
            buttonText = "Secondary Button",
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeamButtonPreview() {
    SeamTheme {
        SeamButton(
            text = "Tap to Unlock",
            onClick = { },
            isFullWidth = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeamButtonDisabledPreview() {
    SeamThemeProvider {
        SeamButton(
            text = "Disabled Button",
            onClick = { },
            enabled = false,
            isFullWidth = true,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SeamButtonWithBorderPreview() {
    SeamThemeProvider {
        SeamButton(
            text = "Border Button",
            onClick = { },
            borderColor = MaterialTheme.colorScheme.onSurface,
            isFullWidth = true,
        )
    }
}
