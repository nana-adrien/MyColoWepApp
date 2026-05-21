package empire.digiprem.mycolowepapp.feature.admin.security_code.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.data.networking.safeSupabaseCall
import empire.digiprem.mycolowepapp.feature.admin.security_code.data.dto.SecurityCodeDto
import empire.digiprem.mycolowepapp.feature.admin.security_code.data.dto.SecurityCodeInsertDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class SecurityCodeRemoteDataSource(private val client: SupabaseClient) {

    suspend fun getAll(): Result<List<SecurityCodeDto>, DataError.Remote> =
        safeSupabaseCall {
            client.from("security_codes")
                .select { order("created_at", Order.DESCENDING) }
                .decodeList()
        }

    suspend fun insert(dto: SecurityCodeInsertDto): Result<SecurityCodeDto, DataError.Remote> =
        safeSupabaseCall {
            client.from("security_codes")
                .insert(dto) { select() }
                .decodeSingle()
        }

    suspend fun updateActive(id: String, isActive: Boolean): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.from("security_codes")
                .update({ set("is_active", isActive) }) {
                    filter { eq("id", id) }
                }
        }
}
