package empire.digiprem.mycolowepapp.feature.registration.domain.error

import empire.digiprem.mycolowepapp.core.domain.util.ResultError

sealed interface RegistrationError : ResultError {
    data object InvalidSecurityCode : RegistrationError  // code inexistant ou désactivé
    data object AlreadyRegistered : RegistrationError    // même nom + même âge en BD
    data object NetworkError : RegistrationError
    data class Unknown(val message: String) : RegistrationError
}
