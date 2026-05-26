package empire.digiprem.mycoloapp.features.feed.data.dto

import empire.digiprem.mycoloapp.features.feed.domain.model.Comment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class CommentDto(
    val id: String = "",
    @SerialName("post_id") val postId: String = "",
    @SerialName("user_id") val authorId: String = "",
    @SerialName("full_name") val authorName: String = "",
    @SerialName("avatar_url") val authorAvatarUrl: String? = null,
    val content: String = "",
    @SerialName("created_at") val createdAt: Instant = Instant.DISTANT_PAST,
)

fun CommentDto.toDomain(): Comment = Comment(
    id = id,
    postId = postId,
    authorId = authorId,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    content = content,
    createdAt = createdAt,
)
