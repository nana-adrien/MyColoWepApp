package empire.digiprem.mycolowepapp.feature.registration.presentation

import empire.digiprem.mycolowepapp.feature.registration.domain.model.EducationLevel
import empire.digiprem.mycolowepapp.feature.registration.domain.model.Genre
import kotlinx.datetime.LocalDate

sealed interface RegistrationAction {
    data class OnSecurityCodeChange(val value: String) : RegistrationAction
    data object OnValidateCodeClick : RegistrationAction

    data class OnFullNameChange(val value: String) : RegistrationAction
    data class OnFamilyNameChange(val value: String) : RegistrationAction
    data class OnAgeChange(val value: String) : RegistrationAction
    data class OnEducationLevelChange(val educationLevel: EducationLevel) : RegistrationAction
    data class OnBirthDateChange(val birthDate: LocalDate?) : RegistrationAction
    data class OnGenreChange(val genre: Genre) : RegistrationAction
    data object OnSubmitClick : RegistrationAction

    data object OnClearError : RegistrationAction
}
