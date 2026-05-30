package empire.digiprem.mycoloapp.core.design_system.extension

import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.core.domain.validator.error.EmailValidationError
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.validation_email_empty
import mycolowepapp.shared.generated.resources.validation_email_invalid_format
import mycolowepapp.shared.generated.resources.validation_email_too_long

fun EmailValidationError.toUiText(): UiText = when (this) {
    EmailValidationError.Empty
        -> UiText.Resource(Res.string.validation_email_empty)
    EmailValidationError.InvalidFormat
        -> UiText.Resource(Res.string.validation_email_invalid_format)
    EmailValidationError.TooLong
        -> UiText.Resource(Res.string.validation_email_too_long)
    is EmailValidationError.ServerError
        -> UiText.DynamicString(message)
}
