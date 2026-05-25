package empire.digiprem.mycoloapp.features.live.domain.model

import empire.digiprem.mycoloapp.core.domain.util.ResultError

sealed interface LiveError : ResultError {
    data object NotSupported : LiveError
    data object PermissionDenied : LiveError
    data object RoomNotFound : LiveError
    data class ConnectionFailed(val message: String?) : LiveError
    data class Unknown(val message: String?) : LiveError
}
