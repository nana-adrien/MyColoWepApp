package empire.digiprem.mycoloapp.features.live.data.service

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveError
import empire.digiprem.mycoloapp.features.live.domain.model.LiveKitWebData
import empire.digiprem.mycoloapp.features.live.domain.model.LiveParticipant
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IosLiveKitManager : LiveKitManager {

    private val _connectionState = MutableStateFlow<LiveConnectionState>(LiveConnectionState.Idle)
    private val _participants = MutableStateFlow<List<LiveParticipant>>(emptyList())
    private var webData: LiveKitWebData? = null

    override suspend fun joinRoom(roomName: String, token: String, livekitUrl: String): Result<Unit, LiveError> {
        webData = LiveKitWebData(livekitUrl, token, roomName, isPublishing = false)
        _connectionState.value = LiveConnectionState.Connected
        return Result.Success(Unit)
    }

    override suspend fun leaveRoom() {
        webData = null
        _connectionState.value = LiveConnectionState.Disconnected
        _participants.value = emptyList()
    }

    override suspend fun publishCamera(): Result<Unit, LiveError> {
        webData = webData?.copy(isPublishing = true)
        return Result.Success(Unit)
    }

    override suspend fun publishMicrophone(): Result<Unit, LiveError> = Result.Success(Unit)

    override suspend fun toggleCamera(): Result<Unit, LiveError> {
        webData = webData?.copy(isPublishing = !(webData?.isPublishing ?: false))
        return Result.Success(Unit)
    }

    override suspend fun toggleMicrophone(): Result<Unit, LiveError> = Result.Success(Unit)

    override fun observeParticipants(): Flow<List<LiveParticipant>> = _participants.asStateFlow()

    override fun observeConnectionState(): Flow<LiveConnectionState> = _connectionState.asStateFlow()

    override fun getLocalVideoTrack(): Any? = webData?.copy(isPublishing = true)

    override fun getRemoteVideoTrack(participantId: String): Any? = webData?.copy(isPublishing = false)
}
