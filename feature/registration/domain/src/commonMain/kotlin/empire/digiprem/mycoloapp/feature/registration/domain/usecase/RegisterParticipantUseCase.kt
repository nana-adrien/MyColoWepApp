package empire.digiprem.mycoloapp.feature.registration.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.feature.registration.domain.model.RegistrationForm
import empire.digiprem.mycoloapp.feature.registration.domain.repository.IRegistrationRepository

class RegisterParticipantUseCase(private val repository: IRegistrationRepository) {
    suspend operator fun invoke(form: RegistrationForm): Result<String, DataError.Remote> {
       /* val mapFailure:(DataError.Remote)->RegistrationError = {
            when (it) {
                DataError.Remote.Conflict  -> RegistrationError.AlreadyRegistered
                DataError.Remote.NotFound  -> RegistrationError.InvalidSecurityCode
                DataError.Remote.Network   -> RegistrationError.NetworkError
                else -> RegistrationError.Unknown(
                    (it as? DataError.Remote.Unknown)?.message ?: "Erreur inconnue"
                )
            }
        }*/
        return when (val result = repository.validateSecurityCode(form.securityCode)) {
            is Result.Success -> {
                repository
                    .register(form.copy(securityCode = result.data))

            }
            is Result.Failure -> result
        }

    }

}
