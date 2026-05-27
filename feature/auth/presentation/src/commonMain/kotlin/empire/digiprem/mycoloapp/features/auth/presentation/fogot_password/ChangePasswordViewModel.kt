package empire.digiprem.mycoloapp.features.auth.presentation.fogot_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.error.AlertEvent
import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.core.extension.toUiText
import empire.digiprem.mycoloapp.features.auth.domain.error.AuthPasswordError
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ChangePasswordUseCase
import empire.digiprem.mycoloapp.core.domain.util.UiText
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

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val alert: AlertSender,
) : ViewModel() {

    private val _state = MutableStateFlow(ChangePasswordState())
    private var isStarted = false

    val state = _state.onStart {
        if (!isStarted) {
            observeFields()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ChangePasswordState()
    )

    private val _eventChannel = Channel<ChangePasswordEvent>()
    val events = _eventChannel.receiveAsFlow()

    private fun observeFields() {
        combine(
            snapshotFlow { _state.value.newPasswordState.text.isNotEmpty() }.distinctUntilChanged(),
            snapshotFlow { _state.value.confirmPasswordState.text.isNotEmpty() }.distinctUntilChanged(),
        ) { hasNewPassword, hasConfirmation ->
            _state.update { it.copy(userCanSend = hasNewPassword && hasConfirmation) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ChangePasswordAction) {
        when (action) {
            ChangePasswordAction.OnConfirmClick -> changePassword()
            ChangePasswordAction.OnToggleNewPasswordVisibility ->
                _state.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
            ChangePasswordAction.OnToggleConfirmPasswordVisibility ->
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            ChangePasswordAction.OnClearError ->
                _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            changePasswordUseCase(
                newPassword = _state.value.newPasswordState.text.toString(),
                confirmPassword = _state.value.confirmPasswordState.text.toString(),
            ).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(ChangePasswordEvent.OnPasswordChanged)
            }.onFailure { error ->
                val message = when (error) {
                    AuthPasswordError.PasswordMismatch -> UiText.DynamicString("Les mots de passe ne correspondent pas")
                    AuthPasswordError.PasswordTooShort -> UiText.DynamicString("Le mot de passe doit contenir au moins 6 caractères")
                    is DataError.Remote -> {
                        alert.sendAlert(AlertEvent.Error(error.toUiText()))
                        null
                    }
                    else -> UiText.DynamicString("Une erreur est survenue")
                }
                _state.update { it.copy(isLoading = false, errorMessage = message) }
            }
        }
    }
}
