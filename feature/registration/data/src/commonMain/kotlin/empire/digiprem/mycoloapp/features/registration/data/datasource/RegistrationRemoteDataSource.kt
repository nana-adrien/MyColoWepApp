package empire.digiprem.mycoloapp.features.registration.data.datasource

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.data.safeSupabaseCall
import empire.digiprem.mycoloapp.core.domain.error.AppErrorCode
import empire.digiprem.mycoloapp.core.domain.util.mapOrFailureIfNull
import empire.digiprem.mycoloapp.features.registration.data.dto.ParticipantInsertDto
import empire.digiprem.mycoloapp.features.registration.data.dto.SecurityCodeValidationDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RegistrationRemoteDataSource(private val client: SupabaseClient) {

    suspend fun validateSecurityCode(code: String): Result<String, DataError.Remote> =
        safeSupabaseCall {
           client.from("security_codes")
                .select {
                    filter {
                        eq("code", code)
                        eq("is_active", true)
                    }
                }
                .decodeSingleOrNull<SecurityCodeValidationDto>()
        }.mapOrFailureIfNull(
            transform = {
                it.id
            },
            onFailure = {
                DataError.Remote.InvalidOperation(AppErrorCode.CODE_NOT_FOUND)
            }
        )

    suspend fun insert(dto: ParticipantInsertDto): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.from("participants").insert(dto)
        }

    suspend fun incrementCodeUsage(code: String): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.postgrest.rpc(
                "increment_code_usage",
                buildJsonObject { put("p_code", code) }
            )
        }
}
