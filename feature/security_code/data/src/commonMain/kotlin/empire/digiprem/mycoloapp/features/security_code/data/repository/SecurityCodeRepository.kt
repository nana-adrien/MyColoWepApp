package empire.digiprem.mycoloapp.features.security_code.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.Tools
import empire.digiprem.mycoloapp.core.domain.util.map
import empire.digiprem.mycoloapp.features.security_code.data.datasource.SecurityCodeRemoteDataSource
import empire.digiprem.mycoloapp.features.security_code.data.dto.SecurityCodeInsertDto
import empire.digiprem.mycoloapp.features.security_code.data.dto.toDomain
import empire.digiprem.mycoloapp.features.security_code.domain.model.SecurityCode
import empire.digiprem.mycoloapp.features.security_code.domain.repository.ISecurityCodeRepository

class SecurityCodeRepository(
    private val remoteDataSource: SecurityCodeRemoteDataSource
) : ISecurityCodeRepository {

    override suspend fun getAll(): Result<List<SecurityCode>, DataError.Remote> =
        remoteDataSource.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun generate(adminId: String): Result<Unit, DataError.Remote> {
        val code = generateCode()
        val dto = SecurityCodeInsertDto(code = code, createdBy = adminId)
        return remoteDataSource.insert(dto)//.map { it.toDomain() }
    }
    override suspend fun toggleActive(id: String, isActive: Boolean): Result<Unit, DataError.Remote> =
        remoteDataSource.updateActive(id, isActive)
}

private fun generateCode(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val random = (1..6).map { chars.random() }.joinToString("")
    return "${Tools.securityCodePrefix}$random"
}
