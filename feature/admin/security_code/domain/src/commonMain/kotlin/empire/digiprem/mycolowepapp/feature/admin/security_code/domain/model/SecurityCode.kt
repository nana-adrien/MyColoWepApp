package empire.digiprem.mycolowepapp.feature.admin.security_code.domain.model

data class SecurityCode(
    val id: String,
    val code: String,
    val createdByEmail: String,
    val usageCount: Int,
    val isActive: Boolean,
    val createdAt: String
)
