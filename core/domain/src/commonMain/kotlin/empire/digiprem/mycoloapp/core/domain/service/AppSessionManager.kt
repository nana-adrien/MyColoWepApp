package empire.digiprem.mycoloapp.core.domain.service

import empire.digiprem.mycoloapp.core.domain.model.AppUserSession
import kotlinx.coroutines.flow.Flow

interface AppSessionManager{
    fun observeSession(): Flow<AppUserSession?>
    suspend fun save(session: AppUserSession?)
     suspend fun loadAppUserSession(): AppUserSession?
    suspend fun clearSession()
}