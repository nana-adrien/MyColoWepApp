package empire.digiprem.mycoloapp.features.live.data.service

import android.content.Context
import empire.digiprem.mycoloapp.core.domain.error.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveError
import empire.digiprem.mycoloapp.features.live.domain.model.LiveParticipant
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import io.livekit.android.LiveKit
import io.livekit.android.room.Room
import io.livekit.android.room.participant.LocalParticipant
import io.livekit.android.room.participant.RemoteParticipant
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.RemoteVideoTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AndroidLiveKitManager(private val context: Context) : LiveKitManager {

    private var room: Room? = null
    private val _connectionState = MutableStateFlow<LiveConnectionState>(LiveConnectionState.Idle)
    private val _participants = MutableStateFlow<List<LiveParticipant>>(emptyList())
    private val scope = CoroutineScope(Dispatchers.Main)

    override suspend fun joinRoom(roomName: String, token: String, livekitUrl: String): Result<Unit, LiveError> {
        return try {
            _connectionState.value = LiveConnectionState.Connecting
            val r = LiveKit.create(appContext = context)
            room = r
            r.connect(livekitUrl, token)
            _connectionState.value = LiveConnectionState.Connected
            observeRoomParticipants(r)
            Result.Success(Unit)
        } catch (e: Exception) {
            _connectionState.value = LiveConnectionState.Error(e.message ?: "Connection failed")
            Result.Failure(LiveError.ConnectionFailed(e.message))
        }
    }

    private fun observeRoomParticipants(r: Room) {
        scope.launch {
            r.remoteParticipants.collect { participants ->
                _participants.value = participants.values.map { p ->
                    LiveParticipant(
                        id = p.sid.value,
                        name = p.name ?: p.identity.value,
                        isHost = false,
                        isSpeaking = p.isSpeaking,
                        isMicEnabled = !p.audioTrackPublications.any { it.value.muted },
                        isCameraEnabled = !p.videoTrackPublications.any { it.value.muted },
                    )
                }
            }
        }
    }

    override suspend fun leaveRoom() {
        room?.disconnect()
        room = null
        _connectionState.value = LiveConnectionState.Disconnected
        _participants.value = emptyList()
    }

    override suspend fun publishCamera(): Result<Unit, LiveError> {
        return try {
            room?.localParticipant?.setCameraEnabled(true)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(LiveError.ConnectionFailed(e.message))
        }
    }

    override suspend fun publishMicrophone(): Result<Unit, LiveError> {
        return try {
            room?.localParticipant?.setMicrophoneEnabled(true)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(LiveError.ConnectionFailed(e.message))
        }
    }

    override suspend fun toggleCamera(): Result<Unit, LiveError> {
        val local = room?.localParticipant ?: return Result.Failure(LiveError.ConnectionFailed("No room"))
        return try {
            val currentEnabled = local.videoTrackPublications.any { !it.value.muted }
            local.setCameraEnabled(!currentEnabled)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(LiveError.ConnectionFailed(e.message))
        }
    }

    override suspend fun toggleMicrophone(): Result<Unit, LiveError> {
        val local = room?.localParticipant ?: return Result.Failure(LiveError.ConnectionFailed("No room"))
        return try {
            val currentEnabled = local.audioTrackPublications.any { !it.value.muted }
            local.setMicrophoneEnabled(!currentEnabled)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(LiveError.ConnectionFailed(e.message))
        }
    }

    override fun observeParticipants(): Flow<List<LiveParticipant>> = _participants.asStateFlow()

    override fun observeConnectionState(): Flow<LiveConnectionState> = _connectionState.asStateFlow()

    override fun getLocalVideoTrack(): Any? =
        room?.localParticipant?.videoTrackPublications?.values?.firstOrNull()?.track

    override fun getRemoteVideoTrack(participantId: String): Any? =
        room?.remoteParticipants?.value?.get(io.livekit.android.util.toParticipantSid(participantId))
            ?.videoTrackPublications?.values?.firstOrNull()?.track
}
