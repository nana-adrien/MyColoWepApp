package empire.digiprem.mycolowepapp.feature.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.core.domain.util.onFailure
import empire.digiprem.mycolowepapp.core.domain.util.onSuccess
import empire.digiprem.mycolowepapp.feature.registration.domain.error.RegistrationError
import empire.digiprem.mycolowepapp.feature.registration.domain.usecase.RegisterParticipantUseCase
import empire.digiprem.mycolowepapp.feature.registration.domain.usecase.ValidateSecurityCodeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val validateSecurityCode: ValidateSecurityCodeUseCase,
    private val registerParticipant: RegisterParticipantUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private val _eventChannel = Channel<RegistrationEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: RegistrationAction) {
        when (action) {
            // Étape 1 — code de sécurité
            is RegistrationAction.OnSecurityCodeChange -> _state.update {
                it.copy(form = it.form.copy(securityCode = action.value), codeError = null)
            }
            is RegistrationAction.OnValidateCodeClick -> validateCode()

            // Étape 2 — formulaire
            is RegistrationAction.OnFullNameChange -> _state.update {
                it.copy(form = it.form.copy(fullName = action.value), errorMessage = null)
            }
            is RegistrationAction.OnFamilyNameChange -> _state.update {
                it.copy(form = it.form.copy(familyName = action.value), errorMessage = null)
            }
            is RegistrationAction.OnAgeChange -> {
                if (action.value.all { c -> c.isDigit() } && action.value.length <= 3) {
                    _state.update { it.copy(form = it.form.copy(age = action.value), errorMessage = null) }
                }
            }
            is RegistrationAction.OnJobStatusChange -> _state.update {
                it.copy(form = it.form.copy(jobStatus = action.status), errorMessage = null)
            }
            is RegistrationAction.OnSubmitClick -> submit()
            is RegistrationAction.OnClearError -> _state.update { it.copy(errorMessage = null, codeError = null) }
        }
    }

    private fun validateCode() {
        val code = _state.value.form.securityCode.trim()
        if (code.isBlank()) {
            _state.update { it.copy(codeError = "Le code de sécurité est requis") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isCodeValidating = true, codeError = null) }
            validateSecurityCode(code)
                .onSuccess { isValid ->
                    if (isValid) {
                        _state.update { it.copy(isCodeValidating = false, isCodeValidated = true) }
                    } else {
                        _state.update { it.copy(isCodeValidating = false, codeError = "Code invalide ou désactivé") }
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isCodeValidating = false, codeError = error.toMessage()) }
                }
        }
    }

    private fun submit() {
        val form = _state.value.form
        val validationError = when {
            form.fullName.isBlank()   -> "Le prénom est requis"
            form.familyName.isBlank() -> "Le nom de famille est requis"
            form.age.isBlank() || form.age.toIntOrNull() == null -> "Veuillez entrer un âge valide"
            form.jobStatus == null    -> "Veuillez sélectionner votre statut professionnel"
            else                      -> null
        }
        if (validationError != null) {
            _state.update { it.copy(errorMessage = validationError) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            registerParticipant(form)
                .onSuccess { referenceNumber ->
                    _state.update { it.copy(isLoading = false) }
                    _eventChannel.send(RegistrationEvent.OnRegistrationSuccess(referenceNumber))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toMessage()) }
                }
        }
    }
}

private fun RegistrationError.toMessage(): String = when (this) {
    RegistrationError.InvalidSecurityCode -> "Code de sécurité invalide ou désactivé"
    RegistrationError.AlreadyRegistered   -> "Un participant avec ce nom et cet âge est déjà inscrit"
    RegistrationError.NetworkError        -> "Connexion impossible. Vérifiez votre réseau."
    is RegistrationError.Unknown          -> "Erreur : $message"
}
