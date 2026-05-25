package empire.digiprem.mycoloapp.features.live.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import kotlinx.coroutines.flow.Flow

interface ILiveRepository {
    fun observeActiveSessions(): Flow<List<LiveSession>>
    fun observeComments(sessionId: String): Flow<List<LiveComment>>
    suspend fun fetchActiveSessions(): Result<List<LiveSession>, DataError.Remote>
    suspend fun startLive(title: String): Result<LiveSession, DataError.Remote>
    suspend fun stopLive(sessionId: String): Result<Unit, DataError.Remote>
    suspend fun joinLive(sessionId: String): Result<Unit, DataError.Remote>
    suspend fun sendComment(sessionId: String, content: String): Result<LiveComment, DataError.Remote>
    suspend fun getLiveKitToken(sessionId: String, isAdmin: Boolean): Result<String, DataError.Remote>
    suspend fun getLiveKitUrl(): String
}
