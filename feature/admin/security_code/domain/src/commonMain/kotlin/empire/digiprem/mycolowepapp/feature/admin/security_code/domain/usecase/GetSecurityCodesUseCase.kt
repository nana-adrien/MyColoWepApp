package empire.digiprem.mycolowepapp.feature.admin.security_code.domain.usecase

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.mapFailure
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.error.SecurityCodeError
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.model.SecurityCode
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.repository.ISecurityCodeRepository

class GetSecurityCodesUseCase(private val repository: ISecurityCodeRepository) {
    suspend operator fun invoke(): Result<List<SecurityCode>, SecurityCodeError> =
        repository.getAll().mapFailure { it.toSecurityCodeError() }
}

class GenerateSecurityCodeUseCase(private val repository: ISecurityCodeRepository) {
    suspend operator fun invoke(adminEmail: String, adminId: String): Result<SecurityCode, SecurityCodeError> =
        repository.generate(adminEmail, adminId).mapFailure { it.toSecurityCodeError() }
}

class ToggleSecurityCodeUseCase(private val repository: ISecurityCodeRepository) {
    suspend operator fun invoke(id: String, isActive: Boolean): Result<Unit, SecurityCodeError> =
        repository.toggleActive(id, isActive).mapFailure { it.toSecurityCodeError() }
}

private fun DataError.Remote.toSecurityCodeError(): SecurityCodeError = when (this) {
    DataError.Remote.Unauthorized -> SecurityCodeError.Unauthorized
    DataError.Remote.Network      -> SecurityCodeError.NetworkError
    else -> SecurityCodeError.Unknown(
        (this as? DataError.Remote.Unknown)?.message ?: "Erreur inconnue"
    )
}
