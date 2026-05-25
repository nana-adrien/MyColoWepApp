package empire.digiprem.mycoloapp.features.registration.presentation

sealed interface RegistrationEvent {
    data class OnRegistrationSuccess(val referenceNumber: String) : RegistrationEvent
}
