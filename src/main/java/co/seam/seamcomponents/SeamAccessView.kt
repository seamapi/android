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

package co.seam.seamcomponents

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.seam.seamcomponents.navigation.SeamDestinations
import co.seam.seamcomponents.ui.components.userinteraction.BluetoothRedirectScreen
import co.seam.seamcomponents.ui.components.common.ErrorContent
import co.seam.seamcomponents.ui.components.common.LoadingContent
import co.seam.seamcomponents.ui.components.keys.KeyCard
import co.seam.seamcomponents.ui.components.keys.KeyCardErrorState
import co.seam.seamcomponents.ui.components.userinteraction.PermissionsRedirectScreen
import co.seam.seamcomponents.ui.screens.SeamCredentialsView
import co.seam.seamcomponents.ui.screens.SeamOtpView
import co.seam.seamcomponents.ui.screens.SeamUnlockCardView
import co.seam.seamcomponents.ui.viewmodel.KeysViewModel
import co.seam.seamcomponents.ui.viewmodel.SeamSDKState
import co.seam.seamcomponents.ui.viewmodel.SeamViewModel
import co.seam.seamcomponents.ui.viewmodel.UnlockViewModel

/**
 * The main entry point composable for the Seam access management interface.
 *
 * This composable manages the entire user flow for accessing and managing credentials through
 * the Seam SDK. It handles SDK initialization, navigation between different screens, and manages
 * various UI states including loading, error handling, OTP authorization, and Bluetooth permissions.
 *
 * The component orchestrates navigation between:
 * - Credentials list screen for viewing available keys
 * - OTP authorization screen for completing authentication flows
 * - Bluetooth redirect screen for permission handling
 * - Unlock overlay for interacting with individual credentials
 *
 * @param clientSessionToken The session token required for SDK authentication and initialization
 * @param context The Android context, defaults to the current composition's local context
 * @param navController The navigation controller for managing screen transitions,
 * defaults to a new instance
 *
 */
@Composable
fun SeamAccessView(
    clientSessionToken: String,
    context: Context = LocalContext.current,
    navController: NavHostController = rememberNavController(),
) {
    // Create separate ViewModels for different responsibilities
    val seamViewModel: SeamViewModel = viewModel()
    val keysViewModel: KeysViewModel = viewModel()
    val unlockViewModel: UnlockViewModel = viewModel()

    val sdkState by seamViewModel.sdkState.collectAsState()

    // Initialize the SeamSDK when the component is first composed
    LaunchedEffect(clientSessionToken) {
        keysViewModel.reinitialize()
        seamViewModel.initializeSeamSDK(context, clientSessionToken)
    }

    // Overlay state for unlock screen
    var showUnlockOverlay by remember { mutableStateOf(false) }
    var selectedKeyCard by remember { mutableStateOf<KeyCard?>(null) }


    // Use theme from parent SeamThemeProvider
    Box(modifier = Modifier.fillMaxSize()) {
        // Base layer - Navigation host
        NavHost(
            navController = navController,
            startDestination = SeamDestinations.KEYS_ROUTE,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(SeamDestinations.KEYS_ROUTE) {
                when (sdkState) {
                    is SeamSDKState.Error -> {
                        ErrorContent(
                            (sdkState as SeamSDKState.Error).errorState,
                            onRetry = {
                                seamViewModel.initializeSeamSDK(context, clientSessionToken)
                            },
                        )
                    }
                    SeamSDKState.Idle, SeamSDKState.Initializing -> {
                        LoadingContent()
                    }
                    SeamSDKState.Initialized -> {
                        SeamCredentialsView(
                            viewModel = keysViewModel,
                            onNavigateToUnlock = { keyCard ->
                                val hasError = keysViewModel.verifyKeyCardHasErrors(keyCard)
                                if (hasError) {
                                    navigateToKeyCardError(navController, keyCard)
                                } else {
                                    selectedKeyCard = keyCard
                                    showUnlockOverlay = true
                                }
                            },
                        )
                    }
                }
            }

            composable("${SeamDestinations.OTP_AUTHORIZATION_ROUTE}/{otpUrl}") { backStackEntry ->
                val encodedOtpUrl = backStackEntry.arguments?.getString("otpUrl")
                encodedOtpUrl?.let {
                    val decodedOtpUrl = Uri.decode(it)
                    SeamOtpView(
                        otpUrl = decodedOtpUrl,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                    )
                }
            }

            composable(SeamDestinations.BLUETOOTH_REDIRECT_ROUTE) {
                BluetoothRedirectScreen(
                    onSkipClicked = {
                        navController.popBackStack()
                    },
                )
            }

            composable(SeamDestinations.PERMISSION_REDIRECT_ROUTE) {
                PermissionsRedirectScreen(
                    onSkipClicked = {
                        navController.popBackStack()
                    },
                )
            }
        }

        if (showUnlockOverlay) {
            selectedKeyCard?.let { keyCard ->
                SeamUnlockCardView(
                    keyCard = keyCard,
                    onNavigateBack = {
                        showUnlockOverlay = false
                        selectedKeyCard = null
                        unlockViewModel.resetState()
                        unlockViewModel.cancelUnlock()
                    },
                    viewModel = unlockViewModel,
                )
            }
        }
    }
}

private fun navigateToKeyCardError(navController: NavHostController, keyCard: KeyCard) {
    val credentialsErrorState = keyCard.firstErrorToSolve
    when (credentialsErrorState) {
        is KeyCardErrorState.CompleteOtpAuthorization -> {
            val otpUrl = credentialsErrorState.otpUrl
            val encodedUrl = Uri.encode(otpUrl.toString())
            navController.navigate("${SeamDestinations.OTP_AUTHORIZATION_ROUTE}/$encodedUrl")
        }
        is KeyCardErrorState.GrantPermissions -> {
            navController.navigate(SeamDestinations.PERMISSION_REDIRECT_ROUTE)
        }
        is KeyCardErrorState.EnableBluetooth -> {
            navController.navigate(SeamDestinations.BLUETOOTH_REDIRECT_ROUTE)
        }

        KeyCardErrorState.CredentialExpired -> {}
        KeyCardErrorState.CredentialLoading -> {}
        KeyCardErrorState.EnableInternet -> {}
        KeyCardErrorState.None -> {

        }
    }
}
