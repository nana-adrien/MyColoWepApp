package empire.digiprem.mycoloapp.features.live.data.dto

import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveCommentDto(
    val id: String = "",
    @SerialName("live_id") val liveId: String = "",
    @SerialName("user_id") val userId: String = "",
    @SerialName("user_name") val userName: String = "",
    val message: String = "",
    @SerialName("created_at") val createdAt: Instant = Instant.DISTANT_PAST,
)

fun LiveCommentDto.toDomain(): LiveComment = LiveComment(
    id = id,
    sessionId = liveId,
    authorId = userId,
    authorName = userName,
    authorAvatarUrl = null,
    content = message,
    sentAt = createdAt,
)
