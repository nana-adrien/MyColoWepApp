package empire.digiprem.mycolowepapp.feature.registration.domain.model

data class RegistrationForm(
    val fullName: String = "",
    val birthDate: String = "",
    val educationLevel: EducationLevel? = null,
    val familyName: String = "",
    val securityCode: String = "",
    val genre: Genre = Genre.MALE,
)
