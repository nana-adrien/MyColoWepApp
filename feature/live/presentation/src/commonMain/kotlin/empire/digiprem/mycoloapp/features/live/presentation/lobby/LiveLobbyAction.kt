package empire.digiprem.mycoloapp.features.live.presentation.lobby

sealed interface LiveLobbyAction {
    data object OnRefresh : LiveLobbyAction
    data class OnJoinSession(val sessionId: String) : LiveLobbyAction
    data object OnStartLiveClick : LiveLobbyAction
}
