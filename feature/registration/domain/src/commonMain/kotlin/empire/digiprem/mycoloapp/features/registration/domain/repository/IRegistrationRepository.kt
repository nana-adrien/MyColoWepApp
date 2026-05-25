package empire.digiprem.mycoloapp.features.registration.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.registration.domain.model.RegistrationForm

interface IRegistrationRepository {
    suspend fun validateSecurityCode(code: String): Result<String, DataError.Remote>
    suspend fun register(form: RegistrationForm): Result<String, DataError.Remote>  // retourne le n° de référence
}
