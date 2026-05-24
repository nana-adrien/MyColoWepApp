package empire.digiprem.mycoloapp.feature.registration.presentation.form;

sealed interface RegisterFormEvent {
    data object OnSuccess : RegisterFormEvent
    data class OnError(val message: String) : RegisterFormEvent
}