package empire.digiprem.mycoloapp.features.auth.presentation.verify_identity

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
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ResetPasswordUseCase
import empire.digiprem.mycoloapp.features.auth.domain.usecase.VerifyOtpUseCase
import empire.digiprem.mycoloapp.core.domain.util.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyOtpViewModel(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val alert: AlertSender,
) : ViewModel() {

    private val _state = MutableStateFlow(VerifyOtpState())
    private var isStarted = false

    val state = _state.onStart {
        if (!isStarted) {
            observeOtp()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = VerifyOtpState()
    )

    private val _eventChannel = Channel<VerifyOtpEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun initEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun observeOtp() {
        snapshotFlow { _state.value.otpState.text.length == 6 }
            .distinctUntilChanged()
            .onEach { isValid -> _state.update { it.copy(userCanSend = isValid) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: VerifyOtpAction) {
        when (action) {
            VerifyOtpAction.OnVerifyClick -> verifyOtp()
            VerifyOtpAction.OnResendCodeClick -> resendCode()
            VerifyOtpAction.OnClearError -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            verifyOtpUseCase(
                email = _state.value.email,
                token = _state.value.otpState.text.toString(),
            ).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(VerifyOtpEvent.OnOtpVerified)
            }.onFailure { error ->
                val message = when (error) {
                    AuthPasswordError.InvalidOtp -> UiText.DynamicString("Code OTP invalide (6 chiffres requis)")
                    is DataError.Remote -> {
                        alert.sendAlert(AlertEvent.Error(error.toUiText()))
                        null
                    }
                    else -> UiText.DynamicString("Code incorrect, veuillez réessayer")
                }
                _state.update { it.copy(isLoading = false, errorMessage = message) }
            }
        }
    }

    private fun resendCode() {
        val email = _state.value.email
        if (email.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isResend = true) }
            resetPasswordUseCase(email)
                .onSuccess { _state.update { it.copy(isResend = false) } }
                .onFailure { _state.update { it.copy(isResend = false) } }
        }
    }
}
