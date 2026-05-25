package empire.digiprem.mycoloapp.features.live.presentation.broadcast

sealed interface LiveBroadcastEvent {
    data object NavigateBack : LiveBroadcastEvent
    data object LiveStarted : LiveBroadcastEvent
    data object LiveStopped : LiveBroadcastEvent
}
