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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.seam.core.api.SeamCredential
import co.seam.core.api.SeamSDK
import co.seam.core.sdkerrors.SeamCredentialError
import co.seam.core.sdkerrors.SeamError
import co.seam.core.sdkerrors.SeamRequiredUserInteraction
import co.seam.seamcomponents.ui.components.keys.KeyCard
import co.seam.seamcomponents.ui.components.keys.KeyCardErrorState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime

/**
 * UI state for the Keys screen - handles content and loading states
 */
sealed class KeysUiState {
    object Loading : KeysUiState()

    data class Success(val keys: List<KeyCard>) : KeysUiState()

    object Empty : KeysUiState()
}

/**
 * ViewModel for the Keys screen - manages key cards display and refresh functionality
 */
class KeysViewModel : ViewModel() {
    private var credentialsCache: List<SeamCredential> = emptyList()
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _uiState = MutableStateFlow<KeysUiState>(KeysUiState.Loading)
    val uiState: StateFlow<KeysUiState> = _uiState.asStateFlow()

    private val _errorMessageState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorMessageState.asStateFlow()

    private var initializationTime = System.currentTimeMillis()

    init {
        startCollectingCredentials()
    }

    /**
     * Starts collecting credentials from the SeamSDK
     * This should be called once the SDK is initialized
     */
    fun startCollectingCredentials() {
        initializationTime = System.currentTimeMillis()
        viewModelScope.launch(dispatcher) {
            try {
                SeamSDK.getInstance().credentials.collect { credentialsList ->
                    processCredentials(credentialsList)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    /**
     * Processes credentials from the SeamSDK and converts them to KeyCard objects
     */
    private fun processCredentials(credentialsList: List<SeamCredential>) {
        credentialsCache = credentialsList
        if (credentialsList.isEmpty()) {
            val elapsedTime = (System.currentTimeMillis() - initializationTime)
            if (elapsedTime >= INITIALIZATION_TIMEOUT) {
                _uiState.value = KeysUiState.Empty
            } else {
                _uiState.value = KeysUiState.Loading
            }
            return
        }

        val keyCards =
            credentialsList.map { credential ->
                convertSeamCredentialToKeyCard(credential)
            }

        _uiState.value = KeysUiState.Success(keyCards)
    }

    /**
     * Converts a SeamCredential to a KeyCard for UI display
     */
    private fun convertSeamCredentialToKeyCard(credential: SeamCredential): KeyCard {
        val firstErrorToSolve = credential.errors.toKeyCardErrorState()
        return KeyCard(
            id = credential.id ?: "",
            location = credential.location,
            name = credential.name,
            checkoutDate = credential.expiry?.toJavaLocalDateTime(),
            code = credential.code,
            firstErrorToSolve = firstErrorToSolve,
        )
    }

    /**
     * Refreshes credentials from the server
     */
    fun refreshCredentials() {
        viewModelScope.launch(dispatcher) {
            try {
                _uiState.value = KeysUiState.Loading
                val refreshedCredentials = SeamSDK.getInstance().refresh()
                processCredentials(refreshedCredentials)
                _errorMessageState.value = null // Clear error on successful refresh
            } catch (e: Exception) {
                println("KeysViewModel - Error refreshing credentials: ${e.message}")
                e.printStackTrace()
                handleError(e)
            }
        }
    }

    /**
     * Clears the current error message
     */
    fun clearError() {
        _errorMessageState.value = null
    }

    fun reinitialize() {
        initializationTime = System.currentTimeMillis()
    }

    /**
     * Handles errors from SeamSDK operations
     */
    private fun handleError(e: Exception) {
        val errorMessage =
            when (e) {
                is SeamError.InitializationRequired -> "SDK not initialized"
                is SeamError.ActivationRequired -> "SDK not activated"
                is SeamError.InternetConnectionRequired -> "Internet connection required"
                is SeamError.InvalidClientSessionToken -> "Invalid client session token"
                else -> e.message ?: "Failed to load credentials"
            }
        _errorMessageState.value = errorMessage
        _uiState.value = KeysUiState.Empty // Keep UI in empty state, error will show in banner
    }

    fun verifyKeyCardHasErrors(keyCard: KeyCard?): Boolean {
        if (keyCard == null) {
            return true
        }
        return keyCard.firstErrorToSolve != KeyCardErrorState.None
    }

    companion object {
        const val INITIALIZATION_TIMEOUT = 10_000L
    }
}

private fun List<SeamCredentialError>.toKeyCardErrorState(): KeyCardErrorState {
    val firstError = this.firstOrNull()
    if (firstError == null) {
        return KeyCardErrorState.None
    }
    return when (firstError) {
        is SeamCredentialError.Expired -> KeyCardErrorState.CredentialExpired
        is SeamCredentialError.Loading -> KeyCardErrorState.CredentialLoading
        is SeamCredentialError.Unknown -> KeyCardErrorState.None
        is SeamCredentialError.UserInteractionRequired -> {
            val interaction = firstError.interaction
            when (interaction) {
                is SeamRequiredUserInteraction.CompleteOtpAuthorization ->
                    KeyCardErrorState.CompleteOtpAuthorization(interaction.otpUrl)
                is SeamRequiredUserInteraction.GrantPermissions ->
                    KeyCardErrorState.GrantPermissions(interaction.permissions)
                is SeamRequiredUserInteraction.EnableBluetooth ->
                    KeyCardErrorState.EnableBluetooth
                is SeamRequiredUserInteraction.EnableInternet ->
                    KeyCardErrorState.EnableInternet
            }
        }
    }
}
