package empire.digiprem.mycoloapp.features.live.data.service

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveError
import empire.digiprem.mycoloapp.features.live.domain.model.LiveParticipant
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StubLiveKitManager : LiveKitManager {

    private val _connectionState = MutableStateFlow<LiveConnectionState>(LiveConnectionState.Idle)
    private val _participants = MutableStateFlow<List<LiveParticipant>>(emptyList())

    override suspend fun joinRoom(roomName: String, token: String, livekitUrl: String): Result<Unit, LiveError> =
        Result.Failure(LiveError.NotSupported)

    override suspend fun leaveRoom() {}

    override suspend fun publishCamera(): Result<Unit, LiveError> =
        Result.Failure(LiveError.NotSupported)

    override suspend fun publishMicrophone(): Result<Unit, LiveError> =
        Result.Failure(LiveError.NotSupported)

    override suspend fun toggleCamera(): Result<Unit, LiveError> =
        Result.Failure(LiveError.NotSupported)

    override suspend fun toggleMicrophone(): Result<Unit, LiveError> =
        Result.Failure(LiveError.NotSupported)

    override fun observeParticipants(): Flow<List<LiveParticipant>> = _participants.asStateFlow()

    override fun observeConnectionState(): Flow<LiveConnectionState> = _connectionState.asStateFlow()

    override fun getLocalVideoTrack(): Any? = null

    override fun getRemoteVideoTrack(participantId: String): Any? = null
}
