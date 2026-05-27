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
import empire.digiprem.mycoloapp.features.auth.domain.usecase.ModifierMotDePasseUseCase
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

class ModifierMotDePasseViewModel(
    private val modifierMotDePasseUseCase: ModifierMotDePasseUseCase,
    private val alert: AlertSender,
) : ViewModel() {

    private val _state = MutableStateFlow(ModifierMotDePasseState())
    private var isStarted = false

    val state = _state.onStart {
        if (!isStarted) {
            observeFields()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ModifierMotDePasseState()
    )

    private val _eventChannel = Channel<ModifierMotDePasseEvent>()
    val events = _eventChannel.receiveAsFlow()

    private fun observeFields() {
        combine(
            snapshotFlow { _state.value.nouveauMotDePasseState.text.isNotEmpty() }.distinctUntilChanged(),
            snapshotFlow { _state.value.confirmationMotDePasseState.text.isNotEmpty() }.distinctUntilChanged(),
        ) { hasNouveauMdp, hasConfirmation ->
            _state.update { it.copy(userCanSend = hasNouveauMdp && hasConfirmation) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ModifierMotDePasseAction) {
        when (action) {
            ModifierMotDePasseAction.OnConfirmerClick -> modifierMotDePasse()
            ModifierMotDePasseAction.OnToggleNouveauMotDePasseVisibility ->
                _state.update { it.copy(isNouveauMotDePasseVisible = !it.isNouveauMotDePasseVisible) }
            ModifierMotDePasseAction.OnToggleConfirmationMotDePasseVisibility ->
                _state.update { it.copy(isConfirmationMotDePasseVisible = !it.isConfirmationMotDePasseVisible) }
            ModifierMotDePasseAction.OnEffacerErreur ->
                _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun modifierMotDePasse() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            modifierMotDePasseUseCase(
                newPassword = _state.value.nouveauMotDePasseState.text.toString(),
                confirmPassword = _state.value.confirmationMotDePasseState.text.toString(),
            ).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(ModifierMotDePasseEvent.OnMotDePasseModifie)
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
