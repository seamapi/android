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

package co.seam.seamcomponents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.ErrorBanner
import co.seam.seamcomponents.ui.components.common.darken
import co.seam.seamcomponents.ui.components.keys.KeyCard
import co.seam.seamcomponents.ui.components.common.SeamPrimaryButton
import co.seam.seamcomponents.ui.components.common.SeamSecondaryButton
import co.seam.seamcomponents.ui.components.unlock.UnlockContent
import co.seam.seamcomponents.ui.components.unlock.UnlockError
import co.seam.seamcomponents.ui.components.unlock.UnlockHeader
import co.seam.seamcomponents.ui.components.unlock.UnlockPhase
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.seamTheme
import co.seam.seamcomponents.ui.viewmodel.UnlockViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeamUnlockCardView(
    keyCard: KeyCard,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UnlockViewModel = viewModel(),
) {
    val unlockCardStyle = seamTheme.unlockCard
    val containerColor = unlockCardStyle.cardBackground ?: MaterialTheme.colorScheme.background
    val headerContainerColor = unlockCardStyle.headerBackground
        ?: MaterialTheme.colorScheme.background.darken(0.7f)

    val unlockPhase by viewModel.unlockPhase.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Handle dismiss - reset state and navigate back
    val handleDismiss = {
        viewModel.resetState()
        onNavigateBack()
    }

    // Use ModalBottomSheet which renders at window level
    ModalBottomSheet(
        onDismissRequest = handleDismiss,
        sheetState = bottomSheetState,
        modifier = modifier
            .safeDrawingPadding()
            .padding(top = 32.dp),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        containerColor = containerColor,
        tonalElevation = 8.dp,
        dragHandle = null
    ) {
        // Bottom sheet content with minimum height for peek behavior
        Column(
            modifier = Modifier
                .background(containerColor)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerContainerColor)
                    .padding(top = 4.dp)
            ) {
                Box(
                    modifier
                        .align(Alignment.CenterHorizontally)
                        .width(32.dp)
                        .height(6.dp)
                        .background(containerColor, RoundedCornerShape(2.dp))
                )
                UnlockHeader(
                    keyCard = keyCard,
                )
            }

            if (unlockPhase == UnlockPhase.FAILED) {
                UnlockError(
                    title = stringResource(R.string.unlock_content_unlocking_failed_title),
                    description = stringResource(R.string.unlock_content_unlocking_failed_description),
                    modifier = Modifier.padding(16.dp),
                    onTryAgain = { viewModel.unlockCredential(keyCard.id) },
                )
            } else {
                ErrorBanner(
                    errorMessage = errorState,
                    onDismiss = viewModel::clearError
                )
                UnlockContent(
                    unlockPhase = unlockPhase,
                    modifier = Modifier.padding(16.dp),
                    onPressPrimaryButton = {
                        if (unlockPhase == UnlockPhase.IDLE) {
                            viewModel.unlockCredential(keyCard.id)
                        } else if (unlockPhase == UnlockPhase.SCANNING) {
                            viewModel.cancelUnlock()
                        }
                    }
                )
            }

            // Spacer to push buttons to bottom when fully expanded
            Spacer(
                modifier = Modifier.weight(1f)
            )

            // Footer with Cancel/Done button
            if (unlockPhase == UnlockPhase.SUCCESS || unlockPhase == UnlockPhase.FAILED) {
                SeamPrimaryButton(
                    buttonText = stringResource(R.string.done),
                    onClick = handleDismiss,
                    modifier = Modifier
                        .padding(bottom = 72.dp)
                        .padding(horizontal = 16.dp)
                )
            } else if (unlockPhase == UnlockPhase.SCANNING) {
                SeamSecondaryButton(
                    buttonText = stringResource(R.string.cancel),
                    onClick = { viewModel.cancelUnlock() },
                    modifier = Modifier
                        .padding(bottom = 72.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockScreenPreview() {
    SeamThemeProvider {
        val mockKeyCard =
            KeyCard(
                id = "key-1",
                location = "Grand Hotel & Spa",
                name = "1205",
                checkoutDate = LocalDateTime.now().plusDays(2),
                code = "1234",
            )

        SeamUnlockCardView(
            keyCard = mockKeyCard,
            onNavigateBack = { },
        )
    }
}

