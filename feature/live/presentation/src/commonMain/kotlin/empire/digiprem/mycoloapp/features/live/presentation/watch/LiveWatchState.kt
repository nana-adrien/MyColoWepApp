package empire.digiprem.mycoloapp.features.live.presentation.watch

import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession

data class LiveWatchState(
    val session: LiveSession? = null,
    val comments: List<LiveComment> = emptyList(),
    val commentInput: String = "",
    val isLoading: Boolean = true,
    val isSendingComment: Boolean = false,
    val sessionEnded: Boolean = false,
    val connectionState: LiveConnectionState = LiveConnectionState.Idle,
    val isNotSupported: Boolean = false,
    val remoteVideoTrack: Any? = null,
    val error: String? = null,
)
