package empire.digiprem.mycoloapp.core.data.providers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

 fun dataStoreProvider(context: Context): DataStore<Preferences> {
    return createStoreProvider {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}