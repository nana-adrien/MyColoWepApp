package empire.digiprem.mycoloapp.features.live.domain.model

sealed interface LiveConnectionState {
    data object Idle : LiveConnectionState
    data object Connecting : LiveConnectionState
    data object Connected : LiveConnectionState
    data object Disconnected : LiveConnectionState
    data class Error(val message: String) : LiveConnectionState
}
