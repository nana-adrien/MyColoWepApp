package empire.digiprem.mycoloapp.features.auth.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result

interface IAuthPasswordRepository {
    suspend fun updatePassword(newPassword: String): Result<Unit, DataError.Remote>
    suspend fun resetPasswordForEmail(email: String): Result<Unit, DataError.Remote>
    suspend fun verifyEmailOtp(email: String, token: String): Result<Unit, DataError.Remote>
}
