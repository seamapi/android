package co.seam.seamcomponents.ui.components.keys

import java.net.URL

sealed class KeyCardErrorState {
    data class CompleteOtpAuthorization(val otpUrl: URL) : KeyCardErrorState()
    data class GrantPermissions(val permissions: List<String>) : KeyCardErrorState()
    object EnableInternet : KeyCardErrorState()
    object EnableBluetooth : KeyCardErrorState()
    object CredentialExpired : KeyCardErrorState()
    object CredentialLoading : KeyCardErrorState()
    object None : KeyCardErrorState()
}
