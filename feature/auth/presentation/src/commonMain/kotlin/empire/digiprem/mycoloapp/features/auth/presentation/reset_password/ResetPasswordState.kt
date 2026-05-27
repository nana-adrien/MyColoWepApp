package empire.digiprem.mycoloapp.features.auth.presentation.reset_password

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText

data class ResetPasswordState(
    val emailState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val emailSent: Boolean = false,
    val errorMessage: UiText? = null,
)
