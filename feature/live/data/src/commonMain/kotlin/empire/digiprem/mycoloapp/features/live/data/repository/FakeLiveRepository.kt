package empire.digiprem.mycoloapp.features.live.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class FakeLiveRepository : ILiveRepository {

    private val _sessions = MutableStateFlow<List<LiveSession>>(
        listOf(
            LiveSession(
                id = "live_001",
                hostId = "host_001",
                hostName = "Marie Dupont",
                hostAvatarUrl = null,
                title = "Séance de coiffure en direct !",
                isActive = true,
                viewerCount = 42,
                startedAt = kotlin.time.Clock.System.now(),
            ),
        )
    )

    private val _comments = MutableStateFlow<Map<String, List<LiveComment>>>(emptyMap())

    override fun observeActiveSessions(): Flow<List<LiveSession>> =
        _sessions.map { it.filter { s -> s.isActive } }

    override fun observeComments(sessionId: String): Flow<List<LiveComment>> =
        _comments.map { it[sessionId] ?: emptyList() }

    override suspend fun fetchActiveSessions(): Result<List<LiveSession>, DataError.Remote> =
        Result.Success(_sessions.value.filter { it.isActive })

    override suspend fun startLive(title: String): Result<LiveSession, DataError.Remote> {
        val session = LiveSession(
            id = "live_${Clock.System.now().toEpochMilliseconds()}",
            hostId = "user_me",
            hostName = "Moi",
            hostAvatarUrl = null,
            title = title,
            isActive = true,
            viewerCount = 0,
            startedAt = Clock.System.now(),
        )
        _sessions.value = _sessions.value + session
        return Result.Success(session)
    }

    override suspend fun stopLive(sessionId: String): Result<Unit, DataError.Remote> {
        _sessions.value = _sessions.value.map { s ->
            if (s.id == sessionId) s.copy(isActive = false) else s
        }
        return Result.Success(Unit)
    }

    override suspend fun joinLive(sessionId: String): Result<Unit, DataError.Remote> {
        _sessions.value = _sessions.value.map { s ->
            if (s.id == sessionId) s.copy(viewerCount = s.viewerCount + 1) else s
        }
        return Result.Success(Unit)
    }

    override suspend fun getLiveKitToken(sessionId: String, isAdmin: Boolean): Result<String, DataError.Remote> =
        Result.Success("fake_token_${sessionId}_${if (isAdmin) "admin" else "viewer"}")

    override suspend fun getLiveKitUrl(): String = "wss://fake-livekit.example.com"

    override suspend fun sendComment(sessionId: String, content: String): Result<LiveComment, DataError.Remote> {
        val comment = LiveComment(
            id = "cmt_${Clock.System.now().toEpochMilliseconds()}",
            sessionId = sessionId,
            authorId = "user_me",
            authorName = "Moi",
            authorAvatarUrl = null,
            content = content,
            sentAt = Clock.System.now(),
        )
        val current = _comments.value.toMutableMap()
        current[sessionId] = (current[sessionId] ?: emptyList()) + comment
        _comments.value = current
        return Result.Success(comment)
    }
}
