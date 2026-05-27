package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface ReinitialiserMotDePasseAction {
    data object OnEnvoyerLienClick : ReinitialiserMotDePasseAction
    data object OnEffacerErreur : ReinitialiserMotDePasseAction
    data object OnNaviguerVersVerification : ReinitialiserMotDePasseAction
}
