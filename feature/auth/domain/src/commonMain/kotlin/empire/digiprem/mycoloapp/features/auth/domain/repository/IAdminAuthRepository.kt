package empire.digiprem.mycoloapp.features.auth.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result

interface IAdminAuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit, DataError.Remote>
  //  suspend fun signUp(email: String, password: String, fullName: String): Result<Unit, DataError.Remote>
    suspend fun signOut(): Result<Unit, DataError.Remote>
}
