package empire.digiprem.mycolowepapp.feature.admin.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminLoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(AdminLoginState())
    val state: StateFlow<AdminLoginState> = _state.asStateFlow()

    private val _eventChannel = Channel<AdminLoginEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: AdminLoginAction) {
        when (action) {
            is AdminLoginAction.OnEmailChange -> _state.update {
                it.copy(email = action.value, errorMessage = null)
            }
            is AdminLoginAction.OnPasswordChange -> _state.update {
                it.copy(password = action.value, errorMessage = null)
            }
            is AdminLoginAction.OnTogglePasswordVisibility -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            is AdminLoginAction.OnLoginClick -> login()
        }
    }

    private fun login() {
        val state = _state.value
        val error = when {
            state.email.isBlank() -> "L'adresse email est requise"
            state.password.isBlank() -> "Le mot de passe est requis"
            else -> null
        }
        if (error != null) {
            _state.update { it.copy(errorMessage = error) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            _state.update { it.copy(isLoading = false) }
            _eventChannel.send(AdminLoginEvent.OnLoginSuccess)
        }
    }
}
