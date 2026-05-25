package empire.digiprem.mycoloapp.features.live.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.map
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.features.live.data.datasource.LiveRemoteDataSource
import empire.digiprem.mycoloapp.features.live.data.dto.LiveCommentDto
import empire.digiprem.mycoloapp.features.live.data.dto.LiveDto
import empire.digiprem.mycoloapp.features.live.data.dto.toDomain
import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LiveRepositoryImpl(
    private val remoteDataSource: LiveRemoteDataSource,
    private val supabaseClient: SupabaseClient,
) : ILiveRepository {

    private val _sessions = MutableStateFlow<List<LiveSession>>(emptyList())
    private val _comments = MutableStateFlow<Map<String, List<LiveComment>>>(emptyMap())
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        subscribeToLiveUpdates()
    }

    private fun subscribeToLiveUpdates() {
        scope.launch {
            try {
                val channel = supabaseClient.realtime.channel("live-status")
                channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "lives"
                }.collect { action ->
                    when (action) {
                        is PostgresAction.Insert -> {
                            val live = action.decodeRecord<LiveDto>()
                            _sessions.value = (_sessions.value + live.toDomain()).distinctBy { it.id }
                        }
                        is PostgresAction.Update -> {
                            val live = action.decodeRecord<LiveDto>()
                            _sessions.value = _sessions.value.map { if (it.id == live.id) live.toDomain() else it }
                        }
                        is PostgresAction.Delete -> {
                            val id = action.oldRecord["id"]?.toString()?.trim('"') ?: return@collect
                            _sessions.value = _sessions.value.filter { it.id != id }
                        }
                        else -> {}
                    }
                }
                channel.subscribe()
            } catch (e: Exception) {
                // Realtime unavailable — rely on manual fetches
            }
        }
    }

    fun subscribeToComments(sessionId: String) {
        scope.launch {
            try {
                val channel = supabaseClient.realtime.channel("live-comments-$sessionId")
                channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                    table = "live_comments"
                    //filter = "live_id=eq.$sessionId"
                }.collect { action ->
                    val comment = action.decodeRecord<LiveCommentDto>().toDomain()
                    val current = _comments.value.toMutableMap()
                    current[sessionId] = (current[sessionId] ?: emptyList()) + comment
                    _comments.value = current
                }
                channel.subscribe()
            } catch (e: Exception) {
                // Realtime unavailable
            }
        }
    }

    override fun observeActiveSessions(): Flow<List<LiveSession>> =
        _sessions.map { it.filter { s -> s.isActive } }

    override fun observeComments(sessionId: String): Flow<List<LiveComment>> {
        subscribeToComments(sessionId)
        return _comments.map { it[sessionId] ?: emptyList() }
    }

    override suspend fun fetchActiveSessions(): Result<List<LiveSession>, DataError.Remote> {
        return remoteDataSource.fetchActiveLives().also { result ->
            result.onSuccess { dtos ->
                _sessions.value = dtos.map { it.toDomain() }
            }
        }.map { dtos -> dtos.map { it.toDomain() } }
    }

    override suspend fun startLive(title: String): Result<LiveSession, DataError.Remote> =
        remoteDataSource.startLive(title).map { it.toDomain() }

    override suspend fun stopLive(sessionId: String): Result<Unit, DataError.Remote> {
        val result = remoteDataSource.stopLive(sessionId)
        result.onSuccess {
            _sessions.value = _sessions.value.map { s ->
                if (s.id == sessionId) s.copy(isActive = false) else s
            }
        }
        return result
    }

    override suspend fun joinLive(sessionId: String): Result<Unit, DataError.Remote> {
        val result = remoteDataSource.joinLive(sessionId)
        result.onSuccess {
            _sessions.value = _sessions.value.map { s ->
                if (s.id == sessionId) s.copy(viewerCount = s.viewerCount + 1) else s
            }
        }
        return result
    }

    override suspend fun sendComment(sessionId: String, content: String): Result<LiveComment, DataError.Remote> =
        remoteDataSource.sendComment(sessionId, content).map { it.toDomain() }

    override suspend fun getLiveKitToken(sessionId: String, isAdmin: Boolean): Result<String, DataError.Remote> =
        remoteDataSource.getLiveKitToken(sessionId, isAdmin)

    override suspend fun getLiveKitUrl(): String = "wss://your-livekit-server.example.com"
}
