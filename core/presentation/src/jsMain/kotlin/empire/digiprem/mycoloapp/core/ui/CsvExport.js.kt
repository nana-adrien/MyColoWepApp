package empire.digiprem.mycoloapp.core.ui

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

actual fun downloadCsvFile(csvContent: String, fileName: String) {
    val blob = Blob(
        blobParts = arrayOf("﻿$csvContent"),   // BOM UTF-8 pour Excel
        options   = BlobPropertyBag(type = "text/csv;charset=utf-8;")
    )
    val url  = URL.createObjectURL(blob)
    val link = document.createElement("a") as HTMLAnchorElement
    link.href     = url
    link.download = fileName
    link.style.display = "none"
    document.body?.appendChild(link)
    link.click()
    document.body?.removeChild(link)
    URL.revokeObjectURL(url)
}
