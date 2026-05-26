package empire.digiprem.mycoloapp.core.data.providers

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.WebLocalStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer

 fun dataStoreProvider(): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    storage = WebLocalStorage(
        serializer = PreferencesSerializer,
        name = DATA_STORE_FILE_NAME
    )
)