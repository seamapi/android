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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import java.net.URL

/**
 * UI state for the Keys screen - handles content and loading states
 */
sealed class KeysUiState {
    object Loading : KeysUiState()
    data class Success(val keys: List<KeyCard>) : KeysUiState()
    object Empty : KeysUiState()
}

sealed class KeysErrorState {
    data class CompleteOtpAuthorization(val otpUrl: URL) : KeysErrorState()
    data class GrantPermissions(val permissions: List<String>) : KeysErrorState()
    object EnableInternet : KeysErrorState()
    object EnableBluetooth : KeysErrorState()
    object None : KeysErrorState()
}

/**
 * ViewModel for the Keys screen - manages key cards display and refresh functionality
 */
class KeysViewModel : ViewModel() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _uiState = MutableStateFlow<KeysUiState>(KeysUiState.Loading)
    val uiState: StateFlow<KeysUiState> = _uiState.asStateFlow()

    // Keys-specific error state
    private val _credentialErrorState = MutableStateFlow<KeysErrorState>(KeysErrorState.None)
    val credentialErrorState: StateFlow<KeysErrorState> = _credentialErrorState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _hasBluetoothErrorState = MutableStateFlow(false)
    val hasBluetoothErrorState: StateFlow<Boolean> = _hasBluetoothErrorState.asStateFlow()

    private val _hasInternetErrorState = MutableStateFlow(false)
    val hasInternetErrorState: StateFlow<Boolean> = _hasInternetErrorState.asStateFlow()

    init {
        startCollectingCredentials()
    }

    /**
     * Starts collecting credentials from the SeamSDK
     * This should be called once the SDK is initialized
     */
    fun startCollectingCredentials() {
        viewModelScope.launch(dispatcher) {
            try {
                SeamSDK.getInstance().credentials.collect { credentialsList ->
                    println("KeysViewModel - Credentials collected: ${credentialsList.size} items")
                    processCredentials(credentialsList)
                    checkCredentialsErrors()
                }
            } catch (e: Exception) {
                println("KeysViewModel - Error collecting credentials: ${e.message}")
                handleError(e)
            }
        }
    }

    /**
     * Processes credentials from the SeamSDK and converts them to KeyCard objects
     */
    private fun processCredentials(credentialsList: List<SeamCredential>) {
        println("KeysViewModel - ProcessCredentials called with ${credentialsList.size} credentials")

        if (credentialsList.isEmpty()) {
            println("KeysViewModel - No credentials found, setting Empty state")
            _uiState.value = KeysUiState.Empty
            return
        }

        val keyCards = credentialsList.map { credential ->
            println("KeysViewModel - Converting credential: ${credential.name} (${credential.providerName})")
            convertSeamCredentialToKeyCard(credential)
        }

        println("KeysViewModel - Setting Success state with ${keyCards.size} key cards")
        _uiState.value = KeysUiState.Success(keyCards)
    }

    private fun checkCredentialsErrors() {
        val errors = getUserInteractionRequiredErrors()
        val firstError = errors.firstOrNull()
        val interaction = firstError?.interaction
        _hasBluetoothErrorState.value = errors
            .map { it.interaction }
            .filterIsInstance<SeamRequiredUserInteraction.EnableBluetooth>()
            .isNotEmpty()
        _hasInternetErrorState.value = errors
            .map { it.interaction }
            .filterIsInstance<SeamRequiredUserInteraction.EnableInternet>()
            .isNotEmpty()
        _credentialErrorState.value = when (interaction) {
            is SeamRequiredUserInteraction.CompleteOtpAuthorization ->
                KeysErrorState.CompleteOtpAuthorization(interaction.otpUrl)
            is SeamRequiredUserInteraction.GrantPermissions ->
                KeysErrorState.GrantPermissions(interaction.permissions)
            is SeamRequiredUserInteraction.EnableBluetooth ->
                KeysErrorState.EnableBluetooth
            is SeamRequiredUserInteraction.EnableInternet ->
                KeysErrorState.EnableInternet
            else -> KeysErrorState.None
        }
    }

    private fun getUserInteractionRequiredErrors(): List<SeamCredentialError.UserInteractionRequired> {
        val credentials = SeamSDK.getInstance().credentials.value
        val errors = credentials.flatMap { it.errors }
        return errors.filterIsInstance<SeamCredentialError.UserInteractionRequired>()
    }

    /**
     * Converts a SeamCredential to a KeyCard for UI display
     */
    private fun convertSeamCredentialToKeyCard(credential: SeamCredential): KeyCard {
        return KeyCard(
            id = credential.id ?: "",
            location = credential.location,
            name = credential.name,
            checkoutDate = credential.expiry?.toJavaLocalDateTime(),
            code = credential.code,
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
                _errorState.value = null // Clear error on successful refresh
            } catch (e: Exception) {
                println("KeysViewModel - Error refreshing credentials: ${e.message}")
                e.printStackTrace()
                handleError(e)
            }
        }
    }

    /**
     * Gets a key card by its ID from the current state
     */
    fun getKeyCardById(keyCardId: String): KeyCard? {
        return when (val currentState = _uiState.value) {
            is KeysUiState.Success -> currentState.keys.find { it.id == keyCardId }
            else -> null
        }
    }

    /**
     * Clears the current error message
     */
    fun clearError() {
        _errorState.value = null
    }

    /**
     * Handles errors from SeamSDK operations
     */
    private fun handleError(e: Exception) {
        val errorMessage = when (e) {
            is SeamError.InitializationRequired -> "SDK not initialized"
            is SeamError.ActivationRequired -> "SDK not activated"
            is SeamError.InternetConnectionRequired -> "Internet connection required"
            is SeamError.InvalidClientSessionToken -> "Invalid client session token"
            else -> e.message ?: "Failed to load credentials"
        }
        _errorState.value = errorMessage
        _uiState.value = KeysUiState.Empty // Keep UI in empty state, error will show in banner
    }
}
