package empire.digiprem.mycoloapp.features.live.data.datasource

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.data.dto.LiveCommentDto
import empire.digiprem.mycoloapp.features.live.data.dto.LiveDto
import empire.digiprem.mycoloapp.features.live.data.dto.LiveKitTokenRequest
import empire.digiprem.mycoloapp.features.live.data.dto.LiveKitTokenResponse
import empire.digiprem.mycoloapp.features.live.data.dto.StartLiveRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Clock

class LiveRemoteDataSource(private val supabaseClient: SupabaseClient) {

    suspend fun fetchActiveLives(): Result<List<LiveDto>, DataError.Remote> = safeCall {
        supabaseClient.postgrest
            .from("lives")
            .select { filter { eq("is_active", true) }; order("started_at", Order.DESCENDING) }
            .decodeList()
    }

    suspend fun startLive(title: String): Result<LiveDto, DataError.Remote> = safeCall {
        val userId = supabaseClient.auth.currentUserOrNull()?.id ?: error("Not authenticated")
        val roomName = "room_${userId}_${Clock.System.now().toEpochMilliseconds()}"
        supabaseClient.postgrest
            .from("lives")
            .insert(StartLiveRequest(roomName = roomName, startedBy = userId, title = title))
            .decodeSingle()
    }

    suspend fun stopLive(sessionId: String): Result<Unit, DataError.Remote> = safeCall {
        supabaseClient.postgrest
            .from("lives")
            .update({ set("is_active", false); set("ended_at", Clock.System.now().toString()) }) {
                filter { eq("id", sessionId) }
            }
        Unit
    }

    suspend fun joinLive(sessionId: String): Result<Unit, DataError.Remote> = safeCall {
        // viewer_count incrémenté atomiquement via une RPC PostgreSQL dédiée.
        // Pour créer la fonction côté Supabase :
        //   CREATE OR REPLACE FUNCTION increment_viewer_count(live_id_input UUID)
        //   RETURNS void AS $$ UPDATE lives SET viewer_count = viewer_count + 1 WHERE id = live_id_input; $$ LANGUAGE sql;
        supabaseClient.postgrest.rpc("increment_viewer_count", mapOf("live_id_input" to sessionId))
        Unit
    }

    suspend fun sendComment(sessionId: String, content: String): Result<LiveCommentDto, DataError.Remote> = safeCall {
        val userId = supabaseClient.auth.currentUserOrNull()?.id ?: error("Not authenticated")
        val userName = supabaseClient.auth.currentUserOrNull()?.userMetadata?.get("full_name")?.toString()?.trim('"') ?: "Utilisateur"
        supabaseClient.postgrest
            .from("live_comments")
            .insert(mapOf("live_id" to sessionId, "user_id" to userId, "message" to content, "user_name" to userName))
            .decodeSingle()
    }

    suspend fun getLiveKitToken(sessionId: String, isAdmin: Boolean): Result<String, DataError.Remote> = safeCall {
        val user = supabaseClient.auth.currentUserOrNull() ?: error("Not authenticated")
        val participantName = user.userMetadata?.get("full_name")?.toString()?.trim('"') ?: user.id
        val session = supabaseClient.postgrest.from("lives").select { filter { eq("id", sessionId) } }.decodeSingle<LiveDto>()
        val response = supabaseClient.functions.invoke(
            function = "livekit-token",
            body = LiveKitTokenRequest(
                roomName = session.roomName,
                participantName = participantName,
                isAdmin = isAdmin,
            ),
        )
       // val decoded = Json.decodeFromString<LiveKitTokenResponse>(response.body<LiveKitTokenResponse>().toString())
        val decoded =response.body<LiveKitTokenResponse>()
        decoded.token
    }

    suspend fun fetchComments(sessionId: String): Result<List<LiveCommentDto>, DataError.Remote> = safeCall {
        supabaseClient.postgrest
            .from("live_comments")
            .select { filter { eq("live_id", sessionId) }; order("created_at", Order.ASCENDING) }
            .decodeList()
    }
}

private suspend fun <T> safeCall(block: suspend () -> T): Result<T, DataError.Remote> {
    return try {
        Result.Success(block())
    } catch (e: ClientRequestException) {
        when (e.response.status) {
            HttpStatusCode.Unauthorized -> Result.Failure(DataError.Remote.Unauthorized)
            HttpStatusCode.Forbidden -> Result.Failure(DataError.Remote.Forbidden)
            HttpStatusCode.NotFound -> Result.Failure(DataError.Remote.NotFound)
            else -> Result.Failure(DataError.Remote.Unknown)
        }
    } catch (e: Exception) {
        Result.Failure(DataError.Remote.Unknown)
    }
}
