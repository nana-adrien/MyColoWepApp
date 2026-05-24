package empire.digiprem.mycoloapp.feature.admin.login.presentation

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycoloapp.core.domain.util.UiText

data class AdminLoginState(
    val emailTextFieldState: TextFieldState=TextFieldState(),
    val passwordTextFieldState: TextFieldState=TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val errorMessage: UiText? = null,    // inline (validation)
)
