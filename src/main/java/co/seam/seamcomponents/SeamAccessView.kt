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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import co.seam.seamcomponents.ui.components.bluetooth.BluetoothRedirectScreen
import co.seam.seamcomponents.ui.components.common.ErrorContent
import co.seam.seamcomponents.ui.components.common.LoadingContent
import co.seam.seamcomponents.ui.screens.SeamCredentialsView
import co.seam.seamcomponents.ui.screens.SeamOtpView
import co.seam.seamcomponents.ui.components.keys.KeyCard
import co.seam.seamcomponents.ui.screens.SeamUnlockCardView
import co.seam.seamcomponents.ui.viewmodel.KeysErrorState
import co.seam.seamcomponents.ui.viewmodel.KeysViewModel
import co.seam.seamcomponents.ui.viewmodel.SeamSDKState
import co.seam.seamcomponents.ui.viewmodel.SeamViewModel
import co.seam.seamcomponents.ui.viewmodel.UnlockViewModel

/**
 * Main SeamApp composable that provides the complete Seam mobile key experience.
 * This is the primary entry point for the SeamComponents module.
 *
 * @param clientSessionToken The Seam client session token for authentication
 * @param context The Android context (optional, uses LocalContext by default)
 * @param navController The navigation controller (optional, creates one by default)
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
        seamViewModel.initializeSeamSDK(context, clientSessionToken)
    }

    var isAuthorizationScreenShow by remember { mutableStateOf(false) }
    var isBluetoothScreenShow by remember { mutableStateOf(false) }
    var isBluetoothScreenSkipped by remember { mutableStateOf(false) }
    var isOtpScreenSkipped by remember { mutableStateOf(false) }

    // Overlay state for unlock screen
    var showUnlockOverlay by remember { mutableStateOf(false) }
    var selectedKeyCard by remember { mutableStateOf<KeyCard?>(null) }

    val hasBluetoothErrorState by keysViewModel.hasBluetoothErrorState.collectAsState()
    if (hasBluetoothErrorState && !isBluetoothScreenShow && !isBluetoothScreenSkipped) {
        isBluetoothScreenShow = true
        navController.navigate(SeamDestinations.BLUETOOTH_REDIRECT_ROUTE)
    }

    val credentialsErrorState by keysViewModel.credentialErrorState.collectAsState()
    when (credentialsErrorState) {
        is KeysErrorState.CompleteOtpAuthorization -> {
            if (!isAuthorizationScreenShow && !isOtpScreenSkipped) {
                isAuthorizationScreenShow = true
                val otpUrl = (credentialsErrorState as KeysErrorState.CompleteOtpAuthorization).otpUrl
                val encodedUrl = Uri.encode(otpUrl.toString())
                navController.navigate("${SeamDestinations.OTP_AUTHORIZATION_ROUTE}/${encodedUrl}")
            }
        }
        else -> {
            if (isAuthorizationScreenShow) {
                navController.popBackStack()
                isAuthorizationScreenShow = false
            }
            if (isBluetoothScreenShow) {
                navController.popBackStack()
                isBluetoothScreenShow = false
            }
        }
    }

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
                SeamSDKState.Idle,
                SeamSDKState.Initializing -> {
                    LoadingContent()
                }
                SeamSDKState.Initialized -> {
                    SeamCredentialsView(
                        onNavigateToUnlock = { keyCard ->
                            // Find the keyCard and show overlay instead of navigation
                            val keyCard = keysViewModel.getKeyCardById(keyCard.id)
                            keyCard?.let {
                                if (!keyCard.isLoading) {
                                    selectedKeyCard = it
                                    showUnlockOverlay = true
                                }
                            }
                            // reset otp skipped state
                            isOtpScreenSkipped = false
                        },
                        viewModel = keysViewModel,
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
                        isOtpScreenSkipped = true
                        navController.popBackStack()
                        isAuthorizationScreenShow = false
                    },
                )
            }
        }

        composable(SeamDestinations.BLUETOOTH_REDIRECT_ROUTE) {
            BluetoothRedirectScreen(
                onSkipClicked = {
                    isBluetoothScreenSkipped = true
                    navController.popBackStack()
                    isBluetoothScreenShow = false
                }
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
