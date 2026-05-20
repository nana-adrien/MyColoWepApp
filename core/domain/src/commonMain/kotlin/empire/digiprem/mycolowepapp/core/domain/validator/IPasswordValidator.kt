package empire.digiprem.mycolowepapp.core.domain.validator

import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.validator.error.PasswordValidationError

interface IPasswordValidator {
    fun validate(
        value: String,
        confirmValue: String? = null,
        minLength: Int = 8,
        maxLength: Int = 128,
        requireUpperCase: Boolean = true,
        requireLowerCase: Boolean = true,
        requireDigit: Boolean = true,
        requireSpecialChar: Boolean = true,
    ): Result<Unit, PasswordValidationError>
}
