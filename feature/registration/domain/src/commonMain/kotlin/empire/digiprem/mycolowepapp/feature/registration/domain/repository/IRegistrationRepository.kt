package empire.digiprem.mycolowepapp.feature.registration.domain.repository

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm

interface IRegistrationRepository {
    suspend fun validateSecurityCode(code: String): Result<Boolean, DataError.Remote>
    suspend fun register(form: RegistrationForm): Result<String, DataError.Remote>  // retourne le n° de référence
}
