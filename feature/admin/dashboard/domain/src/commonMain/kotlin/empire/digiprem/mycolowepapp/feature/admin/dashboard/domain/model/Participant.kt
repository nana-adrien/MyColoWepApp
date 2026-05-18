package empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model

import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus

data class Participant(
    val id: String,
    val fullName: String,
    val age: Int,
    val jobStatus: JobStatus,
    val familyName: String,
    val registrationDate: String,
    val status: ParticipantStatus
)
