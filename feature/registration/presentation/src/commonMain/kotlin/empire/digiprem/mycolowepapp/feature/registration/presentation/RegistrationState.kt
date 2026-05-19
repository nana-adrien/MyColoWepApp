package empire.digiprem.mycolowepapp.feature.registration.presentation

import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm

data class RegistrationState(
    val form: RegistrationForm = RegistrationForm(),
    // Étape 1 — validation du code
    val isCodeValidating: Boolean = false,
    val isCodeValidated: Boolean = false,
    val codeError: String? = null,
    // Étape 2 — soumission du formulaire
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
