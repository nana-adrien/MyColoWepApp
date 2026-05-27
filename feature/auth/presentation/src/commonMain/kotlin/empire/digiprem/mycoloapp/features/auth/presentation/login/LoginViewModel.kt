package empire.digiprem.mycoloapp.features.auth.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.design_system.extension.toUiText
import empire.digiprem.mycoloapp.core.domain.error.AlertEvent
import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.core.domain.validator.error.EmailValidationError
import empire.digiprem.mycoloapp.core.extension.toUiText
import empire.digiprem.mycoloapp.features.auth.domain.usecase.AdminLoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminLoginViewModel(
    private val adminLoginUseCase: AdminLoginUseCase,
    private val alert: AlertSender
) : ViewModel() {

    private val _state = MutableStateFlow(AdminLoginState())
    var isStarted = false
    val state = _state.onStart {
        if (!isStarted) {
            observeValidateField()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AdminLoginState()
    )

    private val _eventChannel = Channel<AdminLoginEvent>()
    val events = _eventChannel.receiveAsFlow()


    val emailValidateFlow = snapshotFlow { state.value.emailTextFieldState.text.isNotEmpty() }
        .distinctUntilChanged()

    val passwordValidateFlow = snapshotFlow { state.value.passwordTextFieldState.text.isNotEmpty() }
        .distinctUntilChanged()


    fun observeValidateField() {
        combine(
            emailValidateFlow,
            passwordValidateFlow
        ) { isEmailValid, isPasswordValid ->
            _state.update {
                it.copy(
                    userCanSend = isEmailValid && isPasswordValid,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: AdminLoginAction) {
        when (action) {
            is AdminLoginAction.OnLoginClick -> login()
            is AdminLoginAction.OnLeanErrorMessageClick -> _state.update { it.copy(errorMessage = null) }
            else -> {}
        }
    }

    private fun login() {
        val current = _state.value
        val validationError = when {
            //  current.email.isBlank()    -> "L'adresse email est requise"
            // current.password.isBlank() -> "Le mot de passe est requis"
            else -> null
        }
        if (validationError != null) {
            //   _state.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            adminLoginUseCase(
                email = current.emailTextFieldState.text.toString().trim(),
                password = current.passwordTextFieldState.text.toString().trim(),
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _eventChannel.send(AdminLoginEvent.OnLoginSuccess)
                }
                .onFailure { error ->
                    val errorMessage= when (error) {
                        is DataError.Remote.InvalidOperation -> error.toUiText()
                        is EmailValidationError -> error.toUiText()
                        is DataError.Remote -> {
                            alert.sendAlert(AlertEvent.Error(error.toUiText()))
                            null
                        }
                        else -> {
                            null
                        }
                    }
                    _state.update { it.copy(isLoading = false, errorMessage = errorMessage) }

                }
        }
    }
}
