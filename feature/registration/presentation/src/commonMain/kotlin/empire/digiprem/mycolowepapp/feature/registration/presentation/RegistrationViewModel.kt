package empire.digiprem.mycolowepapp.feature.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private val _eventChannel = Channel<RegistrationEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.OnFullNameChange -> _state.update {
                it.copy(form = it.form.copy(fullName = action.value))
            }
            is RegistrationAction.OnAgeChange -> {
                if (action.value.all { c -> c.isDigit() } && action.value.length <= 3) {
                    _state.update { it.copy(form = it.form.copy(age = action.value)) }
                }
            }
            is RegistrationAction.OnFamilyNameChange -> _state.update {
                it.copy(form = it.form.copy(familyName = action.value))
            }
            is RegistrationAction.OnJobStatusChange -> _state.update {
                it.copy(form = it.form.copy(jobStatus = action.status))
            }
            is RegistrationAction.OnSecurityCodeChange -> _state.update {
                it.copy(form = it.form.copy(securityCode = action.value))
            }
            is RegistrationAction.OnSubmitClick -> submit()
            is RegistrationAction.OnClearError -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun submit() {
        val form = _state.value.form
        val error = validateForm(form)
        if (error != null) {
            _state.update { it.copy(errorMessage = error) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val referenceNumber = generateReferenceNumber()
            _state.update { it.copy(isLoading = false) }
            _eventChannel.send(RegistrationEvent.OnRegistrationSuccess(referenceNumber))
        }
    }

    private fun validateForm(form: RegistrationForm): String? = when {
        form.fullName.isBlank() -> "Le nom complet est requis"
        form.age.isBlank() || form.age.toIntOrNull() == null -> "Veuillez entrer un âge valide"
        form.familyName.isBlank() -> "Le nom de famille est requis"
        form.jobStatus == null -> "Veuillez sélectionner votre statut"
        form.securityCode.isBlank() -> "Le code de sécurité est requis"
        else -> null
    }

    private fun generateReferenceNumber(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val suffix = (1..5).map { chars.random() }.joinToString("")
        return "#MC-2026-$suffix"
    }
}
