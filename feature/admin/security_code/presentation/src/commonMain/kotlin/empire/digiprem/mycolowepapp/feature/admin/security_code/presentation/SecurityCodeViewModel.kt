package empire.digiprem.mycolowepapp.feature.admin.security_code.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycolowepapp.core.domain.util.onFailure
import empire.digiprem.mycolowepapp.core.domain.util.onSuccess
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.error.SecurityCodeError
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.usecase.GenerateSecurityCodeUseCase
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.usecase.GetSecurityCodesUseCase
import empire.digiprem.mycolowepapp.feature.admin.security_code.domain.usecase.ToggleSecurityCodeUseCase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SecurityCodeViewModel(
    private val getSecurityCodes: GetSecurityCodesUseCase,
    private val generateSecurityCode: GenerateSecurityCodeUseCase,
    private val toggleSecurityCode: ToggleSecurityCodeUseCase,
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    private val _state = MutableStateFlow(SecurityCodeState())
    val state: StateFlow<SecurityCodeState> = _state.asStateFlow()

    init { loadCodes() }

    fun onAction(action: SecurityCodeAction) {
        when (action) {
            is SecurityCodeAction.OnRetryLoad           -> loadCodes()
            is SecurityCodeAction.OnGenerateCode        -> generateCode()
            is SecurityCodeAction.OnToggleActive        -> toggleActive(action.id, action.currentIsActive)
            is SecurityCodeAction.OnDismissError        -> _state.update { it.copy(actionError = null, loadError = null) }
            is SecurityCodeAction.OnDismissSuccess      -> _state.update { it.copy(successMessage = null) }
        }
    }

    private fun loadCodes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }
            getSecurityCodes()
                .onSuccess { codes -> _state.update { it.copy(codes = codes, isLoading = false) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, loadError = error.toMessage()) } }
        }
    }

    private fun generateCode() {
        viewModelScope.launch {
            val user = runCatching { supabaseClient.auth.currentUserOrNull() }.getOrNull()
            val email = user?.email ?: "admin"
            val id = user?.id ?: ""

            _state.update { it.copy(isGenerating = true, actionError = null) }
            generateSecurityCode(adminEmail = email, adminId = id)
                .onSuccess { code ->
                    _state.update { state ->
                        state.copy(
                            codes = listOf(code) + state.codes,
                            isGenerating = false,
                            successMessage = "Code ${code.code} généré avec succès"
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isGenerating = false, actionError = error.toMessage()) }
                }
        }
    }

    private fun toggleActive(id: String, currentIsActive: Boolean) {
        val newIsActive = !currentIsActive
        viewModelScope.launch {
            toggleSecurityCode(id = id, isActive = newIsActive)
                .onSuccess {
                    _state.update { state ->
                        state.copy(
                            codes = state.codes.map { c ->
                                if (c.id == id) c.copy(isActive = newIsActive) else c
                            },
                            successMessage = if (newIsActive) "Code réactivé" else "Code désactivé"
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(actionError = error.toMessage()) }
                }
        }
    }
}

private fun SecurityCodeError.toMessage(): String = when (this) {
    SecurityCodeError.Unauthorized -> "Accès non autorisé"
    SecurityCodeError.NetworkError -> "Connexion impossible"
    is SecurityCodeError.Unknown   -> message
}
