package co.seam.seamcomponents.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.theme.SeamFontSize
import co.seam.seamcomponents.ui.theme.SeamFontWeight
import co.seam.seamcomponents.ui.theme.SeamThemeProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeamTopBar(
    title: String,
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = SeamFontSize.xLarge,
                    fontWeight = SeamFontWeight.bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            navigationIcon = {
                if (onNavigateBack != null) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            modifier = modifier,
        )
        Spacer(
            modifier = Modifier
                .height(0.5.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.outline),
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SeamTopBarPreview() {
    SeamThemeProvider {
        SeamTopBar(
            title = "My Mobile Keys",
        )
    }
}
