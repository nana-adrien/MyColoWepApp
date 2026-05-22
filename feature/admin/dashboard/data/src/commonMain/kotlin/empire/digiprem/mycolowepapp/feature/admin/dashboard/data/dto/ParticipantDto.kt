package empire.digiprem.mycolowepapp.feature.admin.dashboard.data.dto

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    val id: String,
    val fullName: String,
    val familyName: String,
    val jobStatus: String,
    val birthDate: String,
    val registeredAt: String,
    @SerialName("education_level")
    val educationLevel: String? = null,
)

fun ParticipantDto.toDomain(): Participant = Participant(
    id            = id,
    fullName      = fullName,
    familyName    = familyName,
    birthDate     = runCatching { LocalDate.parse(birthDate) }.getOrDefault(LocalDate(2000, 1, 1)),
    jobStatus     = runCatching { JobStatus.valueOf(jobStatus) }.getOrDefault(JobStatus.SEEKING_WORK),
    educationLevel = educationLevel.orEmpty(),
    registeredAt  = registeredAt,
)
