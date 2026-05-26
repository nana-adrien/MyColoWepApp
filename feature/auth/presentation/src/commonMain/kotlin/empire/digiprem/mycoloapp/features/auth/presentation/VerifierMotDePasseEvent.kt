package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface VerifierMotDePasseEvent {
    data object OnOtpVerifie : VerifierMotDePasseEvent
}
