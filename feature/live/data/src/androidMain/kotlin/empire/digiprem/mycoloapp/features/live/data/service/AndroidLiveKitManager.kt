package empire.digiprem.mycoloapp.features.live.data.service

import android.content.Context
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveConnectionState
import empire.digiprem.mycoloapp.features.live.domain.model.LiveError
import empire.digiprem.mycoloapp.features.live.domain.model.LiveParticipant
import empire.digiprem.mycoloapp.features.live.domain.service.LiveKitManager
import io.livekit.android.LiveKit
import io.livekit.android.events.RoomEvent
import io.livekit.android.events.collect
import io.livekit.android.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            observeRoom(r)
            Result.Success(Unit)
        } catch (e: Exception) {
            _connectionState.value = LiveConnectionState.Error(e.message ?: "Connection failed")
            Result.Failure(LiveError.ConnectionFailed(e.message))
        }
    }

    private fun observeRoom(r: Room) {
        scope.launch {
            r.remoteParticipants.collect { map ->
                _participants.value = map.values.map { p ->
                    LiveParticipant(
                        id = p.sid.value,
                        name = p.name ?: p.identity.value,
                        isHost = false,
                        isSpeaking = p.isSpeaking,
                        isMicEnabled = p.audioTrackPublications.values.any { !it.muted },
                        isCameraEnabled = p.videoTrackPublications.values.any { !it.muted },
                    )
                }
            }
        }
        scope.launch {
            r.events.collect { event ->
                when (event) {
                    is RoomEvent.Disconnected -> {
                        _connectionState.value = LiveConnectionState.Disconnected
                        _participants.value = emptyList()
                    }
                    else -> {}
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
            val enabled = local.videoTrackPublications.values.any { !it.muted }
            local.setCameraEnabled(!enabled)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(LiveError.ConnectionFailed(e.message))
        }
    }

    override suspend fun toggleMicrophone(): Result<Unit, LiveError> {
        val local = room?.localParticipant ?: return Result.Failure(LiveError.ConnectionFailed("No room"))
        return try {
            val enabled = local.audioTrackPublications.values.any { !it.muted }
            local.setMicrophoneEnabled(!enabled)
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
        room?.remoteParticipants?.value
            ?.values
            ?.find { it.sid.value == participantId || it.identity.value == participantId }
            ?.videoTrackPublications?.values?.firstOrNull()?.track
}
