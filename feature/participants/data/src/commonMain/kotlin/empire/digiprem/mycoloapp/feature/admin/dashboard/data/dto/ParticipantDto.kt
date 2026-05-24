package empire.digiprem.mycoloapp.feature.admin.dashboard.data.dto

import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycoloapp.feature.registration.domain.model.EducationLevel
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
