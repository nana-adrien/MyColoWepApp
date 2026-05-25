package empire.digiprem.mycoloapp.features.security_code.data.dto

import empire.digiprem.mycoloapp.features.security_code.data.datasource.SecurityCodeWithCreatorDto
import empire.digiprem.mycoloapp.features.security_code.domain.model.SecurityCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SecurityCodeDto(
    val id: String,
    val code: String,
    @SerialName("created_by")       val createdBy: String,
    @SerialName("created_by_email") val createdByEmail: String,
    @SerialName("usage_count")      val usageCount: Int,
    @SerialName("is_active")        val isActive: Boolean,
    @SerialName("created_at")       val createdAt: String
)

fun SecurityCodeWithCreatorDto.toDomain(): SecurityCode = SecurityCode(
    id             = id,
    code           = code,
    createdByEmail = creatorEmail!!,
    usageCount     = currentUses,
    isActive       = isActive,
    createdAt      = createdAt.take(10)  // garder juste la date YYYY-MM-DD
)

fun SecurityCodeDto.toDomain(): SecurityCode = SecurityCode(
    id             = id,
    code           = code,
    createdByEmail = createdByEmail,
    usageCount     = usageCount,
    isActive       = isActive,
    createdAt      = createdAt.take(10)  // garder juste la date YYYY-MM-DD
)

@Serializable
data class SecurityCodeInsertDto(
    val code: String,
    val createdBy: String,
)
