package empire.digiprem.mycoloapp.feature.registration.data.dto

import empire.digiprem.mycoloapp.feature.registration.domain.model.EducationLevel
import empire.digiprem.mycoloapp.feature.registration.domain.model.RegistrationForm
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantInsertDto(
    val fullName: String,
    val familyName: String,
    val city: String,
    val birthDate: String,
    val educationLevel: String,
    val securityCodeId: String,
    val genre: String,
)

fun RegistrationForm.toInsertDto(): ParticipantInsertDto = ParticipantInsertDto(
    fullName       = fullName.trim(),
    familyName     = familyName.trim(),
    birthDate      = birthDate,
    educationLevel      = educationLevel?.name ?: EducationLevel.SECONDARY.name,
    securityCodeId = securityCode,
    genre          = genre.name,
    city         = city,
)
