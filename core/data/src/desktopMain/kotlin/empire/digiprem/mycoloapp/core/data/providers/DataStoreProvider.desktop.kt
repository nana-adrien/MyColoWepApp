package empire.digiprem.mycoloapp.core.data.providers

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

// jvmMain (ou desktopMain)
 fun dataStoreProvider(): DataStore<Preferences> {
return createStoreProvider {
        // Dossier utilisateur — persiste entre les redémarrages
        val appDir = File(System.getProperty("user.home"), ".mycoloapp")
        appDir.mkdirs() // crée le dossier si inexistant
        File(appDir, DATA_STORE_FILE_NAME).absolutePath
    }
}