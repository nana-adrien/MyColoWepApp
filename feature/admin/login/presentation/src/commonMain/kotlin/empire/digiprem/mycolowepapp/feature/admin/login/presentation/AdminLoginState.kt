package empire.digiprem.mycolowepapp.feature.admin.login.presentation

import androidx.compose.foundation.text.input.TextFieldState
import empire.digiprem.mycolowepapp.core.domain.util.UiText

data class AdminLoginState(
    val emailTextFieldState: TextFieldState=TextFieldState(),
    val passwordTextFieldState: TextFieldState=TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userCanSend: Boolean = false,
    val errorMessage: UiText? = null,    // inline (validation)
)
