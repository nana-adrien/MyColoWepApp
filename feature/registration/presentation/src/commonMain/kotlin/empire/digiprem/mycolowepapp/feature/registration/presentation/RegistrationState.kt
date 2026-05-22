package empire.digiprem.mycolowepapp.feature.registration.presentation

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycolowepapp.core.domain.util.UiText
import empire.digiprem.mycolowepapp.feature.registration.domain.model.Genre
import empire.digiprem.mycolowepapp.feature.registration.domain.model.EducationLevel
import kotlinx.datetime.LocalDate

data class RegistrationState(
    val fullNameTextFieldState: TextFieldState = TextFieldState(),
    val birthDate: LocalDate?=null,
    val educationLevel: EducationLevel? = null,
    val genre: Genre?= null,
    val familyNameTextFieldState: TextFieldState =TextFieldState(),
    val securityCodeTextFieldState: TextFieldState =TextFieldState(),
    // Étape 1 — validation du code
    val isCodeValidating: Boolean = false,
    val isCodeValidated: Boolean = false,
    val codeError: String? = null,
    // Étape 2 — soumission du formulaire
    val userCanSendFrom:Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
