package empire.digiprem.mycoloapp.feature.registration.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.feature.registration.domain.repository.IRegistrationRepository

class ValidateSecurityCodeUseCase(private val repository: IRegistrationRepository) {
    suspend operator fun invoke(code: String): Result<String, DataError.Remote> =
        repository.validateSecurityCode(code)/*.mapFailure { dataError ->
            when (dataError) {
                DataError.Remote.NotFound  -> RegistrationError.InvalidSecurityCode
                DataError.Remote.Network   -> RegistrationError.NetworkError
                else -> RegistrationError.Unknown(
                    (dataError as? DataError.Remote.Unknown)?.message ?: "Erreur inconnue"
                )
            }
        }*/
}
