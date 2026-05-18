package empire.digiprem.mycolowepapp.feature.registration.presentation

import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus

sealed interface RegistrationAction {
    data class OnFullNameChange(val value: String) : RegistrationAction
    data class OnAgeChange(val value: String) : RegistrationAction
    data class OnFamilyNameChange(val value: String) : RegistrationAction
    data class OnJobStatusChange(val status: JobStatus) : RegistrationAction
    data class OnSecurityCodeChange(val value: String) : RegistrationAction
    data object OnSubmitClick : RegistrationAction
    data object OnClearError : RegistrationAction
}
