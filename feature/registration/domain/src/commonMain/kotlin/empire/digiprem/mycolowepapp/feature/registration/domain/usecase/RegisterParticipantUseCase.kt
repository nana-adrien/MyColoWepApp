package empire.digiprem.mycolowepapp.feature.registration.domain.usecase

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.mapFailure
import empire.digiprem.mycolowepapp.core.domain.util.onFailure
import empire.digiprem.mycolowepapp.core.domain.util.onSuccess
import empire.digiprem.mycolowepapp.feature.registration.domain.error.RegistrationError
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import empire.digiprem.mycolowepapp.feature.registration.domain.repository.IRegistrationRepository

class RegisterParticipantUseCase(private val repository: IRegistrationRepository) {
    suspend operator fun invoke(form: RegistrationForm): Result<String, RegistrationError> {
        val mapFailure:(DataError.Remote)->RegistrationError = {
            when (it) {
                DataError.Remote.Conflict  -> RegistrationError.AlreadyRegistered
                DataError.Remote.NotFound  -> RegistrationError.InvalidSecurityCode
                DataError.Remote.Network   -> RegistrationError.NetworkError
                else -> RegistrationError.Unknown(
                    (it as? DataError.Remote.Unknown)?.message ?: "Erreur inconnue"
                )
            }
        }
        return when (val result= repository.validateSecurityCode(form.securityCode)) {
            is Result.Success -> {
                repository
                    .register(form)
                    .mapFailure { dataError ->mapFailure(dataError)  }
            }
            is Result.Failure -> result.mapFailure { mapFailure(it) }
        }

    }

}
