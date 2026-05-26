package empire.digiprem.mycoloapp.features.auth.domain.usecase

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.ResultError
import empire.digiprem.mycoloapp.features.auth.domain.error.AuthPasswordError
import empire.digiprem.mycoloapp.features.auth.domain.repository.IAuthPasswordRepository

class ModifierMotDePasseUseCase(private val repository: IAuthPasswordRepository) {
    suspend operator fun invoke(
        newPassword: String,
        confirmPassword: String,
    ): Result<Unit, ResultError> {
        if (newPassword.length < 6) return Result.Failure(AuthPasswordError.PasswordTooShort)
        if (newPassword != confirmPassword) return Result.Failure(AuthPasswordError.PasswordMismatch)
        return repository.updatePassword(newPassword)
    }
}
