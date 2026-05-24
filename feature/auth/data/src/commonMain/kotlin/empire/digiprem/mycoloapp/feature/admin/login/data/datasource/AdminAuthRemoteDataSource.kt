package empire.digiprem.mycoloapp.feature.admin.login.data.datasource

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.data.safeSupabaseCall
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AdminAuthRemoteDataSource(private val client: SupabaseClient) {
    suspend fun signIn(email: String, password: String): Result<Unit, DataError.Remote> =
        safeSupabaseCall {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

    suspend fun signOut(): Result<Unit, DataError.Remote> =
        safeSupabaseCall { client.auth.signOut() }
}
