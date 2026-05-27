package empire.digiprem.mycoloapp.features.auth.presentation.fogot_password

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText

data class ChangePasswordState(
    val newPasswordState: TextFieldState = TextFieldState(),
    val confirmPasswordState: TextFieldState = TextFieldState(),
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val errorMessage: UiText? = null,
)
