package empire.digiprem.mycoloapp.features.live.domain.model

import kotlinx.datetime.Instant

data class LiveComment(
    val id: String,
    val sessionId: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarUrl: String?,
    val content: String,
    val sentAt: Instant,
)
