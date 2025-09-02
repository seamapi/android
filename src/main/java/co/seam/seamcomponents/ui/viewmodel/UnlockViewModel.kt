package co.seam.seamcomponents.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.seam.core.api.SeamSDK
import co.seam.core.api.UnlockProximity
import co.seam.core.events.SeamUnlockEvent
import co.seam.core.sdkerrors.SeamCredentialError
import co.seam.core.sdkerrors.SeamError
import co.seam.seamcomponents.ui.components.unlock.UnlockPhase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for unlock operations and state management
 */
class UnlockViewModel : ViewModel() {
    private var unlockJob: Job? = null
    private var successTimerJob: Job? = null
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _unlockPhase = MutableStateFlow(UnlockPhase.IDLE)
    val unlockPhase: StateFlow<UnlockPhase> = _unlockPhase.asStateFlow()

    // Unlock-specific error state
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    init {
        // Start collecting unlock status events
        startCollectingUnlockStatus()
    }

    /**
     * Starts collecting unlock status events from the SeamSDK
     */
    private fun startCollectingUnlockStatus() {
        viewModelScope.launch(dispatcher) {
            try {
                SeamSDK.getInstance().unlockStatus.filterNotNull().collect { unlockEvent ->
                    handleUnlockStatus(unlockEvent)
                }
            } catch (e: Exception) {
                println("UnlockViewModel - Error collecting unlock status: ${e.message}")
                handleError(e)
            }
        }
    }

    fun resetState() {
        unlockJob?.cancel()
        successTimerJob?.cancel()
        _unlockPhase.value = UnlockPhase.IDLE
        _errorState.value = null
    }

    fun cancelUnlock() {
        unlockJob?.cancel()
        successTimerJob?.cancel()
        _unlockPhase.value = UnlockPhase.IDLE
        _errorState.value = null
    }

    /**
     * Unlocks a credential using the SeamSDK
     */
    fun unlockCredential(credentialId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                _unlockPhase.value = UnlockPhase.SCANNING
                _errorState.value = null // Clear any existing errors

                unlockJob = SeamSDK.getInstance().unlock(
                    credentialId = credentialId,
                    unlockProximity = UnlockProximity.TOUCH,
                    timeout = 30.seconds,
                )

                // Note: The actual unlock status will be handled by handleUnlockStatus
                // which receives events from the SeamSDK unlock status flow
            } catch (e: Exception) {
                println("UnlockViewModel - Error unlocking credential: ${e.message}")
                e.printStackTrace()

                _unlockPhase.value = UnlockPhase.FAILED
                handleError(e)
            }
        }
    }

    /**
     * Clears the current error message
     */
    fun clearError() {
        _errorState.value = null
    }

    /**
     * Starts a 3-second countdown timer and changes unlock phase to IDLE if currently SUCCESS
     */
    private fun startSuccessCountdown() {
        // Cancel any existing timer
        successTimerJob?.cancel()

        successTimerJob = viewModelScope.launch(dispatcher) {
            kotlinx.coroutines.delay(3.seconds)
            // Only change to IDLE if we're still in SUCCESS state
            if (_unlockPhase.value == UnlockPhase.SUCCESS) {
                _unlockPhase.value = UnlockPhase.IDLE
            }
        }
    }

    /**
     * Handles unlock status events and updates the UI appropriately
     */
    private fun handleUnlockStatus(seamUnlockEvent: SeamUnlockEvent) {
        when (seamUnlockEvent) {
            is SeamUnlockEvent.ScanningStarted -> {
                println("UnlockViewModel - Unlock scanning started")
                _unlockPhase.value = UnlockPhase.SCANNING
            }
            is SeamUnlockEvent.AccessGranted -> {
                println("UnlockViewModel - Access granted!")
                _unlockPhase.value = UnlockPhase.SUCCESS
                _errorState.value = null // Clear any errors on success
                // Start 3-second countdown to return to IDLE
                startSuccessCountdown()
            }
            is SeamUnlockEvent.Timeout -> {
                println("UnlockViewModel - Unlock operation timed out")
                _unlockPhase.value = UnlockPhase.FAILED
                _errorState.value = "Unlock operation timed out"
            }
            is SeamUnlockEvent.ReaderError -> {
                println("UnlockViewModel - Reader error: ${seamUnlockEvent.message}")
                _unlockPhase.value = UnlockPhase.FAILED
                _errorState.value = "Reader error: ${seamUnlockEvent.message}"
            }
        }
    }

    /**
     * Handles errors from SeamSDK operations
     */
    private fun handleError(e: Exception) {
        val errorMessage = when (e) {
            is SeamError.InitializationRequired -> "SDK not initialized"
            is SeamError.ActivationRequired -> "SDK not activated"
            is SeamError.CredentialErrors -> {
                val errors = e.errors.joinToString(", ") { error ->
                    when (error) {
                        is SeamCredentialError.Expired -> "Credential expired"
                        is SeamCredentialError.Loading -> "Credential still loading"
                        is SeamCredentialError.UserInteractionRequired -> "User interaction required"
                        is SeamCredentialError.Unknown -> "Unknown credential error"
                    }
                }
                "Credential errors: $errors"
            }
            is SeamError.IntegrationNotFound -> "Integration not found"
            is SeamError.InternetConnectionRequired -> "Internet connection required"
            else -> e.message ?: "Failed to unlock credential"
        }
        _errorState.value = errorMessage
    }
}
