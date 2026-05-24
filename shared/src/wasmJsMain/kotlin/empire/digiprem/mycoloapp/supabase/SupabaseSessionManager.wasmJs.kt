package empire.digiprem.mycoloapp.supabase

import io.github.jan.supabase.auth.user.UserSession
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

actual fun createPlatformSessionManager(): AppSessionManager = BrowserLocalSessionManager()

class BrowserLocalSessionManager : AppSessionManager {
    // Flow interne qui émet à chaque changement de session
    private val _sessionFlow = MutableStateFlow<UserSession?>(null)

    init {
        // Charge la session initiale au démarrage
        _sessionFlow.value = runCatching {
            localStorage.getItem(storageKey)?.let {
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

    override suspend fun loadSession(): UserSession = localStorage.getItem(storageKey)?.let {
        json.decodeFromString<UserSession>(it)
    } ?: error("Aucune session trouvée dans le stockage local")

    override suspend fun deleteSession() {
        runCatching { localStorage.removeItem(storageKey) }
        _sessionFlow.value = null
    }

    override fun observeSession(): Flow<UserSession?> = _sessionFlow

}
