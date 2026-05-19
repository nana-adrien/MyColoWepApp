package empire.digiprem.mycolowepapp.feature.admin.security_code.domain.repository

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.model.SecurityCode

interface ISecurityCodeRepository {
    suspend fun getAll(): Result<List<SecurityCode>, DataError.Remote>
    suspend fun generate(adminEmail: String, adminId: String): Result<SecurityCode, DataError.Remote>
    suspend fun toggleActive(id: String, isActive: Boolean): Result<Unit, DataError.Remote>
}
