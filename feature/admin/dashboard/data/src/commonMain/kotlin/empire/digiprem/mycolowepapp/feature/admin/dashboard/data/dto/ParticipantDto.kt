package empire.digiprem.mycolowepapp.feature.admin.dashboard.data.dto

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.registration.domain.model.EducationLevel
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    val id: String,
    val fullName: String,
    val familyName: String,
    @SerialName("job_status")
    val jobStatus: String,
    val birthDate: String,
    val registeredAt: String,
)

fun ParticipantDto.toDomain(): Participant = Participant(
    id             = id,
    fullName       = fullName,
    familyName     = familyName,
    birthDate      = runCatching { LocalDate.parse(birthDate) }.getOrDefault(LocalDate(2000, 1, 1)),
    educationLevel = runCatching { EducationLevel.valueOf(jobStatus) }.getOrDefault(EducationLevel.SECONDARY),
    registeredAt   = registeredAt,
)
