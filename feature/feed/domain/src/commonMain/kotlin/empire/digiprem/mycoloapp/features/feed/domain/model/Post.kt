package empire.digiprem.mycoloapp.features.feed.domain.model

import kotlinx.datetime.Instant

data class Post(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarUrl: String?,
    val caption: String,
    val mediaUrl: String?,
    val mediaType: MediaType?,
    val likesCount: Int,
    val commentsCount: Int,
    val isLikedByMe: Boolean,
    val createdAt: Instant,
)

enum class MediaType { IMAGE, VIDEO }
