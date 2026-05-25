package empire.digiprem.mycoloapp.features.auth.domain.error

import empire.digiprem.mycoloapp.core.domain.util.ResultError

sealed interface AdminAuthError : ResultError {
    data object InvalidCredentials : AdminAuthError
    data object NetworkError : AdminAuthError
    data class Unknown(val message: String) : AdminAuthError
    data class FieldError(val error: ResultError) : AdminAuthError
}
