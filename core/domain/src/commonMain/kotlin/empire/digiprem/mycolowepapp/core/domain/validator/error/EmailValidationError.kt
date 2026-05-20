package empire.digiprem.mycolowepapp.core.domain.validator.error

import empire.digiprem.mycolowepapp.core.domain.util.ResultError

sealed interface EmailValidationError : ResultError {
    data object Empty : EmailValidationError
    data object InvalidFormat : EmailValidationError
    data object TooLong : EmailValidationError
    data class ServerError(val message: String) : EmailValidationError
}
