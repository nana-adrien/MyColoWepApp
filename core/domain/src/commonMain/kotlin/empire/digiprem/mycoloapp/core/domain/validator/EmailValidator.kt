package empire.digiprem.mycoloapp.core.domain.validator

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.validator.error.EmailValidationError

class EmailValidator : IEmailValidator {

    override fun validate(value: String, maxLength: Int): Result<Unit, EmailValidationError> = when {
        value.isBlank()
            -> Result.Failure(EmailValidationError.Empty)
        value.length > maxLength
            -> Result.Failure(EmailValidationError.TooLong)
        !EMAIL_REGEX.matches(value.trim())
            -> Result.Failure(EmailValidationError.InvalidFormat)
        else
            -> Result.Success(Unit)
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")
    }
}
