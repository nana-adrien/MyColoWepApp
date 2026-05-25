package empire.digiprem.mycoloapp.features.live.presentation.watch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveError
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import empire.digiprem.mycoloapp.features.live.domain.usecase.SendLiveCommentUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LiveWatchViewModel(
    private val sessionId: String,
    private val sendCommentUseCase: SendLiveCommentUseCase,
    private val liveRepository: ILiveRepository,
    private val liveKitManager: LiveKitManager,
) : ViewModel() {

    private val _state = MutableStateFlow(LiveWatchState())
    val state = combine(
        _state,
        liveRepository.observeComments(sessionId),
        liveRepository.observeActiveSessions(),
        liveKitManager.observeConnectionState(),
        liveKitManager.observeParticipants(),
    ) { state, comments, sessions, connectionState, participants ->
        val session = sessions.find { it.id == sessionId }
        val hostParticipant = participants.find { it.isHost }
        state.copy(
            comments = comments,
            session = session,
            sessionEnded = session?.isActive == false,
            isLoading = false,
            connectionState = connectionState,
            remoteVideoTrack = hostParticipant?.let { p -> liveKitManager.getRemoteVideoTrack(p.id) },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LiveWatchState(),
    )

    private val _eventChannel = Channel<LiveWatchEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            liveRepository.fetchActiveSessions()
            joinRoom()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { liveKitManager.leaveRoom() }
    }

    fun onAction(action: LiveWatchAction) {
        when (action) {
            is LiveWatchAction.OnCommentInputChange -> _state.update { it.copy(commentInput = action.text) }
            LiveWatchAction.OnSendComment -> sendComment()
            LiveWatchAction.OnNavigateBack -> viewModelScope.launch {
                liveKitManager.leaveRoom()
                _eventChannel.send(LiveWatchEvent.NavigateBack)
            }
        }
    }

    private fun joinRoom() {
        viewModelScope.launch {
            liveRepository.getLiveKitToken(sessionId, isAdmin = false)
                .onSuccess { token ->
                    val livekitUrl = liveRepository.getLiveKitUrl()
                    liveKitManager.joinRoom(sessionId, token, livekitUrl)
                        .onFailure { error ->
                            when (error) {
                                LiveError.NotSupported -> _state.update { it.copy(isNotSupported = true) }
                                else -> _state.update { s -> s.copy(error = "Connexion au live échouée.") }
                            }
                        }
                }
                .onFailure { _state.update { s -> s.copy(error = "Impossible d'obtenir le token.") } }
        }
    }

    private fun sendComment() {
        val content = _state.value.commentInput.trim()
        if (content.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isSendingComment = true, commentInput = "") }
            sendCommentUseCase(sessionId, content)
                .onFailure { _state.update { s -> s.copy(error = "Envoi échoué.") } }
            _state.update { it.copy(isSendingComment = false) }
        }
    }
}
