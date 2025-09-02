package co.seam.seamcomponents.ui.components.common

sealed class SeamErrorState {
    object InitializationRequired : SeamErrorState()
    object ActivationRequired : SeamErrorState()
    object IntegrationNotFound : SeamErrorState()
    object AlreadyInitialized : SeamErrorState()
    object DeactivationInProgress : SeamErrorState()
    object InvalidCredentialId : SeamErrorState()
    object InternetConnectionRequired : SeamErrorState()
    object InvalidClientSessionToken : SeamErrorState()
    object InvalidUnlockProximity : SeamErrorState()
    object Unknown : SeamErrorState()
}
