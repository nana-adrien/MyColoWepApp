package empire.digiprem.mycoloapp.feature.registration.presentation.form

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.error.AlertEvent
import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.core.domain.util.onFailure
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.core.extension.toUiText
import empire.digiprem.mycoloapp.feature.registration.domain.error.RegistrationError
import empire.digiprem.mycoloapp.feature.registration.domain.model.RegistrationForm
import empire.digiprem.mycoloapp.feature.registration.domain.usecase.RegisterParticipantUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
    private val registerParticipant: RegisterParticipantUseCase,
    private val alert: AlertSender
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
        .map { text -> text.length >= 3 }
        .distinctUntilChanged()

    val birthDateValidateFlow = _state
        .map { it.birthDate != null }
        .distinctUntilChanged()

    val familyNameValidateFlow = snapshotFlow{ state.value.familyNameTextFieldState.text }
        .map { text -> text.length >= 3 }
        .distinctUntilChanged()

    val securityCodeValidateFlow = snapshotFlow{ state.value.securityCodeTextFieldState.text}
        .map { it.isNotEmpty() }
        .distinctUntilChanged()
    val cityValidateFlow = snapshotFlow{ state.value.cityTextFieldState.text}
        .map { it.isNotEmpty() }
        .distinctUntilChanged()

    val educationLevelValidateFlow = _state
        .map { it.educationLevel != null }
        .distinctUntilChanged()

    val genreValidateFlow = _state
        .map { it.genre != null }
        .distinctUntilChanged()


    fun observeValidateTextField() {
        combine(
            fullNameValidateFlow,
            birthDateValidateFlow,
            educationLevelValidateFlow,
            genreValidateFlow,
            familyNameValidateFlow,
            securityCodeValidateFlow,
            cityValidateFlow,
        ) /*{ fullName, birthDate, educationLevel, genre, familyName ->
            fullName && birthDate && educationLevel && genre && familyName
        }.combine(
            securityCodeValidateFlow,cityValidateFlow,
        )*/ { values-> //isValid, securityCode,city ->
            _state.update {
                it.copy(
                    userCanSendFrom = values.all { it }
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterFormAction) {
        when (action) {
            is RegisterFormAction.OnSubmitClick -> submit()
            is RegisterFormAction.OnClearError -> _state.update { it.copy(errorMessage = null, codeError = null) }
            is RegisterFormAction.OnEducationLevelChange -> _state.update { it.copy(educationLevel = action.educationLevel) }
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
                    educationLevel = state.value.educationLevel,
                    familyName = state.value.familyNameTextFieldState.text.toString(),
                    city = state.value.cityTextFieldState.text.toString(),
                    birthDate = state.value.birthDate.toString(),
                    genre = state.value.genre!!
                )
                registerParticipant(registrationForm)
                    .onSuccess { referenceNumber ->
                        _state.update { it.copy(
                            isLoading = false,
                            birthDate = null,
                            genre = null,
                            educationLevel=null,
                            errorMessage = null,
                        ) }.apply {
                            _state.value.fullNameTextFieldState.clearText()
                            _state.value.securityCodeTextFieldState.clearText()
                            _state.value.familyNameTextFieldState.clearText()
                            _state.value.cityTextFieldState.clearText()
                        }
                        alert.sendAlert(AlertEvent.Success(UiText.DynamicString("Participant Enregistrer avec success")))
                        _eventChannel.send(RegisterFormEvent.OnSuccess)
                    }
                    .onFailure { error ->
                       val errorMessage= when (error) {
                            is DataError.Remote.InvalidOperation -> error.toUiText()
                            else -> {
                                alert.sendAlert(AlertEvent.Error(error.toUiText()))
                                null
                            }
                        }
                        _state.update { it.copy(
                            isLoading = false,
                            errorMessage =errorMessage
                        ) }
                    }

            }catch (e: Exception) {
                alert.sendAlert(AlertEvent.Error(DataError.Remote.Unknown.toUiText()))
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =null
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