package empire.digiprem.mycoloapp.features.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText

data class ReinitialiserMotDePasseState(
    val emailState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val emailEnvoye: Boolean = false,
    val errorMessage: UiText? = null,
)
