package empire.digiprem.mycoloapp.features.live.domain.service

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveError
import empire.digiprem.mycoloapp.features.live.domain.model.LiveParticipant
import kotlinx.coroutines.flow.Flow

interface LiveKitManager {
    suspend fun joinRoom(roomName: String, token: String, livekitUrl: String): Result<Unit, LiveError>
    suspend fun leaveRoom()
    suspend fun publishCamera(): Result<Unit, LiveError>
    suspend fun publishMicrophone(): Result<Unit, LiveError>
    suspend fun toggleCamera(): Result<Unit, LiveError>
    suspend fun toggleMicrophone(): Result<Unit, LiveError>
    fun observeParticipants(): Flow<List<LiveParticipant>>
    fun observeConnectionState(): Flow<LiveConnectionState>
    fun getLocalVideoTrack(): Any?
    fun getRemoteVideoTrack(participantId: String): Any?
}
