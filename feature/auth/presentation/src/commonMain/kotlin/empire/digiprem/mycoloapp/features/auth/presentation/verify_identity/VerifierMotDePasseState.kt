package empire.digiprem.mycoloapp.features.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText

data class VerifierMotDePasseState(
    val otpState: TextFieldState = TextFieldState(),
    val email: String = "",
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val isRenvoi: Boolean = false,
    val errorMessage: UiText? = null,
)
