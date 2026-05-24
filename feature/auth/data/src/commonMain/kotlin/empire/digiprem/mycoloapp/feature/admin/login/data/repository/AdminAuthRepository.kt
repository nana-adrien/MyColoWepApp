package empire.digiprem.mycoloapp.feature.admin.login.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.feature.admin.login.data.datasource.AdminAuthRemoteDataSource
import empire.digiprem.mycoloapp.feature.admin.login.domain.repository.IAdminAuthRepository

class AdminAuthRepository(
    private val remoteDataSource: AdminAuthRemoteDataSource
) : IAdminAuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit, DataError.Remote> =
        remoteDataSource.signIn(email, password)

    override suspend fun signOut(): Result<Unit, DataError.Remote> =
        remoteDataSource.signOut()
}
