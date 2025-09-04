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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.seam.seamcomponents.R
import co.seam.seamcomponents.ui.components.common.ErrorBanner
import co.seam.seamcomponents.ui.components.common.ErrorInternet
import co.seam.seamcomponents.ui.components.common.LoadingContent
import co.seam.seamcomponents.ui.components.keys.EmptyKeysState
import co.seam.seamcomponents.ui.components.keys.KeyCard
import co.seam.seamcomponents.ui.components.keys.KeyCardComponent
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.viewmodel.KeysUiState
import co.seam.seamcomponents.ui.viewmodel.KeysViewModel
import java.time.LocalDateTime

@Composable
fun SeamCredentialsView(
    onNavigateToUnlock: (keyCard: KeyCard) -> Unit = {},
    viewModel: KeysViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val hasInternetErrorState by viewModel.hasInternetErrorState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Error banner appears above content when there's an error
        ErrorBanner(
            errorMessage = errorState,
            onDismiss = { viewModel.clearError() },
        )

        // Main content area
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (val currentState = uiState) {
                is KeysUiState.Loading -> {
                    LoadingContent(
                        title = stringResource(R.string.loading_mobile_keys),
                    )
                }
                is KeysUiState.Success -> {
                    KeysSuccessContent(
                        keys = currentState.keys,
                        hasInternetError = hasInternetErrorState,
                        onKeyCardClick = { keyCard ->
                            onNavigateToUnlock(keyCard)
                        },
                        onRefresh = { viewModel.refreshCredentials() },
                    )
                }
                is KeysUiState.Empty -> {
                    EmptyContent(
                        onRefresh = { viewModel.refreshCredentials() },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KeysSuccessContent(
    keys: List<KeyCard>,
    onKeyCardClick: (KeyCard) -> Unit,
    hasInternetError: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
) {
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefresh()
            isRefreshing = false
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (hasInternetError) {
                item {
                    ErrorInternet()
                }
            }
            items(keys) { keyCard ->
                KeyCardComponent(
                    keyCard = keyCard,
                    onPress = { onKeyCardClick(keyCard) },
                )
            }
        }
    }
}

@Composable
private fun EmptyContent(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        EmptyKeysState(onRefresh = onRefresh)
    }
}

@Preview(showBackground = true)
@Composable
fun KeysScreenLoadingPreview() {
    SeamThemeProvider {
        LoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
fun KeysScreenSuccessPreview() {
    SeamThemeProvider {
        val mockKeys =
            listOf(
                KeyCard(
                    id = "key-1",
                    location = "Grand Hotel & Spa",
                    name = "1205",
                    checkoutDate = LocalDateTime.now().plusDays(2),
                    code = "1234",
                ),
                KeyCard(
                    id = "key-2",
                    location = "Downtown Plaza Hotel",
                    name = "2341",
                    checkoutDate = LocalDateTime.now().plusDays(3),
                    code = "1234",
                ),
            )

        KeysSuccessContent(
            keys = mockKeys,
            hasInternetError = false,
            onKeyCardClick = {},
            modifier = Modifier,
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KeysScreenEmptyPreview() {
    SeamThemeProvider {
        EmptyContent(onRefresh = {})
    }
}
