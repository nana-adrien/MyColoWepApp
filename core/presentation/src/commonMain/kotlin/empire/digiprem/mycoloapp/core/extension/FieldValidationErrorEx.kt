package empire.digiprem.mycoloapp.core.design_system.extension

import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.core.domain.validator.FieldValidationError
import empire.digiprem.mycoloapp.core.domain.validator.error.EmailValidationError
import empire.digiprem.mycoloapp.core.domain.validator.error.PasswordValidationError
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.validation_email_empty
import mycolowepapp.shared.generated.resources.validation_email_invalid_format
import mycolowepapp.shared.generated.resources.validation_email_too_long
import mycolowepapp.shared.generated.resources.validation_password_does_not_match
import mycolowepapp.shared.generated.resources.validation_password_empty
import mycolowepapp.shared.generated.resources.validation_password_no_digit
import mycolowepapp.shared.generated.resources.validation_password_no_lowercase
import mycolowepapp.shared.generated.resources.validation_password_no_special_char
import mycolowepapp.shared.generated.resources.validation_password_no_uppercase
import mycolowepapp.shared.generated.resources.validation_password_too_long
import mycolowepapp.shared.generated.resources.validation_password_too_short

fun FieldValidationError.toUiText() = when (this) {
    is EmailValidationError -> when (val error = this as EmailValidationError) {
        is EmailValidationError.Empty
            -> UiText.Resource(Res.string.validation_email_empty)

        EmailValidationError.InvalidFormat
            -> UiText.Resource(Res.string.validation_email_invalid_format)

        EmailValidationError.TooLong
            -> UiText.Resource(Res.string.validation_email_too_long)

        is EmailValidationError.ServerError -> UiText.DynamicString(error.message)
    }

    is PasswordValidationError -> when (val error = this as PasswordValidationError) {
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
            -> UiText.DynamicString(error.message)
    }
    else -> UiText.DynamicString("Oups Erreur non prise en charge ")
}