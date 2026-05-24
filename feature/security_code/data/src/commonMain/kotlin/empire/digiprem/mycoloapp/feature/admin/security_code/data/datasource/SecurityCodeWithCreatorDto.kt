package empire.digiprem.mycoloapp.feature.admin.security_code.data.datasource

import kotlinx.serialization.Serializable

@Serializable
data class SecurityCodeWithCreatorDto(
    val id: String,
    val code: String,
    val isActive: Boolean,
    val maxUses: Int,
    val currentUses: Int,
    val createdBy: String? = null,
    val username: String? = null,
    val lastName: String? = null,
    val creatorEmail: String, // ✅ email du créateur
    val createdAt: String,
)