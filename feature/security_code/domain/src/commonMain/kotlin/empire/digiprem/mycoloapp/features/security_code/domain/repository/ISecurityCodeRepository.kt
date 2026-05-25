package empire.digiprem.mycoloapp.features.security_code.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.security_code.domain.model.SecurityCode

interface ISecurityCodeRepository {
    suspend fun getAll(): Result<List<SecurityCode>, DataError.Remote>
    suspend fun generate(adminId: String): Result<Unit, DataError.Remote>
    suspend fun toggleActive(id: String, isActive: Boolean): Result<Unit, DataError.Remote>
}
