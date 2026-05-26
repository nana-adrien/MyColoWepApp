package empire.digiprem.mycoloapp.features.feed.data.dto

import empire.digiprem.mycoloapp.features.feed.domain.model.MediaType
import empire.digiprem.mycoloapp.features.feed.domain.model.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class PostDto(
    val id: String = "",
    @SerialName("user_id") val userId: String = "",
    val caption: String = "",
    @SerialName("media_url") val mediaUrl: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("likes_count") val likesCount: Int = 0,
    @SerialName("comments_count") val commentsCount: Int = 0,
    @SerialName("created_at") val createdAt: String = "",
    // Joined from users table
    @SerialName("full_name") val fullName: String = "",
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

fun PostDto.toDomain(currentUserId: String = ""): Post = Post(
    id = id,
    authorId = userId,
    authorName = fullName,
    authorAvatarUrl = avatarUrl,
    caption = caption,
    mediaUrl = mediaUrl,
    mediaType = when (mediaType?.lowercase()) {
        "image" -> MediaType.IMAGE
        "video" -> MediaType.VIDEO
        else -> null
    },
    likesCount = likesCount,
    commentsCount = commentsCount,
    isLikedByMe = false,
    createdAt = Instant.parse(createdAt),
)
