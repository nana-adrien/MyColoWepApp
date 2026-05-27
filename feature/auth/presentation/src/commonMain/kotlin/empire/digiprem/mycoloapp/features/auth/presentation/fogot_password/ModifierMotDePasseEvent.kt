package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface ModifierMotDePasseEvent {
    data object OnMotDePasseModifie : ModifierMotDePasseEvent
}
