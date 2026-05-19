package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus

// ── Section active du dashboard ──────────────────────────────────────────────

enum class DashboardSection { Overview, Participants, SecurityCodes, Settings }

// ── Formulaire d'ajout de participant ────────────────────────────────────────

data class ParticipantFormData(
    val fullName: String = "",
    val familyName: String = "",
    val age: String = "",
    val selectedJobStatus: JobStatus? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = fullName.isNotBlank()
            && familyName.isNotBlank()
            && age.toIntOrNull()?.let { it in 1..120 } == true
            && selectedJobStatus != null
}

// ── Formulaire de changement de mot de passe ─────────────────────────────────

data class PasswordFormData(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isSubmitting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = newPassword.length >= 8 && newPassword == confirmPassword

    val newPasswordError: String?
        get() = when {
            newPassword.isEmpty()  -> null
            newPassword.length < 8 -> "8 caractères minimum"
            else                   -> null
        }

    val confirmPasswordError: String?
        get() = when {
            confirmPassword.isEmpty()      -> null
            confirmPassword != newPassword -> "Les mots de passe ne correspondent pas"
            else                           -> null
        }
}

// ── État global du dashboard ─────────────────────────────────────────────────

data class AdminDashboardState(
    val participants: List<Participant> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val loadError: String? = null,
    val adminEmail: String = "",
    val currentSection: DashboardSection = DashboardSection.Overview,
    // Master-detail & ajout
    val selectedParticipant: Participant? = null,
    val showRegistrationForm: Boolean = false,
    val registrationForm: ParticipantFormData = ParticipantFormData(),
    // Paramètres
    val passwordForm: PasswordFormData = PasswordFormData()
) {
    val showRightPanel: Boolean
        get() = selectedParticipant != null || showRegistrationForm

    val filteredParticipants: List<Participant>
        get() = if (searchQuery.isBlank()) participants
        else participants.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                it.familyName.contains(searchQuery, ignoreCase = true)
        }

    val totalCount: Int     get() = participants.size
    val validatedCount: Int get() = participants.count { it.status == ParticipantStatus.VALIDATED }
    val pendingCount: Int   get() = participants.count { it.status == ParticipantStatus.PENDING }
    val rejectedCount: Int  get() = participants.count { it.status == ParticipantStatus.REJECTED }

    val byJobStatus: Map<JobStatus, Int>
        get() = JobStatus.entries.associateWith { s -> participants.count { it.jobStatus == s } }

    val byAgeRange: Map<String, Int>
        get() = mapOf(
            "15-20" to participants.count { it.age in 15..20 },
            "21-30" to participants.count { it.age in 21..30 },
            "31-45" to participants.count { it.age in 31..45 },
            "46-60" to participants.count { it.age in 46..60 }
        )
}
