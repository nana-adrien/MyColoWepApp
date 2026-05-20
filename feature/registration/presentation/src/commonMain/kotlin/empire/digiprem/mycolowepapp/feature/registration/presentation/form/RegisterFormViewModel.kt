package empire.digiprem.mycolowepapp.feature.registration.presentation.form

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.core.domain.util.UiText
import empire.digiprem.mycolowepapp.core.domain.util.onFailure
import empire.digiprem.mycolowepapp.core.domain.util.onSuccess
import empire.digiprem.mycolowepapp.feature.registration.domain.error.RegistrationError
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import empire.digiprem.mycolowepapp.feature.registration.domain.usecase.RegisterParticipantUseCase
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationAction
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationEvent
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterFormViewModel (
    private val registerParticipant: RegisterParticipantUseCase
): ViewModel() {

    var isStarted = false
    private val _state = MutableStateFlow(RegisterFormState())
    val state = _state.onStart {
        if (!isStarted) {
            observeValidateTextField()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = RegisterFormState()
    )
    private val _eventChannel = Channel<RegisterFormEvent>()
    val events = _eventChannel.receiveAsFlow()

    val fullNameValidateFlow =snapshotFlow{ state.value.fullNameTextFieldState.text }
        .map { text -> text.length >= 5 }
        .distinctUntilChanged()

    val birthDateValidateFlow = _state
        .map { it.birthDate != null }
        .distinctUntilChanged()

    val familyNameValidateFlow = snapshotFlow{ state.value.familyNameTextFieldState.text }
        .map { text -> text.length >= 5 }
        .distinctUntilChanged()

    val securityCodeValidateFlow = snapshotFlow{ state.value.securityCodeTextFieldState.text}
        .map { it.isNotEmpty() }
        .distinctUntilChanged()

    val jobStatusValidateFlow = _state
        .map { it.jobStatus != null }
        .distinctUntilChanged()

    val genreValidateFlow = _state
        .map { it.genre != null }
        .distinctUntilChanged()


    fun observeValidateTextField() {
        combine(
            fullNameValidateFlow,
            birthDateValidateFlow,
            jobStatusValidateFlow,
            genreValidateFlow,
            familyNameValidateFlow,
        ) { fullName, birthDate, jobStatus, genre, familyName ->
            fullName && birthDate && jobStatus && genre && familyName
        }.combine(
            securityCodeValidateFlow,
        ) { isValid, securityCode ->
            _state.update {
                it.copy(
                    userCanSendFrom = isValid && securityCode,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterFormAction) {
        when (action) {
            is RegisterFormAction.OnSubmitClick -> submit()
            is RegisterFormAction.OnClearError -> _state.update { it.copy(errorMessage = null, codeError = null) }
            is RegisterFormAction.OnJobStatusChange -> _state.update { it.copy(jobStatus = action.status) }
            is RegisterFormAction.OnGenreChange -> _state.update { it.copy(genre = action.genre) }
            is RegisterFormAction.OnBirthDateChange -> _state.update { it.copy(birthDate = action.birthDate) }
            else -> Unit
        }
    }

    private fun submit() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val registrationForm = RegistrationForm(
                    fullName = state.value.fullNameTextFieldState.text.toString(),
                    securityCode = state.value.securityCodeTextFieldState.text.toString(),
                    jobStatus = state.value.jobStatus,
                    familyName = state.value.familyNameTextFieldState.text.toString(),
                    age = state.value.birthDate.toString(),
                )
                registerParticipant(registrationForm)
                    .onSuccess { referenceNumber ->
                        _state.update { it.copy(isLoading = false) }
                        _eventChannel.send(RegisterFormEvent.OnSuccess)
                    }
                    .onFailure { error ->
                        _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
                    }

            }catch (e: Exception) {
                // ✅ isLoading désactivé même en cas d'exception inattendue
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = UiText.DynamicString(e.message ?: "Erreur inconnue")
                    )
                }
            }

        }
    }
}

private fun RegistrationError.toUiText(): UiText = when (this) {
    RegistrationError.InvalidSecurityCode -> UiText.DynamicString("Code de sécurité invalide ou désactivé")
    RegistrationError.AlreadyRegistered -> UiText.DynamicString("Un participant avec ce nom et cet âge est déjà inscrit")
    RegistrationError.NetworkError -> UiText.DynamicString("Connexion impossible. Vérifiez votre réseau.")
    is RegistrationError.Unknown -> UiText.DynamicString("Erreur : $message")
}