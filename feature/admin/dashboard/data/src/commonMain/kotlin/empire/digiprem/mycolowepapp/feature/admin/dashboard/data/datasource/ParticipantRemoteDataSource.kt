package empire.digiprem.mycolowepapp.feature.admin.dashboard.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.dto.ParticipantDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class ParticipantRemoteDataSource(private val client: SupabaseClient) {

    suspend fun getAll(): Result<List<ParticipantDto>, DataError.Remote> =
        safeSupabaseCall { client.from("participants").select().decodeList() }
}

internal suspend fun <T> safeSupabaseCall(call: suspend () -> T): Result<T, DataError.Remote> =
    try {
        Result.Success(call())
    } catch (e: Exception) {
        val message = e.message ?: ""
        when {
            message.contains("401") || message.contains("Unauthorized", ignoreCase = true) ->
                Result.Failure(DataError.Remote.Unauthorized)
            message.contains("network", ignoreCase = true) ||
            message.contains("connect", ignoreCase = true) ->
                Result.Failure(DataError.Remote.Network)
            message.contains("500") || message.contains("server", ignoreCase = true) ->
                Result.Failure(DataError.Remote.ServerError)
            else -> Result.Failure(DataError.Remote.Unknown(message))
        }
    }
