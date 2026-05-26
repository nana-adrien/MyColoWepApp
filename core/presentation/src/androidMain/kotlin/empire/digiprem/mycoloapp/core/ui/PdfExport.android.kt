package empire.digiprem.mycoloapp.core.ui

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.webkit.WebView
import org.koin.java.KoinJavaComponent.inject

// androidMain/kotlin/Platform.kt

actual fun openPrintWindow(htmlContent: String) {
   /* val context: Context  // ton context global
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

    val printAdapter = object : PrintDocumentAdapter() {
        override fun onLayout(
            oldAttributes: PrintAttributes?,
            newAttributes: PrintAttributes,
            cancellationSignal: CancellationSignal?,
            callback: LayoutResultCallback,
            extras: Bundle?
        ) {
            callback.onLayoutFinished(
                PrintDocumentInfo.Builder("document.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build(), true
            )
        }

        override fun onWrite(
            pages: Array<out PageRange>?,
            destination: ParcelFileDescriptor,
            cancellationSignal: CancellationSignal?,
            callback: WriteResultCallback
        ) {
            val webView = WebView(context)
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            webView.printDocumentAdapter("document").onWrite(
                pages, destination, cancellationSignal, callback
            )
        }
    }

    printManager.print("Impression", printAdapter, PrintAttributes.Builder().build())
*/}