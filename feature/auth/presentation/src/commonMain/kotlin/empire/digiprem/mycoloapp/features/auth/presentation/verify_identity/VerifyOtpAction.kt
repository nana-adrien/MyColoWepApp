package empire.digiprem.mycoloapp.features.auth.presentation.verify_identity

sealed interface VerifyOtpAction {
    data object OnVerifyClick : VerifyOtpAction
    data object OnResendCodeClick : VerifyOtpAction
    data object OnClearError : VerifyOtpAction
}
