package empire.digiprem.mycoloapp.core.design_system.extension

import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.core.domain.validator.error.PasswordValidationError
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.validation_password_does_not_match
import mycolowepapp.shared.generated.resources.validation_password_empty
import mycolowepapp.shared.generated.resources.validation_password_no_digit
import mycolowepapp.shared.generated.resources.validation_password_no_lowercase
import mycolowepapp.shared.generated.resources.validation_password_no_special_char
import mycolowepapp.shared.generated.resources.validation_password_no_uppercase
import mycolowepapp.shared.generated.resources.validation_password_too_long
import mycolowepapp.shared.generated.resources.validation_password_too_short

fun PasswordValidationError.toUiText(): UiText = when (this) {
    PasswordValidationError.Empty
        -> UiText.Resource(Res.string.validation_password_empty)
    PasswordValidationError.TooShort
        -> UiText.Resource(Res.string.validation_password_too_short)
    PasswordValidationError.TooLong
        -> UiText.Resource(Res.string.validation_password_too_long)
    PasswordValidationError.NoUpperCase
        -> UiText.Resource(Res.string.validation_password_no_uppercase)
    PasswordValidationError.NoLowerCase
        -> UiText.Resource(Res.string.validation_password_no_lowercase)
    PasswordValidationError.NoDigit
        -> UiText.Resource(Res.string.validation_password_no_digit)
    PasswordValidationError.NoSpecialChar
        -> UiText.Resource(Res.string.validation_password_no_special_char)
    is PasswordValidationError.DoesNotMatch
        -> UiText.Resource(Res.string.validation_password_does_not_match)
    is PasswordValidationError.ServerError
        -> UiText.DynamicString(message)
}
