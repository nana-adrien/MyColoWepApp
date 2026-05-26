package empire.digiprem.mycoloapp.core.data.services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import empire.digiprem.mycoloapp.core.data.mappers.toDomain
import empire.digiprem.mycoloapp.core.data.mappers.toSerializable
import empire.digiprem.mycoloapp.core.domain.model.AppUserSession
import empire.digiprem.mycoloapp.core.domain.service.AppSessionManager
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.exception.SessionRequiredException
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json


class AppSessionManagerHandler(val dataStore: DataStore<Preferences>) : AppSessionManager, SessionManager {

    private val preferencesSupabaseSessionKey = stringPreferencesKey("supabase_session")
    private val preferencesAppSessionKey = stringPreferencesKey("app_session")


    override fun observeSession(): Flow<AppUserSession?> = dataStore.data.map { preferences ->
        val serializedJson = preferences[preferencesAppSessionKey]
        serializedJson?.let {
            //val decryptData = cryptoManager.decrypt(it)
            // logger.e("decrypt Data", decryptData)
            Json.decodeFromString<AppUserSession>(serializedJson)
        }

    }.catch { e ->
        println("Error while reading from storage $e")
        null
    }

    override suspend fun save(session: AppUserSession?) {
        if (session == null) {
            dataStore.edit { it.remove(preferencesAppSessionKey) }
            return
        }
        val serializableAppUserSession = Json.encodeToString(session.toSerializable())
        dataStore.edit { preferences ->
            preferences[preferencesAppSessionKey] = serializableAppUserSession
        }
    }

    override suspend fun loadAppUserSession(): AppUserSession? {
        return observeSession().firstOrNull()
    }

    override suspend fun clearSession() {
        save(null)
    }

    override suspend fun saveSession(session: UserSession) {
        dataStore.edit { it[preferencesSupabaseSessionKey] = Json.encodeToString(session)}
        save(session.toDomain())

    }

    override suspend fun loadSession(): UserSession? {
        val json =  dataStore.data.first()[preferencesSupabaseSessionKey]
        return json?.let { Json.decodeFromString(json) }
    }

    override suspend fun deleteSession() {
        dataStore.edit { it.remove(preferencesSupabaseSessionKey) }
        clearSession()
    }


}