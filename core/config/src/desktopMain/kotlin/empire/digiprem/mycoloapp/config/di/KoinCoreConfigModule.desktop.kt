package empire.digiprem.mycoloapp.config.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import empire.digiprem.mycoloapp.core.data.providers.dataStoreProvider
import empire.digiprem.mycoloapp.core.data.services.AppSessionManagerHandler
import empire.digiprem.mycoloapp.core.domain.service.AppSessionManager
import io.github.jan.supabase.auth.SessionManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val coreConfigPlatformModule=module{
    single<DataStore<Preferences>>{ dataStoreProvider() }
}
