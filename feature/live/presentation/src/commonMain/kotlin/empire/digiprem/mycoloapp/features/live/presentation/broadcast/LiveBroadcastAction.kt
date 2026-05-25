package empire.digiprem.mycoloapp.features.live.presentation.broadcast

sealed interface LiveBroadcastAction {
    data class OnTitleChange(val title: String) : LiveBroadcastAction
    data object OnStartLive : LiveBroadcastAction
    data object OnStopLive : LiveBroadcastAction
    data object OnToggleMic : LiveBroadcastAction
    data object OnToggleCamera : LiveBroadcastAction
    data object OnNavigateBack : LiveBroadcastAction
}
