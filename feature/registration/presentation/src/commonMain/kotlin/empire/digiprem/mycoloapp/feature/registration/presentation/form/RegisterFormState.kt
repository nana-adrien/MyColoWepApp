package empire.digiprem.mycoloapp.feature.registration.presentation.form

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.feature.registration.domain.model.Genre
import empire.digiprem.mycoloapp.feature.registration.domain.model.EducationLevel
import kotlinx.datetime.LocalDate

data class RegisterFormState(
    val fullNameTextFieldState: TextFieldState = TextFieldState(),
    val familyNameTextFieldState: TextFieldState =TextFieldState(),
    val securityCodeTextFieldState: TextFieldState =TextFieldState(),
    val cityTextFieldState: TextFieldState =TextFieldState(),
    val birthDate: LocalDate?=null,
    val educationLevel: EducationLevel? = null,
    val genre: Genre?= null,
    // Étape 1 — validation du code
    val isCodeValidating: Boolean = false,
    val isCodeValidated: Boolean = false,
    val codeError: String? = null,
    // Étape 2 — soumission du formulaire
    val userCanSendFrom:Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)