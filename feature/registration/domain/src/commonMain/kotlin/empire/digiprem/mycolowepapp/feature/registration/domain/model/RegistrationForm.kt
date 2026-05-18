package empire.digiprem.mycolowepapp.feature.registration.domain.model

data class RegistrationForm(
    val fullName: String = "",
    val age: String = "",
    val jobStatus: JobStatus? = null,
    val familyName: String = "",
    val securityCode: String = ""
)

enum class JobStatus {
    STUDENT_SCHOOL,
    STUDENT_HIGHER,
    WORKER,
    SEEKING_WORK
}
