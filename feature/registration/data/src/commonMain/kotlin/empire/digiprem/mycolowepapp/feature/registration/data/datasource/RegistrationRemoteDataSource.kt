package empire.digiprem.mycolowepapp.feature.registration.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.feature.registration.data.dto.ParticipantInsertDto
import empire.digiprem.mycolowepapp.feature.registration.data.dto.SecurityCodeValidationDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RegistrationRemoteDataSource(private val client: SupabaseClient) {

    suspend fun validateSecurityCode(code: String): Result<Boolean, DataError.Remote> =
        safeCall {
            val result = client.from("security_codes")
                .select {
                    filter {
                        eq("code", code)
                        eq("is_active", true)
                    }
                }
                .decodeSingleOrNull<SecurityCodeValidationDto>()
            result != null
        }

    suspend fun insert(dto: ParticipantInsertDto): Result<Unit, DataError.Remote> =
        safeCall {
            client.from("participants").insert(dto)
        }

    suspend fun incrementCodeUsage(code: String): Result<Unit, DataError.Remote> =
        safeCall {
            client.postgrest.rpc(
                "increment_code_usage",
                buildJsonObject { put("p_code", code) }
            )
        }
}

private suspend fun <T> safeCall(call: suspend () -> T): Result<T, DataError.Remote> =
    try {
        Result.Success(call())
    } catch (e: Exception) {
        val message = e.message ?: ""
        val error = when {
            message.contains("23505") ||
            message.contains("unique", ignoreCase = true) ||
            message.contains("duplicate", ignoreCase = true) ||
            message.contains("409")     -> DataError.Remote.Conflict
            message.contains("404") ||
            message.contains("not found", ignoreCase = true) -> DataError.Remote.NotFound
            message.contains("401")     -> DataError.Remote.Unauthorized
            message.contains("500")     -> DataError.Remote.ServerError
            message.contains("network", ignoreCase = true) ||
            message.contains("connect", ignoreCase = true)  -> DataError.Remote.Network
            else                        -> DataError.Remote.Unknown(message)
        }
        Result.Failure(error)
    }
