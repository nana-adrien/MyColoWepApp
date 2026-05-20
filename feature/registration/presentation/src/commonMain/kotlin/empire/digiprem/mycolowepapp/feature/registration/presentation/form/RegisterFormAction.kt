package empire.digiprem.mycolowepapp.feature.registration.presentation.form

import empire.digiprem.mycolowepapp.feature.registration.domain.model.Genre
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationAction
import kotlinx.datetime.LocalDate

sealed interface RegisterFormAction {
    data class OnJobStatusChange(val status: JobStatus) : RegisterFormAction
    data class OnBirthDateChange(val birthDate: LocalDate?) : RegisterFormAction
    data class OnGenreChange(val genre: Genre) : RegisterFormAction
    data object OnSubmitClick : RegisterFormAction

    data object OnClearError : RegisterFormAction
}