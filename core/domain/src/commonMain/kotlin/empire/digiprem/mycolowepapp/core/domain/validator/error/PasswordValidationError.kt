package empire.digiprem.mycolowepapp.core.domain.validator.error

import empire.digiprem.mycolowepapp.core.domain.util.ResultError

sealed interface PasswordValidationError : ResultError {
    data object Empty : PasswordValidationError
    data object TooShort : PasswordValidationError
    data object TooLong : PasswordValidationError
    data object NoUpperCase : PasswordValidationError
    data object NoLowerCase : PasswordValidationError
    data object NoDigit : PasswordValidationError
    data object NoSpecialChar : PasswordValidationError
    data class DoesNotMatch(val confirmPassword: String) : PasswordValidationError
    data class ServerError(val message: String) : PasswordValidationError
}
