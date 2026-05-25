package empire.digiprem.mycoloapp.features.live.presentation.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository
import empire.digiprem.mycoloapp.features.live.domain.usecase.GetActiveSessionsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LiveLobbyViewModel(
    private val getActiveSessionsUseCase: GetActiveSessionsUseCase,
    private val liveRepository: ILiveRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LiveLobbyState())
    val state = combine(_state, getActiveSessionsUseCase()) { state, sessions ->
        state.copy(sessions = sessions, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LiveLobbyState(),
    )

    private val _eventChannel = Channel<LiveLobbyEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadSessions()
    }

    fun onAction(action: LiveLobbyAction) {
        when (action) {
            LiveLobbyAction.OnRefresh -> loadSessions()
            is LiveLobbyAction.OnJoinSession -> joinSession(action.sessionId)
            LiveLobbyAction.OnStartLiveClick -> viewModelScope.launch {
                _eventChannel.send(LiveLobbyEvent.NavigateToBroadcast)
            }
        }
    }

    private fun loadSessions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            liveRepository.fetchActiveSessions()
                .onFailure { _state.update { s -> s.copy(isLoading = false, error = "Impossible de charger les lives.") } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun joinSession(sessionId: String) {
        viewModelScope.launch {
            liveRepository.joinLive(sessionId)
                .onFailure { _state.update { s -> s.copy(error = "Impossible de rejoindre le live.") } }
            _eventChannel.send(LiveLobbyEvent.NavigateToWatch(sessionId))
        }
    }
}
