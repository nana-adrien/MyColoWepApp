package empire.digiprem.mycoloapp.feature.admin.security_code.presentation

import empire.digiprem.mycoloapp.feature.admin.security_code.domain.model.SecurityCode

data class SecurityCodeState(
    val codes: List<SecurityCode> = emptyList(),
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val loadError: String? = null,
    val actionError: String? = null,
    val successMessage: String? = null
)
