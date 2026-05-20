package empire.digiprem.mycolowepapp.feature.registration.presentation

import empire.digiprem.mycolowepapp.feature.registration.domain.model.Genre
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import kotlinx.datetime.LocalDate

sealed interface RegistrationAction {
    // Étape 1 : code de sécurité
    data class OnSecurityCodeChange(val value: String) : RegistrationAction
    data object OnValidateCodeClick : RegistrationAction  // bouton "Valider le code"

    // Étape 2 : formulaire
    data class OnFullNameChange(val value: String) : RegistrationAction
    data class OnFamilyNameChange(val value: String) : RegistrationAction
    data class OnAgeChange(val value: String) : RegistrationAction
    data class OnJobStatusChange(val status: JobStatus) : RegistrationAction
    data class OnBirthDateChange(val birthDate: LocalDate?) : RegistrationAction
    data class OnGenreChange(val genre: Genre) : RegistrationAction
    data object OnSubmitClick : RegistrationAction

    data object OnClearError : RegistrationAction
}
