package empire.digiprem.mycoloapp.feature.registration.presentation

sealed interface RegistrationEvent {
    data class OnRegistrationSuccess(val referenceNumber: String) : RegistrationEvent
}
