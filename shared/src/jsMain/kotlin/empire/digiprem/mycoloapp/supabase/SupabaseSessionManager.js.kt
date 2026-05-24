package empire.digiprem.mycoloapp.supabase

import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import web.storage.localStorage

actual fun createPlatformSessionManager(): AppSessionManager = BrowserLocalSessionManager()

class BrowserLocalSessionManager : AppSessionManager {
    private val _sessionFlow = MutableStateFlow<UserSession?>(null)

    init {
        // Charge la session initiale au démarrage
        _sessionFlow.value = runCatching {
            kotlinx.browser.localStorage.getItem(storageKey)?.let {
                json.decodeFromString<UserSession>(it)
            }
        }.getOrNull()
    }
    private val storageKey = "sb_admin_session"
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    override suspend fun saveSession(session: UserSession) {
        runCatching { localStorage.setItem(storageKey, json.encodeToString(session)) }
        _sessionFlow.value = session
    }

    override suspend fun loadSession(): UserSession? = runCatching {
        localStorage.getItem(storageKey)?.let { json.decodeFromString<UserSession>(it) }
    }.getOrNull()

    override suspend fun deleteSession() {
        runCatching { localStorage.removeItem(storageKey) }
        _sessionFlow.value = null
    }

    override fun observeSession(): Flow<UserSession?> =_sessionFlow
}
