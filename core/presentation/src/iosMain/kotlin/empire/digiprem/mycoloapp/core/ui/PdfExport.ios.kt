package empire.digiprem.mycoloapp.core.ui

import platform.UIKit.UIMarkupTextPrintFormatter
import platform.UIKit.UIPrintInfo
import platform.UIKit.UIPrintInfoOutputType
import platform.UIKit.UIPrintInteractionController


// iosMain/kotlin/Platform.kt
actual fun openPrintWindow(htmlContent: String) {
    val printInfo = UIPrintInfo.printInfo()
    printInfo.outputType = UIPrintInfoOutputType.UIPrintInfoOutputGeneral
    printInfo.jobName = "Impression"

    val printController = UIPrintInteractionController.sharedPrintController()
    printController.printInfo = printInfo
    printController.printFormatter = UIMarkupTextPrintFormatter(htmlContent)

    printController.presentAnimated(true, completionHandler = null)
}
