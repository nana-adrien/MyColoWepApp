package empire.digiprem.mycoloapp.feature.admin.login.domain.usecase

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.ResultError
import empire.digiprem.mycoloapp.core.domain.validator.IEmailValidator
import empire.digiprem.mycoloapp.feature.admin.login.domain.error.AdminAuthError
import empire.digiprem.mycoloapp.feature.admin.login.domain.repository.IAdminAuthRepository

class AdminLoginUseCase(private val repository: IAdminAuthRepository, private val emailValidator: IEmailValidator) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Unit, ResultError> {
        val result = emailValidator.validate(email)
        if (result is Result.Failure) {
            return Result.Failure(
                AdminAuthError.FieldError(result.error)
            )

        }


      return  repository.signIn(email, password)/*.mapFailure { dataError ->

            when (dataError) {
                DataError.Remote.Unauthorized -> AdminAuthError.InvalidCredentials
                DataError.Remote.Network -> AdminAuthError.NetworkError
                DataError.Remote.ServerError -> AdminAuthError.Unknown("Erreur serveur")
                is DataError.Remote.Unknown -> AdminAuthError.Unknown(dataError.message)
                else -> AdminAuthError.Unknown("Erreur serveur")
            }
        }*/
    }
}