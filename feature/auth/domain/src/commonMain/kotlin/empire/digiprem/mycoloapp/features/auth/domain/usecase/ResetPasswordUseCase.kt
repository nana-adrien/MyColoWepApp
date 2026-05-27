package empire.digiprem.mycoloapp.features.auth.domain.usecase

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.ResultError
import empire.digiprem.mycoloapp.features.auth.domain.error.AuthPasswordError
import empire.digiprem.mycoloapp.features.auth.domain.repository.IAuthPasswordRepository

class ResetPasswordUseCase(private val repository: IAuthPasswordRepository) {
    suspend operator fun invoke(email: String): Result<Unit, ResultError> {
        if (email.isBlank()) return Result.Failure(AuthPasswordError.InvalidEmail)
        return repository.resetPasswordForEmail(email)
    }
}
