package empire.digiprem.mycoloapp.features.auth.data.datasource

import empire.digiprem.mycoloapp.core.data.safeSupabaseCall
import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth

class AuthPasswordRemoteDataSource(private val client: SupabaseClient) {

    suspend fun updatePassword(newPassword: String): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.auth.updateUser { password = newPassword }
        }

    suspend fun resetPasswordForEmail(email: String): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.auth.resetPasswordForEmail(email)
        }

    suspend fun verifyEmailOtp(email: String, token: String): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.auth.verifyEmailOtp(
                type = OtpType.Email,
                email = email,
                token = token,
            )
        }
}
