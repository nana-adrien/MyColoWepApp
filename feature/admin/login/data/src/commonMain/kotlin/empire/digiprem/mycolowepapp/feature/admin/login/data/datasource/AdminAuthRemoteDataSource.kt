package empire.digiprem.mycolowepapp.feature.admin.login.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.safeSupabaseCall
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
