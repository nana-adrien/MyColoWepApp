package empire.digiprem.mycoloapp.core.domain.validator

import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.validator.error.EmailValidationError

interface IEmailValidator {
    fun validate(value: String, maxLength: Int = 254): Result<Unit, EmailValidationError>
}
