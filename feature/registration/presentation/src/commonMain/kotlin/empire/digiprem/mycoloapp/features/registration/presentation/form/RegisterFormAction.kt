package empire.digiprem.mycoloapp.features.registration.presentation.form

import empire.digiprem.mycoloapp.features.registration.domain.model.EducationLevel
import empire.digiprem.mycoloapp.features.registration.domain.model.Genre
import kotlinx.datetime.LocalDate

sealed interface RegisterFormAction {
    data class OnEducationLevelChange(val educationLevel: EducationLevel) : RegisterFormAction
    data class OnBirthDateChange(val birthDate: LocalDate?) : RegisterFormAction
    data class OnGenreChange(val genre: Genre) : RegisterFormAction
    data object OnSubmitClick : RegisterFormAction
    data object OnClearError : RegisterFormAction
}
