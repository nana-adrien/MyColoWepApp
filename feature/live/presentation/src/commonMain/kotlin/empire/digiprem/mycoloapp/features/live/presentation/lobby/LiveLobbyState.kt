package empire.digiprem.mycoloapp.features.live.presentation.lobby

import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession

data class LiveLobbyState(
    val sessions: List<LiveSession> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isHost: Boolean = false,
)
