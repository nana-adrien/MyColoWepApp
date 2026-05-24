package empire.digiprem.mycoloapp.core.ui

import kotlinx.browser.window

actual fun openPrintWindow(htmlContent: String) {
    val printWindow = window.open("", "_blank", "width=960,height=720") ?: run {
        window.alert("Veuillez autoriser les popups pour exporter en PDF.")
        return
    }
    printWindow.document.open()
    printWindow.document.write(htmlContent)
    printWindow.document.close()
    printWindow.focus()
    printWindow.print()
}
