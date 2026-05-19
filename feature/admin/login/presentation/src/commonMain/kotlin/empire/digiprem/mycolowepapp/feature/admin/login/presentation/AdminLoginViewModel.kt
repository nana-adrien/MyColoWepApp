package empire.digiprem.mycolowepapp.feature.admin.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.core.domain.util.onFailure
import empire.digiprem.mycolowepapp.core.domain.util.onSuccess
import empire.digiprem.mycolowepapp.feature.admin.login.domain.error.AdminAuthError
import empire.digiprem.mycolowepapp.feature.admin.login.domain.usecase.AdminLoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminLoginViewModel(
    private val adminLoginUseCase: AdminLoginUseCase
) : ViewModel() {

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
            is AdminLoginAction.OnLoginClick    -> login()
            is AdminLoginAction.OnDismissErrorDialog -> _state.update { it.copy(errorDialog = null) }
        }
    }

    private fun login() {
        val current = _state.value
        val validationError = when {
            current.email.isBlank()    -> "L'adresse email est requise"
            current.password.isBlank() -> "Le mot de passe est requis"
            else                       -> null
        }
        if (validationError != null) {
            _state.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, errorDialog = null) }
            adminLoginUseCase(
                email    = current.email.trim(),
                password = current.password
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _eventChannel.send(AdminLoginEvent.OnLoginSuccess)
                }
                .onFailure { error ->
                    val message = when (error) {
                        AdminAuthError.InvalidCredentials -> "Email ou mot de passe incorrect"
                        AdminAuthError.NetworkError       -> "Connexion impossible. Vérifiez votre réseau."
                        is AdminAuthError.Unknown         -> "Erreur : ${error.message}"
                    }
                    _state.update { it.copy(isLoading = false, errorDialog = message) }
                }
        }
    }
}
