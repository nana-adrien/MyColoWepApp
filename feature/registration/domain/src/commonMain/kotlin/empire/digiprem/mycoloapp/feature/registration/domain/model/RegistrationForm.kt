package empire.digiprem.mycoloapp.feature.registration.domain.model

data class RegistrationForm(
    val fullName: String = "",
    val familyName: String = "",
    val city: String = "",
    val birthDate: String = "",
    val educationLevel: EducationLevel? = null,
    val securityCode: String = "",
    val genre: Genre = Genre.MALE,
)
