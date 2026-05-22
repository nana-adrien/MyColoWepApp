package empire.digiprem.mycolowepapp.feature.registration.data.dto

import empire.digiprem.mycolowepapp.feature.registration.domain.model.EducationLevel
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantInsertDto(
    val fullName: String,
    val familyName: String,
    val birthDate: String,
    val jobStatus: String,
    val securityCodeId: String,
    val genre: String,
)

fun RegistrationForm.toInsertDto(): ParticipantInsertDto = ParticipantInsertDto(
    fullName       = fullName.trim(),
    familyName     = familyName.trim(),
    birthDate      = birthDate,
    jobStatus      = educationLevel?.name ?: EducationLevel.SECONDARY.name,
    securityCodeId = securityCode,
    genre          = genre.name,
)
