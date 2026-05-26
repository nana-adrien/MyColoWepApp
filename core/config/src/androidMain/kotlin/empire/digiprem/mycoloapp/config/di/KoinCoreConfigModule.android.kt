package empire.digiprem.mycoloapp.config.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import empire.digiprem.mycoloapp.core.data.providers.dataStoreProvider
import org.koin.dsl.module

actual val coreConfigPlatformModule = module{

    single<DataStore<Preferences>>{ dataStoreProvider(get()) }
}