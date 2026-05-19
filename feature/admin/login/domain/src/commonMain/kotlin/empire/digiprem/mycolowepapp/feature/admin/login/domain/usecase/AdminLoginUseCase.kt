package empire.digiprem.mycolowepapp.feature.admin.login.domain.usecase

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.mapFailure
import empire.digiprem.mycolowepapp.feature.admin.login.domain.error.AdminAuthError
import empire.digiprem.mycolowepapp.feature.admin.login.domain.repository.IAdminAuthRepository

class AdminLoginUseCase(private val repository: IAdminAuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Unit, AdminAuthError> =
        repository.signIn(email, password).mapFailure { dataError ->
            when (dataError) {
                DataError.Remote.Unauthorized -> AdminAuthError.InvalidCredentials
                DataError.Remote.Network      -> AdminAuthError.NetworkError
                DataError.Remote.ServerError  -> AdminAuthError.Unknown("Erreur serveur")
                is DataError.Remote.Unknown   -> AdminAuthError.Unknown(dataError.message)
                else ->  AdminAuthError.Unknown("Erreur serveur")
            }
        }
}
