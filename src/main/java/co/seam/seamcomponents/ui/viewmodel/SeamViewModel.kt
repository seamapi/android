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

package co.seam.seamcomponents.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.seam.core.api.SeamSDK
import co.seam.core.sdkerrors.SeamError
import co.seam.seamcomponents.ui.components.common.SeamErrorState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * SDK state for tracking SeamSDK initialization and activation
 */
sealed class SeamSDKState {
    object Idle : SeamSDKState()
    object Initializing : SeamSDKState()
    object Initialized : SeamSDKState()
    data class Error(val errorState: SeamErrorState) : SeamSDKState()
}

/**
 * ViewModel for Seam SDK core functionality
 * Handles SDK initialization, activation, and global state management
 */
class SeamViewModel : ViewModel() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _sdkState = MutableStateFlow<SeamSDKState>(SeamSDKState.Idle)
    val sdkState: StateFlow<SeamSDKState> = _sdkState.asStateFlow()

    // Store client session token
    private var clientSessionToken: String = ""

    /**
     * Initializes the Seam SDK with the provided client session token.
     *
     * @param context The application context
     * @param clientSessionToken The token used to authenticate with the Seam API
     */
    fun initializeSeamSDK(
        context: Context,
        clientSessionToken: String,
    ) {
        this.clientSessionToken = clientSessionToken

        viewModelScope.launch(dispatcher) {
            try {
                _sdkState.value = SeamSDKState.Initializing
                SeamSDK.initialize(
                    context = context,
                    clientSessionToken = clientSessionToken,
                )
                SeamSDK.getInstance().activate()
                _sdkState.value = SeamSDKState.Initialized
            } catch (seamError: SeamError) {
                val errorState = getSeamErrorState(seamError)
                if (errorState is SeamErrorState.AlreadyInitialized) {
                    _sdkState.value = SeamSDKState.Initialized
                } else {
                    errorState?.let {
                        _sdkState.value = SeamSDKState.Error(it)
                    }
                }
            }
        }
    }

    private fun getSeamErrorState(seamError: SeamError): SeamErrorState? {
        return when (seamError) {
            is SeamError.ActivationRequired -> SeamErrorState.ActivationRequired
            is SeamError.AlreadyInitialized -> SeamErrorState.AlreadyInitialized
            is SeamError.DeactivationInProgress -> SeamErrorState.DeactivationInProgress
            is SeamError.InitializationRequired -> SeamErrorState.InitializationRequired
            is SeamError.IntegrationNotFound -> SeamErrorState.IntegrationNotFound
            is SeamError.InternetConnectionRequired -> SeamErrorState.InternetConnectionRequired
            is SeamError.InvalidClientSessionToken -> SeamErrorState.InvalidClientSessionToken
            is SeamError.InvalidCredentialId -> SeamErrorState.InvalidCredentialId
            is SeamError.InvalidUnlockProximity -> SeamErrorState.InvalidUnlockProximity
            else -> SeamErrorState.Unknown
        }
    }
}
