package empire.digiprem.mycolowepapp.feature.registration.presentation

import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm

data class RegistrationState(
    val form: RegistrationForm = RegistrationForm(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
