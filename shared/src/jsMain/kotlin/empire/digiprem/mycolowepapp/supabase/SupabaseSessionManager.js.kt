package empire.digiprem.mycolowepapp.supabase

import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import web.storage.localStorage

actual fun createPlatformSessionManager(): SessionManager = BrowserLocalSessionManager()

class BrowserLocalSessionManager : SessionManager {

    private val storageKey = "sb_admin_session"
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    override suspend fun saveSession(session: UserSession) {
        runCatching { localStorage.setItem(storageKey, json.encodeToString(session)) }
    }

    override suspend fun loadSession(): UserSession? = runCatching {
        localStorage.getItem(storageKey)?.let { json.decodeFromString<UserSession>(it) }
    }.getOrNull()

    override suspend fun deleteSession() {
        runCatching { localStorage.removeItem(storageKey) }
    }
}
