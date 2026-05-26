package empire.digiprem.mycoloapp.features.auth.presentation

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
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ReinitialiserMotDePasseUseCase
import empire.digiprem.mycoloapp.features.auth.domain.usecase.VerifierMotDePasseUseCase
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

class VerifierMotDePasseViewModel(
    private val verifierMotDePasseUseCase: VerifierMotDePasseUseCase,
    private val reinitialiserMotDePasseUseCase: ReinitialiserMotDePasseUseCase,
    private val alert: AlertSender,
) : ViewModel() {

    private val _state = MutableStateFlow(VerifierMotDePasseState())
    private var isStarted = false

    val state = _state.onStart {
        if (!isStarted) {
            observeOtp()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = VerifierMotDePasseState()
    )

    private val _eventChannel = Channel<VerifierMotDePasseEvent>()
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

    fun onAction(action: VerifierMotDePasseAction) {
        when (action) {
            VerifierMotDePasseAction.OnVerifierClick -> verifierOtp()
            VerifierMotDePasseAction.OnRenvoyerCodeClick -> renvoyerCode()
            VerifierMotDePasseAction.OnEffacerErreur -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun verifierOtp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            verifierMotDePasseUseCase(
                email = _state.value.email,
                token = _state.value.otpState.text.toString(),
            ).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(VerifierMotDePasseEvent.OnOtpVerifie)
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

    private fun renvoyerCode() {
        val email = _state.value.email
        if (email.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isRenvoi = true) }
            reinitialiserMotDePasseUseCase(email)
                .onSuccess { _state.update { it.copy(isRenvoi = false) } }
                .onFailure { _state.update { it.copy(isRenvoi = false) } }
        }
    }
}
