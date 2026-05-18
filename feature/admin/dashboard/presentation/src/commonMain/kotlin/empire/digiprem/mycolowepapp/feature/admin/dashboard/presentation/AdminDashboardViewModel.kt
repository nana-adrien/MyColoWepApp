package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminDashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        AdminDashboardState(participants = mockParticipants())
    )
    val state: StateFlow<AdminDashboardState> = _state.asStateFlow()

    private val _eventChannel = Channel<AdminDashboardEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: AdminDashboardAction) {
        when (action) {
            is AdminDashboardAction.OnSearchChange -> _state.update {
                it.copy(searchQuery = action.query)
            }
            is AdminDashboardAction.OnExportPdf -> Unit
            is AdminDashboardAction.OnExportExcel -> Unit
            is AdminDashboardAction.OnLogoutClick -> viewModelScope.launch {
                _eventChannel.send(AdminDashboardEvent.OnLogout)
            }
        }
    }

    private fun mockParticipants(): List<Participant> = listOf(
        Participant("1", "Amadou Diallo", 22, JobStatus.STUDENT_HIGHER, "Diallo", "2026-05-01", ParticipantStatus.VALIDATED),
        Participant("2", "Fatoumata Koné", 17, JobStatus.STUDENT_SCHOOL, "Koné", "2026-05-03", ParticipantStatus.VALIDATED),
        Participant("3", "Ibrahim Traoré", 35, JobStatus.WORKER, "Traoré", "2026-05-05", ParticipantStatus.PENDING),
        Participant("4", "Mariama Bah", 28, JobStatus.SEEKING_WORK, "Bah", "2026-05-06", ParticipantStatus.PENDING),
        Participant("5", "Oumar Camara", 19, JobStatus.STUDENT_HIGHER, "Camara", "2026-05-08", ParticipantStatus.VALIDATED),
        Participant("6", "Aminata Sow", 42, JobStatus.WORKER, "Sow", "2026-05-10", ParticipantStatus.REJECTED),
        Participant("7", "Mamadou Barry", 16, JobStatus.STUDENT_SCHOOL, "Barry", "2026-05-12", ParticipantStatus.PENDING),
        Participant("8", "Kadiatou Sylla", 31, JobStatus.SEEKING_WORK, "Sylla", "2026-05-14", ParticipantStatus.VALIDATED)
    )
}
