package empire.digiprem.mycolowepapp.feature.admin.login.domain.repository

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result

interface IAdminAuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit, DataError.Remote>
    suspend fun signOut(): Result<Unit, DataError.Remote>
}
