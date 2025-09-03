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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.ErrorBanner
import co.seam.seamcomponents.ui.components.common.darken
import co.seam.seamcomponents.ui.components.keys.KeyCard
import co.seam.seamcomponents.ui.components.unlock.SeamPrimaryButton
import co.seam.seamcomponents.ui.components.unlock.SeamSecondaryButton
import co.seam.seamcomponents.ui.components.unlock.UnlockContent
import co.seam.seamcomponents.ui.components.unlock.UnlockError
import co.seam.seamcomponents.ui.components.unlock.UnlockHeader
import co.seam.seamcomponents.ui.components.unlock.UnlockPhase
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
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
    val unlockPhase by viewModel.unlockPhase.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val peekHeight = screenHeight - 64.dp

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    // Animate sheet to peek height when screen loads
    LaunchedEffect(Unit) {
        bottomSheetState.partialExpand()
    }

    // Handle dismiss - when sheet is hidden, navigate back
    LaunchedEffect(bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue == SheetValue.Hidden && bottomSheetState.targetValue == SheetValue.Hidden) {
            viewModel.resetState() // Reset state
            onNavigateBack()
        }
    }

    // Calculate background alpha based on sheet position for smooth transparency animation
    val backgroundAlpha by remember {
        derivedStateOf {
            if (bottomSheetState.hasPartiallyExpandedState) {
                val peekHeightPx = peekHeight.value * configuration.densityDpi / 160f
                val screenHeightPx = screenHeight.value * configuration.densityDpi / 160f

                val offset = bottomSheetState.requireOffset()

                val dragPosition = (offset - (screenHeightPx - peekHeightPx)).coerceAtLeast(0.001f)
                val dragProgress = dragPosition / peekHeightPx
                1f - dragProgress
            } else {
                1f
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = modifier
            .fillMaxSize(),
        sheetPeekHeight = peekHeight,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        containerColor = Color.Transparent,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.background.darken(0.8f)
            )
        },
        sheetContent = {
            // Bottom sheet content - fillMaxHeight to allow full expansion
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                if (unlockPhase == UnlockPhase.FAILED) {
                    UnlockError(
                        title = stringResource(R.string.unlock_content_unlocking_failed_title),
                        description = stringResource(R.string.unlock_content_unlocking_failed_description),
                        modifier = Modifier.padding(top = 16.dp),
                        onTryAgain = { viewModel.unlockCredential(keyCard.id) },
                    )
                } else {
                    UnlockContent(
                        unlockPhase = unlockPhase,
                        modifier = Modifier.padding(top = 16.dp),
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
                        onClick = {
                            viewModel.resetState() // Reset state
                            onNavigateBack()
                        },
                        modifier = Modifier.padding(bottom = 72.dp)
                    )
                } else if (unlockPhase == UnlockPhase.SCANNING) {
                    SeamSecondaryButton(
                        buttonText = stringResource(R.string.cancel),
                        onClick = {
                            viewModel.resetState() // Reset state
                            onNavigateBack()
                        },
                        modifier = Modifier.padding(bottom = 72.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        // Main content - Fixed header at top
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(
                    color = MaterialTheme.colorScheme.background.darken(0.8f)
                        .copy(alpha = backgroundAlpha)
                )
                .padding(innerPadding)
                .alpha(backgroundAlpha)
        ) {
            UnlockHeader(
                keyCard = keyCard,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            )
            ErrorBanner(
                errorMessage = errorState,
                onDismiss = viewModel::clearError
            )
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

