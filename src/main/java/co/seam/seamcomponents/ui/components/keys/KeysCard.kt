package co.seam.seamcomponents.ui.components.keys

import java.time.LocalDateTime

data class KeyCard(
    val id: String,
    val location: String?,
    val name: String,
    val code: String?,
    val checkoutDate: LocalDateTime? = null,
)
