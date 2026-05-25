package empire.digiprem.mycoloapp.features.participants.presentation

import empire.digiprem.mycoloapp.features.participants.domain.model.Participant
import empire.digiprem.mycoloapp.features.registration.domain.model.EducationLevel

// ── Section active du dashboard ──────────────────────────────────────────────

enum class DashboardSection { Overview, Participants, SecurityCodes, Settings }

// ── Formulaire d'ajout de participant ────────────────────────────────────────

data class ParticipantFormData(
    val fullName: String = "",
    val familyName: String = "",
    val birthDate: String = "",
    val selectedEducationLevel: EducationLevel? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null,
) {
    val isValid: Boolean
        get() = fullName.isNotBlank()
            && familyName.isNotBlank()
            && birthDate.matches(Regex("""\d{4}-\d{2}-\d{2}"""))
            && selectedEducationLevel != null
}

// ── Formulaire de changement de mot de passe ─────────────────────────────────

data class PasswordFormData(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isSubmitting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
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
    val selectedParticipant: Participant? = null,
    val showRegistrationForm: Boolean = false,
    val registrationForm: ParticipantFormData = ParticipantFormData(),
    val passwordForm: PasswordFormData = PasswordFormData(),
) {
    val showRightPanel: Boolean
        get() = selectedParticipant != null || showRegistrationForm

    val filteredParticipants: List<Participant>
        get() = if (searchQuery.isBlank()) participants
        else participants.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
            it.familyName.contains(searchQuery, ignoreCase = true) ||
            it.educationLevel.name.contains(searchQuery, ignoreCase = true)
        }

    val totalCount: Int get() = participants.size

    val byEducationLevel: Map<EducationLevel, Int>
        get() = EducationLevel.entries.associateWith { level -> participants.count { it.educationLevel == level } }

    val byAgeRange: Map<String, Int>
        get() = mapOf(
            "0-14"  to participants.count { it.age in 0..14 },
            "15-20" to participants.count { it.age in 15..20 },
            "21-30" to participants.count { it.age in 21..30 },
            "31+"   to participants.count { it.age >= 31 },
        )
}
