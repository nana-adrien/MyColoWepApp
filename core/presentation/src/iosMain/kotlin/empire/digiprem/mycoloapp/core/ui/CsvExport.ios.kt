package empire.digiprem.mycoloapp.core.ui

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.*
import platform.UIKit.*
actual fun downloadCsvFile(csvContent: String, fileName: String) {
    // 1. Créer le fichier temporaire
    val tempDir = NSTemporaryDirectory()
    val filePath = "$tempDir/$fileName"
    val fileUrl = NSURL.fileURLWithPath(filePath)

    // 2. Écrire le contenu CSV ✅
    val nsString = NSString.create(string = csvContent)
    val data = nsString.dataUsingEncoding(NSUTF8StringEncoding) ?: return

    data.writeToURL(fileUrl, atomically = true)

    // 3. Partager via UIActivityViewController
    val activityController = UIActivityViewController(
        activityItems = listOf(fileUrl),
        applicationActivities = null
    )

    // 4. Présenter le partage
    UIApplication.sharedApplication.keyWindow
        ?.rootViewController
        ?.presentViewController(
            activityController,
            animated = true,
            completion = null
        )
}