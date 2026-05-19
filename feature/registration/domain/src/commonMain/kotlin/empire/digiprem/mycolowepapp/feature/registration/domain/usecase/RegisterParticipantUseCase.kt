package empire.digiprem.mycolowepapp.feature.registration.domain.usecase

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.mapFailure
import empire.digiprem.mycolowepapp.feature.registration.domain.error.RegistrationError
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import empire.digiprem.mycolowepapp.feature.registration.domain.repository.IRegistrationRepository

class RegisterParticipantUseCase(private val repository: IRegistrationRepository) {
    suspend operator fun invoke(form: RegistrationForm): Result<String, RegistrationError> =
        repository.register(form).mapFailure { dataError ->
            when (dataError) {
                DataError.Remote.Conflict  -> RegistrationError.AlreadyRegistered
                DataError.Remote.NotFound  -> RegistrationError.InvalidSecurityCode
                DataError.Remote.Network   -> RegistrationError.NetworkError
                else -> RegistrationError.Unknown(
                    (dataError as? DataError.Remote.Unknown)?.message ?: "Erreur inconnue"
                )
            }
        }
}
