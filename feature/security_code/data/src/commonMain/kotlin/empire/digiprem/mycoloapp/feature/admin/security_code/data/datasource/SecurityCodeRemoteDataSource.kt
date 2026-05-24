package empire.digiprem.mycoloapp.feature.admin.security_code.data.datasource

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.data.safeSupabaseCall
import empire.digiprem.mycoloapp.feature.admin.security_code.data.dto.SecurityCodeInsertDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class SecurityCodeRemoteDataSource(private val client: SupabaseClient) {

    suspend fun getAll(): Result<List<SecurityCodeWithCreatorDto>, DataError.Remote> =
        safeSupabaseCall {
            client.from("security_codes_with_creator")
                .select { order("created_at", Order.DESCENDING) }
                .decodeList()
        }

    suspend fun insert(dto: SecurityCodeInsertDto): Result<Unit, DataError.Remote> {
        var pressed=0
        return safeSupabaseCall {
            pressed++
            println("SecurityCodeAction: GenerateCode $pressed")
            client.from("security_codes")
                .insert(dto) { select()}
                .decodeSingle()
        }
    }

    suspend fun updateActive(id: String, isActive: Boolean): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.from("security_codes")
                .update({ set("is_active", isActive) }) {
                    filter { eq("id", id) }
                }
        }
}
