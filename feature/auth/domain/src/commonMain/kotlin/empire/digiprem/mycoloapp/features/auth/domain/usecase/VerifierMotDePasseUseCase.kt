package empire.digiprem.mycoloapp.features.auth.domain.usecase

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.ResultError
import empire.digiprem.mycoloapp.features.auth.domain.error.AuthPasswordError
import empire.digiprem.mycoloapp.features.auth.domain.repository.IAuthPasswordRepository

class VerifierMotDePasseUseCase(private val repository: IAuthPasswordRepository) {
    suspend operator fun invoke(email: String, token: String): Result<Unit, ResultError> {
        if (token.length != 6) return Result.Failure(AuthPasswordError.InvalidOtp)
        return repository.verifyEmailOtp(email, token)
    }
}
