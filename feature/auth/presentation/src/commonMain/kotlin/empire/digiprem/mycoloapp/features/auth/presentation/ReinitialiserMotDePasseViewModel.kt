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

class ReinitialiserMotDePasseViewModel(
    private val reinitialiserMotDePasseUseCase: ReinitialiserMotDePasseUseCase,
    private val alert: AlertSender,
) : ViewModel() {

    private val _state = MutableStateFlow(ReinitialiserMotDePasseState())
    private var isStarted = false

    val state = _state.onStart {
        if (!isStarted) {
            observeEmail()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ReinitialiserMotDePasseState()
    )

    private val _eventChannel = Channel<ReinitialiserMotDePasseEvent>()
    val events = _eventChannel.receiveAsFlow()

    private fun observeEmail() {
        snapshotFlow { _state.value.emailState.text.isNotEmpty() }
            .distinctUntilChanged()
            .onEach { hasEmail -> _state.update { it.copy(userCanSend = hasEmail) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ReinitialiserMotDePasseAction) {
        when (action) {
            ReinitialiserMotDePasseAction.OnEnvoyerLienClick -> envoyerLien()
            ReinitialiserMotDePasseAction.OnEffacerErreur -> _state.update { it.copy(errorMessage = null) }
            ReinitialiserMotDePasseAction.OnNaviguerVersVerification -> {
                viewModelScope.launch {
                    _eventChannel.send(
                        ReinitialiserMotDePasseEvent.OnNaviguerVersVerification(
                            _state.value.emailState.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun envoyerLien() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            reinitialiserMotDePasseUseCase(
                email = _state.value.emailState.text.toString().trim()
            ).onSuccess {
                _state.update { it.copy(isLoading = false, emailEnvoye = true) }
                _eventChannel.send(ReinitialiserMotDePasseEvent.OnLienEnvoye)
            }.onFailure { error ->
                val message = when (error) {
                    AuthPasswordError.InvalidEmail -> UiText.DynamicString("Adresse email invalide")
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
