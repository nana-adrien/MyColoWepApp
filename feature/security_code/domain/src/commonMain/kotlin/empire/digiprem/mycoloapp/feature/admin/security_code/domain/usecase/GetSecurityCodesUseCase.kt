package empire.digiprem.mycoloapp.feature.admin.security_code.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.mapFailure
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.error.SecurityCodeError
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.model.SecurityCode
import empire.digiprem.mycoloapp.feature.admin.security_code.domain.repository.ISecurityCodeRepository

class GetSecurityCodesUseCase(private val repository: ISecurityCodeRepository) {
    suspend operator fun invoke(): Result<List<SecurityCode>, SecurityCodeError> =
        repository.getAll().mapFailure { it.toSecurityCodeError() }
}

class GenerateSecurityCodeUseCase(private val repository: ISecurityCodeRepository) {


    suspend operator fun invoke(adminEmail: String, adminId: String): Result<Unit, DataError.Remote> {

        return  repository.generate( adminId)
    }
       //.mapFailure { it.toSecurityCodeError() }
}

class ToggleSecurityCodeUseCase(private val repository: ISecurityCodeRepository) {
    suspend operator fun invoke(id: String, isActive: Boolean): Result<Unit, DataError.Remote> =
        repository.toggleActive(id, isActive)//.mapFailure { it.toSecurityCodeError() }
}

private fun DataError.Remote.toSecurityCodeError(): SecurityCodeError = when (this) {
    DataError.Remote.Unauthorized -> SecurityCodeError.Unauthorized
    DataError.Remote.Network      -> SecurityCodeError.NetworkError
    else -> SecurityCodeError.Unknown(
        "Erreur inconnue"
    )
}
