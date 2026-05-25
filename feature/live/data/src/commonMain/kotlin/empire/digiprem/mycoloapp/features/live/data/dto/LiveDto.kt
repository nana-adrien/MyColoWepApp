package empire.digiprem.mycoloapp.features.live.data.dto

import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveDto(
    val id: String = "",
    @SerialName("room_name") val roomName: String = "",
    val title: String = "",
    @SerialName("started_by") val startedBy: String = "",
    @SerialName("started_at") val startedAt: Instant = Instant.DISTANT_PAST,
    @SerialName("is_active") val isActive: Boolean = false,
    @SerialName("viewer_count") val viewerCount: Int = 0,
)

@Serializable
data class StartLiveRequest(
    @SerialName("room_name") val roomName: String,
    @SerialName("started_by") val startedBy: String,
    val title: String,
)

@Serializable
data class LiveKitTokenRequest(
    @SerialName("room_name") val roomName: String,
    @SerialName("participant_name") val participantName: String,
    @SerialName("is_admin") val isAdmin: Boolean,
)

@Serializable
data class LiveKitTokenResponse(
    val token: String,
)

fun LiveDto.toDomain(hostName: String = "Inconnu"): LiveSession = LiveSession(
    id = id,
    hostId = startedBy,
    hostName = hostName,
    hostAvatarUrl = null,
    title = title,
    isActive = isActive,
    viewerCount = viewerCount,
    startedAt = startedAt,
)
