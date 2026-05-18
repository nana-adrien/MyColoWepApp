package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus

data class AdminDashboardState(
    val participants: List<Participant> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
) {
    val filteredParticipants: List<Participant>
        get() = if (searchQuery.isBlank()) participants
        else participants.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                it.familyName.contains(searchQuery, ignoreCase = true)
        }

    val totalCount: Int get() = participants.size
    val validatedCount: Int get() = participants.count { it.status == ParticipantStatus.VALIDATED }
    val pendingCount: Int get() = participants.count { it.status == ParticipantStatus.PENDING }
    val rejectedCount: Int get() = participants.count { it.status == ParticipantStatus.REJECTED }

    val byJobStatus: Map<JobStatus, Int>
        get() = JobStatus.entries.associateWith { status ->
            participants.count { it.jobStatus == status }
        }

    val byAgeRange: Map<String, Int>
        get() = mapOf(
            "15-20" to participants.count { it.age in 15..20 },
            "21-30" to participants.count { it.age in 21..30 },
            "31-45" to participants.count { it.age in 31..45 },
            "46-60" to participants.count { it.age in 46..60 }
        )
}
