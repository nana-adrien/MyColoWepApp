package empire.digiprem.mycoloapp.features.auth.presentation.verify_identity

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText

data class VerifyOtpState(
    val otpState: TextFieldState = TextFieldState(),
    val email: String = "",
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val isResend: Boolean = false,
    val errorMessage: UiText? = null,
)
