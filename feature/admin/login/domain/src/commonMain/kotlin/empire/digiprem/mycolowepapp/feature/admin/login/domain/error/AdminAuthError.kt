package empire.digiprem.mycolowepapp.feature.admin.login.domain.error

import empire.digiprem.mycolowepapp.core.domain.util.ResultError

sealed interface AdminAuthError : ResultError {
    data object InvalidCredentials : AdminAuthError
    data object NetworkError : AdminAuthError
    data class Unknown(val message: String) : AdminAuthError
}
