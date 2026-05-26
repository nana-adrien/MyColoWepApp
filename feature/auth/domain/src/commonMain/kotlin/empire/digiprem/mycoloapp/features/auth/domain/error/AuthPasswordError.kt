package empire.digiprem.mycoloapp.features.auth.domain.error

import empire.digiprem.mycoloapp.core.domain.util.ResultError

sealed interface AuthPasswordError : ResultError {
    data object PasswordMismatch : AuthPasswordError
    data object PasswordTooShort : AuthPasswordError
    data object InvalidEmail : AuthPasswordError
    data object InvalidOtp : AuthPasswordError
}
