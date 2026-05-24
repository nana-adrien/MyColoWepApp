package empire.digiprem.mycoloapp.core.data.networking.services

import empire.digiprem.mycoloapp.core.data.mappers.toDomain
import empire.digiprem.mycoloapp.core.domain.model.AppUserSession
import empire.digiprem.mycoloapp.core.domain.service.AppSessionManager
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

actual class AppSessionManagerHandler : AppSessionManager,
    SessionManager {
    private val _sessionFlow = MutableStateFlow<AppUserSession?>(null)
    private val storageKey = "sb_admin_session"
    private val storageAppSessionKey = "app_session"
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    init {
        // Charge la session initiale au démarrage
        _sessionFlow.value = runCatching {
            localStorage.getItem(storageAppSessionKey)?.let {
                json.decodeFromString<AppUserSession>(it)
            }
        }.getOrNull()
    }


    actual override fun observeSession(): Flow<AppUserSession?> =_sessionFlow

    actual override fun save(session: AppUserSession) {
        runCatching { localStorage.setItem(storageAppSessionKey, json.encodeToString(session)) }
        _sessionFlow.value=session
    }

    actual override suspend fun loadAppUserSession(): AppUserSession? =localStorage.getItem(storageAppSessionKey)?.let {
        json.decodeFromString<AppUserSession>(it)
    }
    actual override fun clearSession() {
        runCatching { localStorage.removeItem(storageKey) }
        _sessionFlow.value = null
    }

    actual override suspend fun saveSession(session: UserSession) {
        runCatching { localStorage.setItem(storageKey, json.encodeToString(session)) }
        save(session.toDomain())
    }

    actual override suspend fun deleteSession() {
        runCatching { localStorage.removeItem(storageKey) }
        clearSession()
    }


    actual override suspend fun loadSession(): UserSession = localStorage.getItem(storageKey)?.let {
        json.decodeFromString<UserSession>(it)
    } ?: error("Aucune session trouvée dans le stockage local")

}