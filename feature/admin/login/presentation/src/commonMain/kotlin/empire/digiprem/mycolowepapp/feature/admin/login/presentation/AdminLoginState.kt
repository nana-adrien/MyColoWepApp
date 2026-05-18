package empire.digiprem.mycolowepapp.feature.admin.login.presentation

data class AdminLoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
