package empire.digiprem.mycolowepapp.core.domain.validator

import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.validator.error.PasswordValidationError

class PasswordValidator : IPasswordValidator {

    override fun validate(
        value: String,
        confirmValue: String?,
        minLength: Int,
        maxLength: Int,
        requireUpperCase: Boolean,
        requireLowerCase: Boolean,
        requireDigit: Boolean,
        requireSpecialChar: Boolean,
    ): Result<Unit, PasswordValidationError> = when {
        value.isBlank()
            -> Result.Failure(PasswordValidationError.Empty)
        value.length < minLength
            -> Result.Failure(PasswordValidationError.TooShort)
        value.length > maxLength
            -> Result.Failure(PasswordValidationError.TooLong)
        requireUpperCase && !value.any { it.isUpperCase() }
            -> Result.Failure(PasswordValidationError.NoUpperCase)
        requireLowerCase && !value.any { it.isLowerCase() }
            -> Result.Failure(PasswordValidationError.NoLowerCase)
        requireDigit && !value.any { it.isDigit() }
            -> Result.Failure(PasswordValidationError.NoDigit)
        requireSpecialChar && !value.any { it in SPECIAL_CHARS }
            -> Result.Failure(PasswordValidationError.NoSpecialChar)
        confirmValue != null && value != confirmValue
            -> Result.Failure(PasswordValidationError.DoesNotMatch(confirmValue))
        else
            -> Result.Success(Unit)
    }

    companion object {
        const val SPECIAL_CHARS = "!@#\$%^&*()_+-=[]{}|;':\",./<>?"
    }
}
