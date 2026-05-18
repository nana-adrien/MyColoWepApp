package empire.digiprem.mycolowepapp.feature.registration.presentation

sealed interface RegistrationEvent {
    data class OnRegistrationSuccess(val referenceNumber: String) : RegistrationEvent
}
