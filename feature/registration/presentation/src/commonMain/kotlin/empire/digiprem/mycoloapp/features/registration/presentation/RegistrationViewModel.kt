package empire.digiprem.mycoloapp.features.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.core.extension.toUiText
import empire.digiprem.mycoloapp.features.registration.domain.error.RegistrationError
import empire.digiprem.mycoloapp.features.registration.domain.model.RegistrationForm
import empire.digiprem.mycoloapp.features.registration.domain.usecase.RegisterParticipantUseCase
import empire.digiprem.mycoloapp.features.registration.domain.usecase.ValidateSecurityCodeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val validateSecurityCode: ValidateSecurityCodeUseCase,
    private val registerParticipant: RegisterParticipantUseCase
) : ViewModel() {

    var isStarted = false
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.onStart {
        if (!isStarted) {
            observeValidateTextField()
            isStarted = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = RegistrationState()
    )
    private val _eventChannel = Channel<RegistrationEvent>()
    val events = _eventChannel.receiveAsFlow()

    val fullNameValidateFlow = _state
        .map { it.fullNameTextFieldState.text.toString() }
        .map { text -> text.length >= 5 }
        .distinctUntilChanged()

    val birthDateValidateFlow = _state
        .map { it.birthDate != null }
        .distinctUntilChanged()

    val familyNameValidateFlow = _state
        .map { it.familyNameTextFieldState.text.toString() }
        .map { text -> text.length >= 5 }
        .distinctUntilChanged()

    val securityCodeValidateFlow = _state
        .map { it.securityCodeTextFieldState.text.toString() }
        .map { it.isNotEmpty() }
        .distinctUntilChanged()

    val jobStatusValidateFlow = _state
        .map { it.educationLevel != null }
        .distinctUntilChanged()

    val genreValidateFlow = _state
        .map { it.genre != null }
        .distinctUntilChanged()


    fun observeValidateTextField() {
        /*combine(
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
        }
            .launchIn(viewModelScope)*/
    }

    fun onAction(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.OnSubmitClick -> submit()
            is RegistrationAction.OnClearError -> _state.update { it.copy(errorMessage = null, codeError = null) }
            is RegistrationAction.OnEducationLevelChange -> _state.update { it.copy(educationLevel = action.educationLevel) }
            is RegistrationAction.OnGenreChange -> _state.update { it.copy(genre = action.genre) }
            is RegistrationAction.OnBirthDateChange -> _state.update { it.copy(birthDate = action.birthDate) }
            else -> Unit
        }
    }

    private fun submit() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val registrationForm = RegistrationForm(
                fullName = state.value.fullNameTextFieldState.text.toString(),
                securityCode = state.value.fullNameTextFieldState.text.toString(),
                educationLevel = state.value.educationLevel,
                familyName = state.value.fullNameTextFieldState.text.toString(),
                birthDate = state.value.fullNameTextFieldState.text.toString(),
            )
            registerParticipant(registrationForm)
                .onSuccess { referenceNumber ->
                    _state.update { it.copy(isLoading = false) }
                    _eventChannel.send(RegistrationEvent.OnRegistrationSuccess(referenceNumber))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
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
