package empire.digiprem.mycoloapp.features.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText

data class ModifierMotDePasseState(
    val nouveauMotDePasseState: TextFieldState = TextFieldState(),
    val confirmationMotDePasseState: TextFieldState = TextFieldState(),
    val isNouveauMotDePasseVisible: Boolean = false,
    val isConfirmationMotDePasseVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val errorMessage: UiText? = null,
)
