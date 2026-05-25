package empire.digiprem.mycoloapp.features.participants.data.dto

import empire.digiprem.mycoloapp.features.participants.domain.model.Participant
import empire.digiprem.mycoloapp.features.registration.domain.model.EducationLevel
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    val id: String,
    val fullName: String,
    val familyName: String,
    val educationLevel: String,
    val birthDate: String,
    val registeredAt: String,
)

fun ParticipantDto.toDomain(): Participant = Participant(
    id             = id,
    fullName       = fullName,
    familyName     = familyName,
    birthDate      = runCatching { LocalDate.parse(birthDate) }.getOrDefault(LocalDate(2000, 1, 1)),
    educationLevel = runCatching { EducationLevel.valueOf(educationLevel) }.getOrDefault(EducationLevel.SECONDARY),
    registeredAt   = registeredAt,
)
