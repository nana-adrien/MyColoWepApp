package empire.digiprem.mycoloapp.features.live.domain.model

import kotlinx.datetime.Instant

data class LiveSession(
    val id: String,
    val hostId: String,
    val hostName: String,
    val hostAvatarUrl: String?,
    val title: String,
    val isActive: Boolean,
    val viewerCount: Int,
    val startedAt: Instant,
)
