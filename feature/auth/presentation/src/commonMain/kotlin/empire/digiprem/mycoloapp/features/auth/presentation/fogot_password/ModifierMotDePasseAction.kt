package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface ModifierMotDePasseAction {
    data object OnConfirmerClick : ModifierMotDePasseAction
    data object OnToggleNouveauMotDePasseVisibility : ModifierMotDePasseAction
    data object OnToggleConfirmationMotDePasseVisibility : ModifierMotDePasseAction
    data object OnEffacerErreur : ModifierMotDePasseAction
}
