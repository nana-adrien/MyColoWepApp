package empire.digiprem.mycolowepapp.feature.admin.login.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AdminAuthRemoteDataSource(private val client: SupabaseClient) {

    suspend fun signIn(email: String, password: String): Result<Unit, DataError.Remote> =
        safeAuthCall {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

    suspend fun signOut(): Result<Unit, DataError.Remote> =
        safeAuthCall { client.auth.signOut() }
}

private suspend fun safeAuthCall(call: suspend () -> Unit): Result<Unit, DataError.Remote> =
    try {
        call()
        Result.Success(Unit)
    } catch (e: Exception) {
        val message = e.message ?: ""
        val error = when {
            message.contains("Invalid login", ignoreCase = true) ||
            message.contains("invalid_grant", ignoreCase = true) ||
            message.contains("400")        -> DataError.Remote.Unauthorized
            message.contains("network", ignoreCase = true) ||
            message.contains("connect", ignoreCase = true) -> DataError.Remote.Network
            message.contains("500")        -> DataError.Remote.ServerError
            else                           -> DataError.Remote.Unknown(message)
        }
        throw e
        Result.Failure(error)
    }
