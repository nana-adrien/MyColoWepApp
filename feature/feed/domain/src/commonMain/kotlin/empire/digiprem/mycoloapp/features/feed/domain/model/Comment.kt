package empire.digiprem.mycoloapp.features.feed.domain.model

import kotlin.time.Instant


data class Comment(
    val id: String,
    val postId: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarUrl: String?,
    val content: String,
    val createdAt: Instant,
)
