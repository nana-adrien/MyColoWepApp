package empire.digiprem.mycoloapp.feature.registration.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SecurityCodeValidationDto(
    val id: String,
    val code: String,
    @SerialName("is_active") val isActive: Boolean
)
