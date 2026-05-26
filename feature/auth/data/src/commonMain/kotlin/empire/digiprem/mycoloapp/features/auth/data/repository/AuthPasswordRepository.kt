package empire.digiprem.mycoloapp.features.auth.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.auth.data.datasource.AuthPasswordRemoteDataSource
import empire.digiprem.mycoloapp.features.auth.domain.repository.IAuthPasswordRepository

class AuthPasswordRepository(
    private val dataSource: AuthPasswordRemoteDataSource
) : IAuthPasswordRepository {

    override suspend fun updatePassword(newPassword: String): Result<Unit, DataError.Remote> =
        dataSource.updatePassword(newPassword)

    override suspend fun resetPasswordForEmail(email: String): Result<Unit, DataError.Remote> =
        dataSource.resetPasswordForEmail(email)

    override suspend fun verifyEmailOtp(email: String, token: String): Result<Unit, DataError.Remote> =
        dataSource.verifyEmailOtp(email, token)
}
