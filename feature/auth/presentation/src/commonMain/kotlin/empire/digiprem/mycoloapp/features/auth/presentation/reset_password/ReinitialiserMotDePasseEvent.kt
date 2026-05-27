package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface ReinitialiserMotDePasseEvent {
    data object OnLienEnvoye : ReinitialiserMotDePasseEvent
    data class OnNaviguerVersVerification(val email: String) : ReinitialiserMotDePasseEvent
}
