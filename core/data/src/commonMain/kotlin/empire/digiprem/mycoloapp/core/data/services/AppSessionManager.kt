package empire.digiprem.mycoloapp.core.data.networking.services

import empire.digiprem.mycoloapp.core.domain.model.AppUserSession
import empire.digiprem.mycoloapp.core.domain.service.AppSessionManager
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.Flow

expect class AppSessionManagerHandler: AppSessionManager, SessionManager {
    override fun observeSession(): Flow<AppUserSession?>
    override fun save(session: AppUserSession)
    override suspend fun loadAppUserSession(): AppUserSession?
    override suspend fun loadSession(): UserSession
    override fun clearSession()
    override suspend fun saveSession(session: UserSession)
    override suspend fun deleteSession()

}