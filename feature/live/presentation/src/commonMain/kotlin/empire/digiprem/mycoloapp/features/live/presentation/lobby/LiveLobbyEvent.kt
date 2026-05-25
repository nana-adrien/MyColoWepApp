package empire.digiprem.mycoloapp.features.live.presentation.lobby

sealed interface LiveLobbyEvent {
    data class NavigateToWatch(val sessionId: String) : LiveLobbyEvent
    data object NavigateToBroadcast : LiveLobbyEvent
}
