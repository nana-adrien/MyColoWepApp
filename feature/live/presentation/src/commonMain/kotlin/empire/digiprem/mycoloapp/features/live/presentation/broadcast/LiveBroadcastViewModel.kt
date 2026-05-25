package empire.digiprem.mycoloapp.features.live.presentation.broadcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveError
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import empire.digiprem.mycoloapp.features.live.domain.usecase.StartLiveUseCase
import empire.digiprem.mycoloapp.features.live.domain.usecase.StopLiveUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LiveBroadcastViewModel(
    private val startLiveUseCase: StartLiveUseCase,
    private val stopLiveUseCase: StopLiveUseCase,
    private val liveRepository: ILiveRepository,
    private val liveKitManager: LiveKitManager,
) : ViewModel() {

    private val _state = MutableStateFlow(LiveBroadcastState())
    val state = combine(
        _state,
        liveRepository.observeActiveSessions(),
        liveKitManager.observeConnectionState(),
        liveKitManager.observeParticipants(),
    ) { state, sessions, connectionState, participants ->
        val currentSession = state.session?.let { s -> sessions.find { it.id == s.id } }
        state.copy(
            session = currentSession ?: state.session,
            connectionState = connectionState,
            participants = participants,
            isNotSupported = connectionState is LiveConnectionState.Error &&
                    state.isLive.not(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LiveBroadcastState(),
    )

    private val _eventChannel = Channel<LiveBroadcastEvent>()
    val events = _eventChannel.receiveAsFlow()

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { liveKitManager.leaveRoom() }
    }

    fun onAction(action: LiveBroadcastAction) {
        when (action) {
            is LiveBroadcastAction.OnTitleChange -> _state.update { it.copy(titleInput = action.title) }
            LiveBroadcastAction.OnStartLive -> startLive()
            LiveBroadcastAction.OnStopLive -> stopLive()
            LiveBroadcastAction.OnToggleMic -> toggleMic()
            LiveBroadcastAction.OnToggleCamera -> toggleCamera()
            LiveBroadcastAction.OnNavigateBack -> viewModelScope.launch {
                liveKitManager.leaveRoom()
                _eventChannel.send(LiveBroadcastEvent.NavigateBack)
            }
        }
    }

    private fun startLive() {
        viewModelScope.launch {
            _state.update { it.copy(isStarting = true, error = null) }
            startLiveUseCase(_state.value.titleInput)
                .onSuccess { session ->
                    _state.update { it.copy(isStarting = false, isLive = true, session = session) }
                    joinLiveKitRoom(session.id, isAdmin = true)
                    _eventChannel.send(LiveBroadcastEvent.LiveStarted)
                }
                .onFailure { _state.update { s -> s.copy(isStarting = false, error = "Impossible de démarrer le live.") } }
        }
    }

    private fun joinLiveKitRoom(sessionId: String, isAdmin: Boolean) {
        viewModelScope.launch {
            liveRepository.getLiveKitToken(sessionId, isAdmin)
                .onSuccess { token ->
                    val livekitUrl = liveRepository.getLiveKitUrl()
                    liveKitManager.joinRoom(sessionId, token, livekitUrl)
                        .onSuccess {
                            liveKitManager.publishCamera()
                            liveKitManager.publishMicrophone()
                            _state.update { it.copy(localVideoTrack = liveKitManager.getLocalVideoTrack()) }
                        }
                        .onFailure { error ->
                            when (error) {
                                LiveError.NotSupported -> _state.update { it.copy(isNotSupported = true) }
                                else -> _state.update { s -> s.copy(error = "Connexion LiveKit échouée.") }
                            }
                        }
                }
                .onFailure { _state.update { s -> s.copy(error = "Impossible d'obtenir le token LiveKit.") } }
        }
    }

    private fun stopLive() {
        val sessionId = _state.value.session?.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isStopping = true, error = null) }
            liveKitManager.leaveRoom()
            stopLiveUseCase(sessionId)
                .onSuccess {
                    _state.update { it.copy(isStopping = false, isLive = false, session = null, localVideoTrack = null) }
                    _eventChannel.send(LiveBroadcastEvent.LiveStopped)
                }
                .onFailure { _state.update { s -> s.copy(isStopping = false, error = "Impossible d'arrêter le live.") } }
        }
    }

    private fun toggleMic() {
        viewModelScope.launch {
            liveKitManager.toggleMicrophone()
            _state.update { it.copy(isMicEnabled = !it.isMicEnabled) }
        }
    }

    private fun toggleCamera() {
        viewModelScope.launch {
            liveKitManager.toggleCamera()
            _state.update { it.copy(isCameraEnabled = !it.isCameraEnabled) }
        }
    }
}
