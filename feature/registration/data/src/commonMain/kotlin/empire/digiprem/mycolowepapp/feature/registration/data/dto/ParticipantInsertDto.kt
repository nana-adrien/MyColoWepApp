package empire.digiprem.mycolowepapp.feature.registration.data.dto

import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantInsertDto(
    @SerialName("full_name")         val fullName: String,
    @SerialName("family_name")       val familyName: String,
    val age: Int,
    @SerialName("job_status")        val jobStatus: String,
    @SerialName("registration_date") val registrationDate: String,
    val status: String = "PENDING"
)

fun RegistrationForm.toInsertDto(today: String): ParticipantInsertDto = ParticipantInsertDto(
    fullName         = fullName.trim(),
    familyName       = familyName.trim(),
    age              = age.toInt(),
    jobStatus        = when (jobStatus) {
        JobStatus.STUDENT_SCHOOL -> "STUDENT_SCHOOL"
        JobStatus.STUDENT_HIGHER -> "STUDENT_HIGHER"
        JobStatus.WORKER         -> "WORKER"
        JobStatus.SEEKING_WORK   -> "SEEKING_WORK"
        null                     -> "SEEKING_WORK"
    },
    registrationDate = today
)
