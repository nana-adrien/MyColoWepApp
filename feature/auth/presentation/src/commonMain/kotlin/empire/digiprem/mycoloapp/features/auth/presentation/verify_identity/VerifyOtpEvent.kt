package empire.digiprem.mycoloapp.features.auth.presentation.verify_identity

sealed interface VerifyOtpEvent {
    data object OnOtpVerified : VerifyOtpEvent
}
