package empire.digiprem.mycolowepapp.feature.registration.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.data.networking.safeSupabaseCall
import empire.digiprem.mycolowepapp.feature.registration.data.dto.ParticipantInsertDto
import empire.digiprem.mycolowepapp.feature.registration.data.dto.SecurityCodeValidationDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RegistrationRemoteDataSource(private val client: SupabaseClient) {

    suspend fun validateSecurityCode(code: String): Result<Boolean, DataError.Remote> =
        safeSupabaseCall {
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
