package empire.digiprem.mycolowepapp.core.domain.validator

import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.validator.error.EmailValidationError

interface IEmailValidator {
    fun validate(value: String, maxLength: Int = 254): Result<Unit, EmailValidationError>
}
