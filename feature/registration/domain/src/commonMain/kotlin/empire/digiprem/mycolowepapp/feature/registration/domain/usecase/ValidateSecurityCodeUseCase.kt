package empire.digiprem.mycolowepapp.feature.registration.domain.usecase

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.mapFailure
import empire.digiprem.mycolowepapp.feature.registration.domain.error.RegistrationError
import empire.digiprem.mycolowepapp.feature.registration.domain.repository.IRegistrationRepository

class ValidateSecurityCodeUseCase(private val repository: IRegistrationRepository) {
    suspend operator fun invoke(code: String): Result<Boolean, RegistrationError> =
        repository.validateSecurityCode(code).mapFailure { dataError ->
            when (dataError) {
                DataError.Remote.NotFound  -> RegistrationError.InvalidSecurityCode
                DataError.Remote.Network   -> RegistrationError.NetworkError
                else -> RegistrationError.Unknown(
                    (dataError as? DataError.Remote.Unknown)?.message ?: "Erreur inconnue"
                )
            }
        }
}
