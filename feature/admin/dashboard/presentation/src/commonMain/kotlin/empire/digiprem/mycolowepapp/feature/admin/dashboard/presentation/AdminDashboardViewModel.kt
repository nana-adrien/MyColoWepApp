package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.core.domain.util.onFailure
import empire.digiprem.mycolowepapp.core.domain.util.onSuccess
import empire.digiprem.mycolowepapp.core.ui.downloadCsvFile
import empire.digiprem.mycolowepapp.core.ui.openPrintWindow
import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.dto.ParticipantDto
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.error.ParticipantError
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.usecase.GetParticipantsUseCase
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.usecase.ObserveParticipantsUseCase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.KoinApplication.Companion.init

class AdminDashboardViewModel(
    private val getParticipants: GetParticipantsUseCase,
    private val observeParticipantsUseCase: ObserveParticipantsUseCase,
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    private val _state = MutableStateFlow(AdminDashboardState())
    val state: StateFlow<AdminDashboardState> = _state.asStateFlow()

    private val _eventChannel = Channel<AdminDashboardEvent>()
    val events = _eventChannel.receiveAsFlow()

    private var sessionLogoutSent = false

    init {
        loadAdminInfo()
        loadParticipants()
        observeSessionExpiry()
        // Dans votre ViewModel ou datasource
        viewModelScope.launch {
            supabaseClient.realtime.connect()
            // 1. Vérifier la connexion Realtime
            supabaseClient.realtime.status.collect { status ->
                println(">>> Realtime status: $status")
            }
        }

        viewModelScope.launch {
            // 2. Vérifier la session
            println(">>> User: ${supabaseClient.auth.currentUserOrNull()?.id}")
            println(">>> Role: ${supabaseClient.auth.currentUserOrNull()?.role}")
        }

        viewModelScope.launch {
            // 3. Tester un SELECT simple
            try {
                val result = supabaseClient.from("participants")
                    .select()
                    .decodeList<ParticipantDto>()
                println(">>> Participants chargés: ${result.size}")
            } catch (e: Exception) {
                println(">>> Erreur SELECT: ${e.message}")
            }

        }
    }

    fun onAction(action: AdminDashboardAction) {
        when (action) {

            // ── Navigation sections ───────────────────────────────────────────
            is AdminDashboardAction.OnSectionChange -> _state.update {
                it.copy(
                    currentSection = action.section,
                    selectedParticipant = null,
                    showRegistrationForm = false,
                    searchQuery = ""
                )
            }

            // ── Liste & recherche ─────────────────────────────────────────────
            is AdminDashboardAction.OnSearchChange -> _state.update { it.copy(searchQuery = action.query) }
            is AdminDashboardAction.OnRetryLoad -> loadParticipants()

            // ── Master-detail ─────────────────────────────────────────────────
            is AdminDashboardAction.OnParticipantSelected -> _state.update {
                it.copy(selectedParticipant = action.participant, showRegistrationForm = false)
            }

            is AdminDashboardAction.OnDismissParticipantDetail -> _state.update {
                it.copy(selectedParticipant = null)
            }

            // ── Actions participants ───────────────────────────────────────────
            is AdminDashboardAction.OnValidateParticipant -> updateParticipantStatus(
                action.id,
                ParticipantStatus.VALIDATED
            )

            is AdminDashboardAction.OnRejectParticipant -> updateParticipantStatus(
                action.id,
                ParticipantStatus.REJECTED
            )

            is AdminDashboardAction.OnDeleteParticipant -> deleteParticipant(action.id)

            // ── Formulaire d'ajout ────────────────────────────────────────────
            is AdminDashboardAction.OnOpenRegistrationForm -> _state.update {
                it.copy(
                    showRegistrationForm = true,
                    selectedParticipant = null,
                    registrationForm = ParticipantFormData()
                )
            }

            is AdminDashboardAction.OnDismissRegistrationForm -> _state.update {
                it.copy(showRegistrationForm = false, registrationForm = ParticipantFormData())
            }

            is AdminDashboardAction.OnFormFullNameChange -> _state.update {
                it.copy(registrationForm = it.registrationForm.copy(fullName = action.value, error = null))
            }

            is AdminDashboardAction.OnFormFamilyNameChange -> _state.update {
                it.copy(registrationForm = it.registrationForm.copy(familyName = action.value, error = null))
            }

            is AdminDashboardAction.OnFormBirthDateChange -> _state.update {
                it.copy(registrationForm = it.registrationForm.copy(birthDate = action.value, error = null))
            }

            is AdminDashboardAction.OnFormEducationLevelChange -> _state.update {
                it.copy(registrationForm = it.registrationForm.copy(selectedEducationLevel = action.educationLevel, error = null))
            }

            is AdminDashboardAction.OnSubmitRegistrationForm -> submitRegistrationForm()

            // ── Paramètres — mot de passe ─────────────────────────────────────
            is AdminDashboardAction.OnPasswordNewChange -> _state.update {
                it.copy(passwordForm = it.passwordForm.copy(newPassword = action.value, errorMessage = null))
            }

            is AdminDashboardAction.OnPasswordConfirmChange -> _state.update {
                it.copy(passwordForm = it.passwordForm.copy(confirmPassword = action.value, errorMessage = null))
            }

            is AdminDashboardAction.OnSubmitPasswordChange -> changePassword()
            is AdminDashboardAction.OnDismissPasswordFeedback -> _state.update {
                it.copy(passwordForm = it.passwordForm.copy(successMessage = null, errorMessage = null))
            }

            // ── Export & session ──────────────────────────────────────────────
            is AdminDashboardAction.OnExportPdf -> exportToPdf()
            is AdminDashboardAction.OnExportExcel -> exportToExcel()
            is AdminDashboardAction.OnLogoutClick -> logout()
        }
    }

    // ── Participants ──────────────────────────────────────────────────────────

    private fun updateParticipantStatus(id: String, newStatus: ParticipantStatus) {
        /* _state.update { s ->
             s.copy(
                 participants = s.participants.map { p ->
                     if (p.id == id) p.copy(status = newStatus) else p
                 },
                 selectedParticipant = s.selectedParticipant?.takeIf { it.id != id }
                     ?: s.selectedParticipant?.let { if (it.id == id) it.copy(status = newStatus) else it }
             )
         }*/
    }

    private fun deleteParticipant(id: String) {
        _state.update { s ->
            s.copy(
                participants = s.participants.filter { it.id != id },
                selectedParticipant = if (s.selectedParticipant?.id == id) null else s.selectedParticipant
            )
        }
    }

    // ── Formulaire d'ajout ────────────────────────────────────────────────────

    private fun submitRegistrationForm() {
        val form = _state.value.registrationForm
        if (!form.isValid) {
            _state.update { it.copy(registrationForm = it.registrationForm.copy(error = "Vérifiez les champs (date : AAAA-MM-JJ)")) }
            return
        }
        val birthDate = runCatching { LocalDate.parse(form.birthDate.trim()) }.getOrNull()
        if (birthDate == null) {
            _state.update { it.copy(registrationForm = it.registrationForm.copy(error = "Format de date invalide (AAAA-MM-JJ)")) }
            return
        }
        val newParticipant = Participant(
            id             = "local_${_state.value.participants.size + 1}",
            fullName       = form.fullName.trim(),
            familyName     = form.familyName.trim(),
            birthDate      = birthDate,
            educationLevel = form.selectedEducationLevel!!,
            registeredAt   = "2026-${currentMonthDay()}",
        )
        _state.update { s ->
            s.copy(
                participants         = s.participants + newParticipant,
                showRegistrationForm = false,
                selectedParticipant  = newParticipant,
                registrationForm     = ParticipantFormData()
            )
        }
    }

    // ── Changement de mot de passe ────────────────────────────────────────────

    private fun changePassword() {
        val form = _state.value.passwordForm
        if (!form.isValid) {
            _state.update { it.copy(passwordForm = it.passwordForm.copy(errorMessage = "Vérifiez les champs saisis")) }
            return
        }
        _state.update { it.copy(passwordForm = it.passwordForm.copy(isSubmitting = true, errorMessage = null)) }
        viewModelScope.launch {
            runCatching {
                supabaseClient.auth.updateUser { password = form.newPassword }
            }.onSuccess {
                _state.update {
                    it.copy(passwordForm = PasswordFormData(successMessage = "Mot de passe modifié avec succès."))
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        passwordForm = it.passwordForm.copy(
                            isSubmitting = false,
                            errorMessage = "Erreur : ${e.message}"
                        )
                    )
                }
            }
        }
    }

    // ── Export ────────────────────────────────────────────────────────────────

    private fun exportToPdf() {
        val html = ParticipantPdfBuilder.buildHtml(_state.value.filteredParticipants)
        openPrintWindow(html)
    }

    private fun exportToExcel() {
        val csv = ParticipantCsvBuilder.buildCsv(_state.value.filteredParticipants)
        downloadCsvFile(csv, "my_colo_participants.csv")
    }

    // ── Session ───────────────────────────────────────────────────────────────

    private fun observeSessionExpiry() {
        viewModelScope.launch {
            supabaseClient.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.NotAuthenticated -> sendLogoutOnce()
                    is SessionStatus.RefreshFailure -> sendLogoutOnce()
                    else -> Unit
                }
            }
        }
    }

    private fun sendLogoutOnce() {
        if (sessionLogoutSent) return
        sessionLogoutSent = true
        viewModelScope.launch { _eventChannel.send(AdminDashboardEvent.OnLogout) }
    }

    private fun loadAdminInfo() {
        viewModelScope.launch {
            val email = runCatching { supabaseClient.auth.currentUserOrNull()?.email ?: "" }.getOrDefault("")
            _state.update { it.copy(adminEmail = email) }
        }
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }
            /* getParticipants()
                 .onSuccess { list -> _state.update { it.copy(participants = list, isLoading = false) } }
                 .onFailure { error ->
                     val msg = when (error) {
                         ParticipantError.Unauthorized -> "Accès non autorisé. Reconnectez-vous."
                         ParticipantError.LoadFailed -> "Impossible de charger les participants."
                         is ParticipantError.Unknown -> "Erreur : ${error.message}"
                     }
                     _state.update { it.copy(isLoading = false, loadError = msg) }
                 }*/
            observeParticipantsUseCase()
                .onEach { list ->
                    _state.update { it.copy(participants = list, isLoading = false) }
                }.launchIn(viewModelScope)

        }
    }

    private fun logout() {
        viewModelScope.launch {
            runCatching { supabaseClient.auth.signOut() }
            _eventChannel.send(AdminDashboardEvent.OnLogout)
        }
    }
}

private fun currentMonthDay(): String =
    "05-${(1..30).random().toString().padStart(2, '0')}"
