package empire.digiprem.mycolowepapp.feature.admin.security_code.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.feature.admin.security_code.data.dto.SecurityCodeDto
import empire.digiprem.mycolowepapp.feature.admin.security_code.data.dto.SecurityCodeInsertDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class SecurityCodeRemoteDataSource(private val client: SupabaseClient) {

    suspend fun getAll(): Result<List<SecurityCodeDto>, DataError.Remote> =
        safeCall {
            client.from("security_codes")
                .select { order("created_at", Order.DESCENDING) }
                .decodeList()
        }

    suspend fun insert(dto: SecurityCodeInsertDto): Result<SecurityCodeDto, DataError.Remote> =
        safeCall {
            client.from("security_codes")
                .insert(dto) { select() }
                .decodeSingle()
        }

    suspend fun updateActive(id: String, isActive: Boolean): Result<Unit, DataError.Remote> =
        safeCall {
            client.from("security_codes")
                .update({ set("is_active", isActive) }) {
                    filter { eq("id", id) }
                }
        }
}

private suspend fun <T> safeCall(call: suspend () -> T): Result<T, DataError.Remote> =
    try {
        Result.Success(call())
    } catch (e: Exception) {
        val message = e.message ?: ""
        val error = when {
            message.contains("401") -> DataError.Remote.Unauthorized
            message.contains("500") -> DataError.Remote.ServerError
            message.contains("network", ignoreCase = true) ||
            message.contains("connect", ignoreCase = true) -> DataError.Remote.Network
            else -> DataError.Remote.Unknown(message)
        }
        Result.Failure(error)
    }
