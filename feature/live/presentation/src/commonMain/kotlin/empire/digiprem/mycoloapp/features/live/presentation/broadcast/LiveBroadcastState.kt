package empire.digiprem.mycoloapp.features.live.presentation.broadcast

import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveParticipant
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession

data class LiveBroadcastState(
    val session: LiveSession? = null,
    val comments: List<LiveComment> = emptyList(),
    val participants: List<LiveParticipant> = emptyList(),
    val connectionState: LiveConnectionState = LiveConnectionState.Idle,
    val titleInput: String = "",
    val isStarting: Boolean = false,
    val isStopping: Boolean = false,
    val isLive: Boolean = false,
    val isMicEnabled: Boolean = true,
    val isCameraEnabled: Boolean = true,
    val isNotSupported: Boolean = false,
    val localVideoTrack: Any? = null,
    val error: String? = null,
)
