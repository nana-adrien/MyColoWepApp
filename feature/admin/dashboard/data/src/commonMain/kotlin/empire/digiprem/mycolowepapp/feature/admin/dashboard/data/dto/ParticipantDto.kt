package empire.digiprem.mycolowepapp.feature.admin.dashboard.data.dto

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    val id: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("family_name") val familyName: String,
    val age: Int,
    @SerialName("job_status") val jobStatus: String,
    @SerialName("registration_date") val registrationDate: String,
    val status: String
)

fun ParticipantDto.toDomain(): Participant = Participant(
    id = id,
    fullName = fullName,
    familyName = familyName,
    age = age,
    jobStatus = when (jobStatus) {
        "STUDENT_SCHOOL" -> JobStatus.STUDENT_SCHOOL
        "STUDENT_HIGHER" -> JobStatus.STUDENT_HIGHER
        "WORKER" -> JobStatus.WORKER
        else -> JobStatus.SEEKING_WORK
    },
    registrationDate = registrationDate,
    status = when (status) {
        "VALIDATED" -> ParticipantStatus.VALIDATED
        "REJECTED" -> ParticipantStatus.REJECTED
        else -> ParticipantStatus.PENDING
    }
)
