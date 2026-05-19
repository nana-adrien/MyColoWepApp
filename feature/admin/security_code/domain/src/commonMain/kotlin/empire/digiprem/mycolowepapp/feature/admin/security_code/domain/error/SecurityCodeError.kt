package empire.digiprem.mycolowepapp.feature.admin.security_code.domain.error

import empire.digiprem.mycolowepapp.core.domain.util.ResultError

sealed interface SecurityCodeError : ResultError {
    data object Unauthorized : SecurityCodeError
    data object NetworkError : SecurityCodeError
    data class Unknown(val message: String) : SecurityCodeError
}
