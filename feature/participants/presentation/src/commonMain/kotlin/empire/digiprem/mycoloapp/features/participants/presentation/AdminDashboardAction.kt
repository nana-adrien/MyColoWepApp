package empire.digiprem.mycoloapp.features.participants.presentation

import empire.digiprem.mycoloapp.features.participants.domain.model.Participant
import empire.digiprem.mycoloapp.features.registration.domain.model.EducationLevel

sealed interface AdminDashboardAction {

    // ── Navigation sections ───────────────────────────────────────────────────
    data class OnSectionChange(val section: DashboardSection) : AdminDashboardAction

    // ── Liste & recherche ─────────────────────────────────────────────────────
    data class OnSearchChange(val query: String) : AdminDashboardAction
    data object OnRetryLoad : AdminDashboardAction

    // ── Master-detail ─────────────────────────────────────────────────────────
    data class OnParticipantSelected(val participant: Participant) : AdminDashboardAction
    data object OnDismissParticipantDetail : AdminDashboardAction

    // ── Actions sur participants ───────────────────────────────────────────────
    data class OnValidateParticipant(val id: String) : AdminDashboardAction
    data class OnRejectParticipant(val id: String) : AdminDashboardAction
    data class OnDeleteParticipant(val id: String) : AdminDashboardAction

    // ── Formulaire d'ajout ────────────────────────────────────────────────────
    data object OnOpenRegistrationForm : AdminDashboardAction
    data object OnDismissRegistrationForm : AdminDashboardAction
    data class OnFormFullNameChange(val value: String) : AdminDashboardAction
    data class OnFormFamilyNameChange(val value: String) : AdminDashboardAction
    data class OnFormBirthDateChange(val value: String) : AdminDashboardAction
    data class OnFormEducationLevelChange(val educationLevel: EducationLevel) : AdminDashboardAction
    data object OnSubmitRegistrationForm : AdminDashboardAction

    // ── Paramètres — changement de mot de passe ───────────────────────────────
    data class OnPasswordNewChange(val value: String) : AdminDashboardAction
    data class OnPasswordConfirmChange(val value: String) : AdminDashboardAction
    data object OnSubmitPasswordChange : AdminDashboardAction
    data object OnDismissPasswordFeedback : AdminDashboardAction

    // ── Export & session ──────────────────────────────────────────────────────
    data object OnExportPdf : AdminDashboardAction
    data object OnExportExcel : AdminDashboardAction
    data object OnLogoutClick : AdminDashboardAction
}
