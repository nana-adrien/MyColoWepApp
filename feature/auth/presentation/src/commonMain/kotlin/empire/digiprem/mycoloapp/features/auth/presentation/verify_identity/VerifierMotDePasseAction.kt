package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface VerifierMotDePasseAction {
    data object OnVerifierClick : VerifierMotDePasseAction
    data object OnRenvoyerCodeClick : VerifierMotDePasseAction
    data object OnEffacerErreur : VerifierMotDePasseAction
}
